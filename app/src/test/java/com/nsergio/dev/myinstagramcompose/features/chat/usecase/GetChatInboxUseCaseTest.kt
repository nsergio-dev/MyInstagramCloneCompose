package com.nsergio.dev.myinstagramcompose.features.chat.usecase

import com.nsergio.dev.myinstagramcompose.TestUtils
import com.nsergio.dev.myinstagramcompose.features.BaseTest
import com.nsergio.dev.myinstagramcompose.features.chat.domain.ChatRepository
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.test.runTest
import org.junit.Test


class GetChatInboxUseCaseTest : BaseTest() {

    private lateinit var getChatInboxUseCase: GetChatInboxUseCase

    @RelaxedMockK
    lateinit var repository: ChatRepository

    override fun onStart() {
        super.onStart()
        getChatInboxUseCase = GetChatInboxUseCase(repository)
    }

    @Test
    fun `Validate invoke calling function`() = runTest {

        val chatStory = TestUtils.chatStory
        val chatItem = TestUtils.chatItem

        val listChat = listOf(chatStory)
        val listItem = listOf(chatItem)

        coEvery { repository.getMessages() } returns listItem

        coEvery { repository.getStories() } returns listChat

        val result = getChatInboxUseCase.invoke()

        assert(result.stories.isNotEmpty())
        assert(result.messages.isNotEmpty())
    }
    
}