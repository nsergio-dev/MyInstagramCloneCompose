package com.nsergio.dev.myinstagramcompose.features.chat.models

data class ChatInbox(
    val stories: List<ChatStory>,
    val messages: List<ChatItem>
)