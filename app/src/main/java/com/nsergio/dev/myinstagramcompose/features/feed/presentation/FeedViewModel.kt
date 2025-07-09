package com.nsergio.dev.myinstagramcompose.features.feed.presentation


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.nsergio.dev.myinstagramcompose.features.feed.domain.model.Post
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
    getPosts: GetPostsUseCase
) : ViewModel() {

    private val pagingFlow = getPosts.invoke().cachedIn(viewModelScope)

    /**
     * Set of post IDs the user has liked in this session.
     */
    private val _likedIds = MutableStateFlow<Set<String>>(emptySet())
    val likedIds: StateFlow<Set<String>> = _likedIds       // original flow

    /**
     * Emits PagingData where each Post knows whether it's liked.
     * The underlying paging stream is still collected only once.
     */
    val posts: StateFlow<PagingData<Post>> = combine(
        pagingFlow,
        likedIds
    ) { paging, likes ->
        paging.map { it.copy(liked = likes.contains(it.id)) }
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
}