package com.nsergio.dev.myinstagramcompose.features.chat.usecase

import com.nsergio.dev.myinstagramcompose.features.chat.domain.ChatRepository
import com.nsergio.dev.myinstagramcompose.features.chat.models.ChatInbox
import javax.inject.Inject

class GetChatInboxUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(): ChatInbox {

        val stories = repository.getStories()

        val messages = repository.getMessages()

        return ChatInbox(stories = stories, messages = messages)
    }
}