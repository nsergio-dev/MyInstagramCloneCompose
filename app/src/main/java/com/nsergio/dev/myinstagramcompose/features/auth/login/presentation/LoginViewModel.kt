package com.nsergio.dev.myinstagramcompose.features.auth.login.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {

    /**
     * State for email input
     */
    private val _email: MutableStateFlow<String> = MutableStateFlow("cb")
    val email: StateFlow<String> = _email

    /**
     * State for password input
     */
    private val _password: MutableStateFlow<String> = MutableStateFlow("cc")
    val password: StateFlow<String> = _password

    /**
     * Update new value for email
     */
    fun onEmailChange(newValue: String) {
        _email.value = newValue
    }

    /**
     * Update new value for password
     */
    fun onPasswordChange(newValue: String) {
        _password.value = newValue
    }

    /**
     * Emits `true` when both email and password are not blank
     */
    val isLoginButtonEnabled: StateFlow<Boolean> = combine(
        email,
        password
    ) { emailObserver, passwordObserver ->
        emailObserver.isNotBlank() && passwordObserver.isNotBlank()
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)
}