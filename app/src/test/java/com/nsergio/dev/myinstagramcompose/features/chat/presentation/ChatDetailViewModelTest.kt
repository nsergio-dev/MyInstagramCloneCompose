package com.nsergio.dev.myinstagramcompose.features.chat.presentation

import com.nsergio.dev.myinstagramcompose.BaseViewModelTest
import com.nsergio.dev.myinstagramcompose.TestUtils
import com.nsergio.dev.myinstagramcompose.core.utils.Utils
import com.nsergio.dev.myinstagramcompose.features.chat.data.FakeMessageRepository
import com.nsergio.dev.myinstagramcompose.features.chat.models.Message
import com.nsergio.dev.myinstagramcompose.features.profile.data.FakeUserRepository
import com.nsergio.dev.myinstagramcompose.features.profile.domain.model.User
import com.nsergio.dev.myinstagramcompose.features.profile.domain.model.UserId
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.unmockkObject
import io.mockk.unmockkStatic
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.time.delay
import org.junit.Assert.assertNull
import org.junit.Test

class ChatDetailViewModelTest : BaseViewModelTest<ChatDetailViewModel>() {

    @RelaxedMockK
    lateinit var userRepository: FakeUserRepository

    @RelaxedMockK
    lateinit var messageRepository: FakeMessageRepository

    override fun initViewModel(): ChatDetailViewModel {
        return ChatDetailViewModel(
            userRepository = userRepository,
            messageRepository = messageRepository
        )
    }

    @Test
    fun `Validate default messages state`() {
        assert(viewModel.messages.value.isEmpty())
    }

    @Test
    fun `Validate default user state`() {
        assertNull(viewModel.user.value)
    }

    @Test
    fun `Validate calling loadConversation and messages-value is not empty`() {
        val messages = listOf(
            Message(
                id = "1",
                text = "tex",
                fromMe = false
            )
        )
        every { messageRepository.getRandomMessages() } returns messages

        viewModel.loadConversation()

        assert(viewModel.messages.value.isNotEmpty())
    }

    @Test
    fun `Validate calling loadConversation and messages-value is empty`() {

        every { messageRepository.getRandomMessages() } returns emptyList()

        viewModel.loadConversation()

        assert(viewModel.messages.value.isEmpty())
    }

    @Test
    fun `Validate calling sendMessage with empty text`() {

        viewModel.sendMessage("")
        assert(viewModel.messages.value.isEmpty())

    }

    @Test
    fun `Validate calling sendMessage with not-empty text`() = runTest {

        mockkObject(Utils)
        every { Utils.generateRandomString() } returnsMany listOf("abc", "bca")

        mockkStatic(::delay)
        coEvery { delay(any()) } returns Unit

        every { messageRepository.getRandomMessage() } returns "lorem ipsum"

        viewModel.sendMessage("abc")

        assert(viewModel.messages.value.isNotEmpty())

        unmockkObject(Utils)
        unmockkStatic(::delay)

    }

    @Test
    fun `Validate calling getUser returns not null data`() {

        val user = TestUtils.user
        every { userRepository.getUser(any<UserId>()) } returns user

        viewModel.getUser("Av")

        assert(viewModel.user.value != null)
        assert(viewModel.user.value == user)
    }

    @Test
    fun `Validate calling getUser returns null data`() {

        every { userRepository.getUser(any<UserId>()) } returns null

        viewModel.getUser("Av")

        assertNull(viewModel.user.value)
    }


}