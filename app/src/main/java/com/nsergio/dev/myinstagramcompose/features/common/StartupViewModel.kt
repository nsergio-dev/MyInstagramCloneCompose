package com.nsergio.dev.myinstagramcompose.features.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random
import kotlin.random.nextInt

@HiltViewModel
class StartupViewModel @Inject constructor() : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun startup() {
        if (!_isLoading.value) return
        viewModelScope.launch {
            createMeUser()
            insertUsersWithMedia(Random.nextInt(15..25))
            delay(3_000)
            _isLoading.value = false
        }
    }
}