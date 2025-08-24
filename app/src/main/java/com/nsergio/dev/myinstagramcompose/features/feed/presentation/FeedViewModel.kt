package com.nsergio.dev.myinstagramcompose.features.feed.presentation


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertHeaderItem
import androidx.paging.map
import com.nsergio.dev.myinstagramcompose.core.ui.components.StoryItem
import com.nsergio.dev.myinstagramcompose.core.ui.components.StoryRing
import com.nsergio.dev.myinstagramcompose.features.common.fakeUsers
import com.nsergio.dev.myinstagramcompose.features.feed.data.LocalFeedOverlay
import com.nsergio.dev.myinstagramcompose.features.feed.data.StoriesRepository
import com.nsergio.dev.myinstagramcompose.features.feed.domain.model.PostWithMedia
import com.nsergio.dev.myinstagramcompose.features.feed.domain.usecase.GetPostsUseCase
import com.nsergio.dev.myinstagramcompose.features.profile.domain.model.User
import com.nsergio.dev.myinstagramcompose.features.profile.domain.model.UserId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    getPosts: GetPostsUseCase,
    storiesRepository: StoriesRepository,
    private val overlay: LocalFeedOverlay
) : ViewModel() {

    private val pagingFlow = getPosts.invoke().cachedIn(viewModelScope)
    private val seenStories = MutableStateFlow<Map<String, StoryRing>>(emptyMap())

    val currentUser: StateFlow<User?> = flow {
        emit(fakeUsers.find { it.id == UserId("me") })
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = null
    )

    /**
     * Set of post IDs the user has liked in this session.
     */
    private val _likedIds = MutableStateFlow<Set<String>>(emptySet())
    val likedIds: StateFlow<Set<String>> = _likedIds

    val stories: StateFlow<List<StoryItem>> = combine(
        storiesRepository.getStoriesFlow(),
        seenStories
    ) { baseStories, stories ->

        baseStories.map { storyItem ->
            val story = stories[storyItem.idHistory.value]
            if (story != null) {
                storyItem.copy(ring = story)
            } else {
                storyItem
            }
        }.sortedByDescending { it.ring }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = emptyList()
    )

    /**
     * Emits PagingData where each Post knows whether it's liked.
     * The underlying paging stream is still collected only once.
     */
    val posts: StateFlow<PagingData<PostWithMedia>> = combine(
        pagingFlow,
        likedIds,
        overlay.headPosts
    ) { paging, likes, userPosts ->
            var paged = paging.map { it.copy(likedByMe = likes.contains(it.id.value)) }

            val postsUserMapped = userPosts.map { it.copy(likedByMe = likes.contains(it.id.value)) }

        postsUserMapped.asReversed().forEach { header ->
                paged = paged.insertHeaderItem(item = header)
            }
            paged
        }.stateIn(viewModelScope, SharingStarted.Eagerly, PagingData.empty())


    /**
     * Toggles like for the given post ID.
     */
    fun onLikeToggle(postId: String) {
        _likedIds.update { current ->
            if (current.contains(postId)) {
                current - postId
            } else {
                current + postId
            }
        }
    }

    fun updateStoryState(story: StoryItem, onClickStory: (StoryItem) -> Unit) {
        val idHistory = story.idHistory.value
        val newRing = setOf(idHistory to StoryRing.entries.random())
        seenStories.update { it + newRing }
        onClickStory.invoke(story)
    }

}