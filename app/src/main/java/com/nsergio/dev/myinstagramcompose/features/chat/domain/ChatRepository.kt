package com.nsergio.dev.myinstagramcompose.features.chat.domain

import com.nsergio.dev.myinstagramcompose.features.chat.models.ChatItem
import com.nsergio.dev.myinstagramcompose.features.chat.models.ChatStory

interface ChatRepository {

    suspend fun getStories(): List<ChatStory>

    suspend fun getMessages(): List<ChatItem>

}