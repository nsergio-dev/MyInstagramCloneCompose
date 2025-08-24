package com.nsergio.dev.myinstagramcompose.features.create_post.presentation

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.nsergio.dev.myinstagramcompose.features.common.fakeUsers
import com.nsergio.dev.myinstagramcompose.features.feed.data.LocalFeedOverlay
import com.nsergio.dev.myinstagramcompose.features.feed.domain.model.Media
import com.nsergio.dev.myinstagramcompose.features.feed.domain.model.MediaType
import com.nsergio.dev.myinstagramcompose.features.feed.domain.model.PostId
import com.nsergio.dev.myinstagramcompose.features.feed.domain.model.PostWithMedia
import com.nsergio.dev.myinstagramcompose.features.profile.domain.model.User
import com.nsergio.dev.myinstagramcompose.features.profile.domain.model.UserId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID
import javax.inject.Inject

data class CreatePostState(
    val previewUri: Uri? = null,
    val isPublishing: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class CreatePostViewModel @Inject constructor(
    private val overlay: LocalFeedOverlay
) : ViewModel() {

    private val _state = MutableStateFlow(CreatePostState())
    val state: StateFlow<CreatePostState> = _state.asStateFlow()

    fun setPreview(uri: Uri?) {
        _state.value = _state.value.copy(previewUri = uri, error = null)
    }

    fun clearPreview() {
        _state.value = _state.value.copy(previewUri = null, error = null)
    }

    fun publish(caption: String = ""): Boolean {
        val uri = _state.value.previewUri ?: return false
        val meIndex = ensureMeUser()
        val me = fakeUsers[meIndex]

        val newPost = PostWithMedia(
            id = PostId(UUID.randomUUID().toString()),
            authorId = me.id,
            authorName = me.name,
            authorAvatarUrl = me.avatarUrl,
            media = listOf(
                Media(url = uri.toString(), type = MediaType.IMAGE, alt = "captured")
            ),
            caption = caption,
            likeCount = 0,
            commentCount = 0,
            shareCount = 0,
            createdAt = System.currentTimeMillis(),
            likedByMe = false
        )

        val updated = me.copy(posts = listOf(newPost) + me.posts)

        fakeUsers[meIndex] = updated

        overlay.addAtTop(newPost)
        _state.value = _state.value.copy(previewUri = null)
        return true
    }

    private fun ensureMeUser(): Int {
        val idx = fakeUsers.indexOfFirst { it.id.value == "me" }
        if (idx >= 0) return idx
        val newMe = User(
            id = UserId("me"),
            name = "me",
            realName = "Me",
            avatarUrl = "https://i.pravatar.cc/150?u=me",
            bio = "Hi!",
            followers = 0,
            following = 0,
            posts = emptyList()
        )
        fakeUsers.add(0, newMe)
        return 0
    }
}