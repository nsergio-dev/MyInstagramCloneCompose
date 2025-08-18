package com.nsergio.dev.myinstagramcompose.features.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nsergio.dev.myinstagramcompose.features.feed.data.PostRepository
import com.nsergio.dev.myinstagramcompose.features.feed.domain.model.PostWithMedia
import com.nsergio.dev.myinstagramcompose.features.profile.data.FakeUserRepository
import com.nsergio.dev.myinstagramcompose.features.profile.domain.model.User
import com.nsergio.dev.myinstagramcompose.features.profile.domain.model.UserId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepo: FakeUserRepository,
    private val postRepository: PostRepository
) : ViewModel() {

    private val _userId = MutableStateFlow(UserId(""))

    val posts: StateFlow<List<PostWithMedia>> = _userId
        .map { postRepository.postsOf(it) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    /**
     * Emits a [User] every time [_userId] changes.
     */
    val user: StateFlow<User?> = _userId
        .map { id ->
            if (id.value.isBlank()) {
                null
            } else {
                val user = userRepo.getUser(id)
                user
            }
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    /**
     * Updates the user id; triggers a new load.
     *
     * @param userId Target user id
     */
    fun setUserId(userId: String) {
        val id = if (userId == "me") {
            val user = userRepo.getUser(UserId("me"))
            user?.id ?: UserId("")
        } else {
            UserId(userId)
        }
        _userId.value = id
    }
}