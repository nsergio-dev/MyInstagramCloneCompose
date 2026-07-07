package com.nsergio.dev.myinstagramcompose.features.chat.presentation

import com.nsergio.dev.myinstagramcompose.BaseViewModelTest
import com.nsergio.dev.myinstagramcompose.TestUtils
import com.nsergio.dev.myinstagramcompose.features.chat.models.ChatInbox
import com.nsergio.dev.myinstagramcompose.features.chat.models.ChatItem
import com.nsergio.dev.myinstagramcompose.features.chat.models.ChatStory
import com.nsergio.dev.myinstagramcompose.features.chat.usecase.GetChatInboxUseCase
import com.nsergio.dev.myinstagramcompose.features.profile.data.FakeUserRepository
import com.nsergio.dev.myinstagramcompose.features.profile.domain.model.User
import com.nsergio.dev.myinstagramcompose.features.profile.domain.model.UserId
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ChatViewModelTest : BaseViewModelTest<ChatViewModel>() {

    @RelaxedMockK
    lateinit var getChatInboxUseCase: GetChatInboxUseCase

    @RelaxedMockK
    lateinit var userRepository: FakeUserRepository

    override fun initViewModel(): ChatViewModel {
        return ChatViewModel(
            getChatInboxUseCase = getChatInboxUseCase,
            userRepository = userRepository
        )
    }

    @Test
    fun `Validate default state value`() {
        val defaultValue = ChatInboxState()
        assertEquals(defaultValue, viewModel.state.value)
    }

    @Test
    fun `Validate default user value`() {
        assertNull(viewModel.user.value)
    }

    @Test
    fun `Validate default query value`() {
        assert(viewModel.query.value.isEmpty())
    }

    @Test
    fun `Validate default uiState value`() {
        val defaultValue = ChatInboxState()
        assertEquals(defaultValue, viewModel.state.value)
    }

    fun `Validate default uiState value by query`() = runTest {
        val defaultValue = ChatInboxState()
        viewModel.updateQuery("query")
        viewModel.uiState.collect {
            delay(400)
            advanceUntilIdle()
            assertEquals(defaultValue, it)
        }
    }

    fun `Validate calling load function`() = runTest {
        val chatStory = TestUtils.chatStory
        val chatItem = TestUtils.chatItem

        val chat = ChatInbox(
            stories = listOf(chatStory),
            messages = listOf(chatItem)
        )
        coEvery { getChatInboxUseCase.invoke() } returns chat

        viewModel.load()

        val colector: FlowCollector<Any> = mockk()
        coEvery { colector.emit(any()) } answers {
            val value = firstArg<Any>()
            assertEquals(value, viewModel.state.value)
            coEvery { colector.emit(any()) } returns Unit
        }
        assert(viewModel.state.value.stories.isNotEmpty())
    }

    @Test
    fun `Validate calling query function`() {
        viewModel.updateQuery("query")
        assert(viewModel.query.value == "query")

    }

    @Test
    fun `Validate calling getUser function`() {
        val user = TestUtils.user
        every {
            userRepository.getUser(any<UserId>())
        } returns user

        viewModel.getUser("Av")
        viewModel.getUser()

        assert(viewModel.user.value == user)

    }


}