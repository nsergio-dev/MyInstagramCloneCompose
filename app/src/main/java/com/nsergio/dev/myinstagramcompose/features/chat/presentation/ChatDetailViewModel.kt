package com.nsergio.dev.myinstagramcompose.features.chat.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nsergio.dev.myinstagramcompose.core.utils.Utils
import com.nsergio.dev.myinstagramcompose.features.chat.data.FakeMessageRepository
import com.nsergio.dev.myinstagramcompose.features.chat.models.Message
import com.nsergio.dev.myinstagramcompose.features.profile.data.FakeUserRepository
import com.nsergio.dev.myinstagramcompose.features.profile.domain.model.User
import com.nsergio.dev.myinstagramcompose.features.profile.domain.model.UserId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatDetailViewModel @Inject constructor(
    private val userRepository: FakeUserRepository,
    private val messageRepository: FakeMessageRepository
) : ViewModel() {

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages.asStateFlow()

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()

    fun loadConversation() {
        viewModelScope.launch {
            _messages.value = messageRepository.getRandomMessages()
        }
    }

    fun sendMessage(text: String) {
        val trimmed = text.trim()
        if (trimmed.isBlank()) return

        val mine = Message(
            id = Utils.generateRandomString(),
            text = trimmed,
            fromMe = true
        )

        _messages.update { it + mine }

        viewModelScope.launch {
            delay(5_000)
            val reply = Message(
                id = Utils.generateRandomString(),
                text = messageRepository.getRandomMessage(),
                fromMe = false
            )
            _messages.update { it + reply }
        }
    }

    fun getUser(userId: String) {
        val user = userRepository.getUser(UserId(userId))
        _user.value = user
    }


}