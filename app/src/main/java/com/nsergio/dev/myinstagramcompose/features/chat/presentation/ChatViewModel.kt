package com.nsergio.dev.myinstagramcompose.features.chat.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nsergio.dev.myinstagramcompose.features.chat.models.ChatInbox
import com.nsergio.dev.myinstagramcompose.features.chat.usecase.GetChatInboxUseCase
import com.nsergio.dev.myinstagramcompose.features.profile.data.FakeUserRepository
import com.nsergio.dev.myinstagramcompose.features.profile.domain.model.User
import com.nsergio.dev.myinstagramcompose.features.profile.domain.model.UserId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
@HiltViewModel
class ChatViewModel @Inject constructor(
    private val getChatInboxUseCase: GetChatInboxUseCase,
    private val userRepository: FakeUserRepository
) : ViewModel() {

    private val _state: MutableStateFlow<ChatInboxState> = MutableStateFlow(ChatInboxState())
    val state: StateFlow<ChatInboxState> = _state.asStateFlow()

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    val uiState: StateFlow<ChatInboxState> =
        combine(
            state,
            query.debounce(300)
        ) { state, query ->
            val qTrim = query.trim()
            if (qTrim.isEmpty()) {
                state
            } else {
                /*val filteredStories = state.stories.filter { st ->
                    st.username.contains(qTrim, ignoreCase = true) ||
                            st.caption.contains(qTrim, ignoreCase = true)
                }*/
                val filteredMessages = state.messages.filter { msg ->
                    msg.username.contains(qTrim, ignoreCase = true)
                }
                state.copy(messages = filteredMessages)
                //state.copy(stories = filteredStories, messages = filteredMessages)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = _state.value
        )

    fun load() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            runCatching { getChatInboxUseCase.invoke() }
                .onSuccess { inbox: ChatInbox ->
                    _state.value = ChatInboxState(
                        isLoading = false,
                        stories = inbox.stories,
                        messages = inbox.messages,
                        error = null
                    )
                }
                .onFailure { ex ->
                    _state.value = ChatInboxState(
                        isLoading = false,
                        stories = emptyList(),
                        messages = emptyList(),
                        error = ex.message ?: "Unknown error"
                    )
                }
        }
    }

    fun updateQuery(newQuery: String) {
        _query.value = newQuery
    }

    fun getUser(userId: String = "me") {
        val user = userRepository.getUser(UserId(userId))
        _user.value = user
    }
}