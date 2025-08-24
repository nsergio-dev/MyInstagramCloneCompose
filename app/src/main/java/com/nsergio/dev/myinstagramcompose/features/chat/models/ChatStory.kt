package com.nsergio.dev.myinstagramcompose.features.chat.models

data class ChatStory(
    val userId: String,
    val username: String,
    val photoUrl: String,
    val caption: String,
    val isOnline: Boolean
)