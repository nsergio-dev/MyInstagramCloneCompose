package com.nsergio.dev.myinstagramcompose.features.chat.presentation

import com.nsergio.dev.myinstagramcompose.features.chat.models.ChatItem
import com.nsergio.dev.myinstagramcompose.features.chat.models.ChatStory

data class ChatInboxState(
    val isLoading: Boolean = false,
    val stories: List<ChatStory> = emptyList(),
    val messages: List<ChatItem> = emptyList(),
    val error: String? = null
)