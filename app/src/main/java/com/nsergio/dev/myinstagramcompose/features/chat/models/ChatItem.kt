package com.nsergio.dev.myinstagramcompose.features.chat.models

data class ChatItem(
    val userId: String,
    val username: String,
    val photoUrl: String,
    val isOnline: Boolean
)