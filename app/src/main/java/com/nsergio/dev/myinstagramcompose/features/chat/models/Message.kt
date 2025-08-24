package com.nsergio.dev.myinstagramcompose.features.chat.models

data class Message(
    val id: String,
    val text: String,
    val fromMe: Boolean
)