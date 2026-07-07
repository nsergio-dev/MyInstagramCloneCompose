package com.nsergio.dev.myinstagramcompose.features.reels.presentation

import androidx.lifecycle.ViewModel
import com.nsergio.dev.myinstagramcompose.features.common.fakeUsers
import com.nsergio.dev.myinstagramcompose.features.feed.domain.model.PostId
import com.nsergio.dev.myinstagramcompose.features.feed.domain.model.PostWithMedia
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ReelsViewModel : ViewModel() {

    private val _reels = MutableStateFlow(allPostsSorted())

    val reels: StateFlow<List<PostWithMedia>> = _reels.asStateFlow()

    private fun allPostsSorted(): List<PostWithMedia> =
        fakeUsers.flatMap { it.posts }.sortedByDescending { it.createdAt }

    fun onLikeToggle(postId: PostId) {
        val updated = updateLikeInFakeUsers(postId)
        if (updated) {
            _reels.value = allPostsSorted()
        }
    }

    fun onComment(postId: PostId) { /* TODO navegar a comentarios */ }

    private fun updateLikeInFakeUsers(postId: PostId): Boolean {

        val userIndex = fakeUsers.indexOfFirst { user -> user.posts.any { it.id == postId } }
        if (userIndex == -1) return false

        val user = fakeUsers[userIndex]
        val newPosts = user.posts.map { post ->
            if (post.id == postId) {
                val newLiked = !post.likedByMe
                val delta = if (newLiked) 1 else -1
                post.copy(
                    likedByMe = newLiked,
                    likeCount = (post.likeCount + delta).coerceAtLeast(0)
                )
            } else post
        }
        fakeUsers[userIndex] = user.copy(posts = newPosts)

        return true
    }
}