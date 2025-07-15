package com.nsergio.dev.myinstagramcompose.features.photo_preview.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nsergio.dev.myinstagramcompose.features.feed.data.PostRepository
import com.nsergio.dev.myinstagramcompose.features.feed.domain.model.PostId
import com.nsergio.dev.myinstagramcompose.features.feed.domain.model.PostWithMedia
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class PhotoViewerViewModel @Inject constructor(
    private val repo: PostRepository
) : ViewModel() {

    private val _params = MutableStateFlow<Pair<PostId, Int>?>(null)

    val post: StateFlow<PostWithMedia?> = combine(_params) { params ->
        params.first()?.first?.let {
            val post = repo.findById(it)
            post
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val startIndex: StateFlow<Int> = _params
        .map { it?.second ?: 0 }
        .stateIn(viewModelScope, SharingStarted.Eagerly, 0)

    /**
     * set  postId with touched photo
     */
    fun setParams(postId: String, index: Int) {
        _params.value = postId.toPostId() to index
    }
}

private fun String.toPostId() = PostId(this)
