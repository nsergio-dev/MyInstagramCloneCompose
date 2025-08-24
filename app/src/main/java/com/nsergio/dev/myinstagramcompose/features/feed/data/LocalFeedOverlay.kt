// features/feed/data/LocalFeedOverlay.kt
package com.nsergio.dev.myinstagramcompose.features.feed.data

import com.nsergio.dev.myinstagramcompose.features.feed.domain.model.PostWithMedia
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalFeedOverlay @Inject constructor() {
    private val _headPosts = MutableStateFlow<List<PostWithMedia>>(emptyList())
    val headPosts: StateFlow<List<PostWithMedia>> = _headPosts.asStateFlow()

    fun addAtTop(post: PostWithMedia) {
        _headPosts.update { listOf(post) + it }
    }

    fun clear() {
        _headPosts.value = emptyList()
    }
}