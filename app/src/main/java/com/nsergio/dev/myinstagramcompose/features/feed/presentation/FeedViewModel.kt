package com.nsergio.dev.myinstagramcompose.features.feed.presentation


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.nsergio.dev.myinstagramcompose.core.ui.components.StoryItem
import com.nsergio.dev.myinstagramcompose.core.ui.components.StoryRing
import com.nsergio.dev.myinstagramcompose.features.feed.data.StoriesRepository
import com.nsergio.dev.myinstagramcompose.features.feed.domain.model.PostWithMedia
import com.nsergio.dev.myinstagramcompose.features.feed.domain.usecase.GetPostsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    getPosts: GetPostsUseCase,
    storiesRepository: StoriesRepository
) : ViewModel() {

    private val pagingFlow = getPosts.invoke().cachedIn(viewModelScope)
    private val seenStories = MutableStateFlow<Map<String, StoryRing>>(emptyMap())

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
        likedIds
    ) { paging, likes ->
        val postsLikedByMe = paging.map { postWithMedia ->
            val postLikedByMe = likes.contains(postWithMedia.id.value)
            postWithMedia.copy(likedByMe = postLikedByMe)
        }

        postsLikedByMe

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