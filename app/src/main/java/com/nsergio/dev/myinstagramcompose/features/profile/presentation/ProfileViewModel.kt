package com.nsergio.dev.myinstagramcompose.features.profile.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nsergio.dev.myinstagramcompose.features.profile.data.FakeUserRepository
import com.nsergio.dev.myinstagramcompose.features.profile.domain.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repo: FakeUserRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    /**
     * Holds the current user id to display.
     */
    private val _userId: MutableStateFlow<String> = MutableStateFlow("")

    /**
     * Emits a [User] every time [_userId] changes.
     */
    val user: StateFlow<User?> = _userId
        .map { id -> if (id.isBlank()) null else repo.getUser(id) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)


    /**
     * Updates the user id; triggers a new load.
     *
     * @param userId Target user id
     */
    fun setUserId(userId: String) {
        _userId.value = userId
    }
}