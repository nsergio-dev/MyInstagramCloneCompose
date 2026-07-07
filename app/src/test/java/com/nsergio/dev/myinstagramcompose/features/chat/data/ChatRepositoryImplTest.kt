package com.nsergio.dev.myinstagramcompose.features.chat.data

import com.nsergio.dev.myinstagramcompose.TestUtils
import com.nsergio.dev.myinstagramcompose.core.utils.ImageUrlHelper
import com.nsergio.dev.myinstagramcompose.features.BaseTest
import com.nsergio.dev.myinstagramcompose.features.common.FakeNameUser
import com.nsergio.dev.myinstagramcompose.features.common.NameUser
import com.nsergio.dev.myinstagramcompose.features.profile.data.FakeUserRepository
import com.nsergio.dev.myinstagramcompose.features.profile.domain.model.User
import com.nsergio.dev.myinstagramcompose.features.profile.domain.model.UserId
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.unmockkObject
import io.mockk.unmockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Test
import kotlin.random.Random
import kotlin.random.nextInt

@OptIn(ExperimentalCoroutinesApi::class)
class ChatRepositoryImplTest : BaseTest() {

    @RelaxedMockK
    lateinit var userRepository: FakeUserRepository

    @RelaxedMockK
    lateinit var imageUrlHelper: ImageUrlHelper

    private lateinit var repositoryImpl: ChatRepositoryImpl

    override fun onStart() {
        super.onStart()
        Dispatchers.setMain(Dispatchers.Unconfined)
        mockkObject(FakeNameUser)
        repositoryImpl = ChatRepositoryImpl(
            userRepository = userRepository,
            imageUrlHelper = imageUrlHelper
        )
    }

    override fun onFinish() {
        super.onFinish()
        Dispatchers.resetMain()
        unmockkObject(FakeNameUser)
    }

    @Test
    fun `Validate getStories returns not empty data`() = runTest {

        val nameExpected = NameUser(nickName = "test", realName = "test")

        every {
            FakeNameUser.getFullName(any<Boolean>())
        } returns nameExpected

        val result = repositoryImpl.getStories()

        assert(result.isNotEmpty())

    }

    @Test
    fun `Validate getStories returns empty data`() = runTest {

        val nameExpected = NameUser(nickName = "test", realName = "test")

        every {
            FakeNameUser.getFullName(any<Boolean>())
        } returns nameExpected

        //mockkStatic(Random::class)
        mockkStatic("kotlin.random.RandomKt")
        every { Random.nextInt(any<IntRange>()) } returns 0

        val result = repositoryImpl.getStories()

        assert(result.isEmpty())
        unmockkStatic("kotlin.random.RandomKt")
    }

    @Test
    fun `Validate getMessages returns empty data`() = runTest {

        mockkStatic("kotlin.random.RandomKt")
        every { Random.nextInt(any<IntRange>()) } returns 0
        val result = repositoryImpl.getMessages()

        assert(result.isEmpty())
        unmockkStatic("kotlin.random.RandomKt")
    }

    @Test
    fun `Validate getMessages returns not empty data`() = runTest {

        every {
            userRepository.getRandomUser(any<Int>())
        } returns listOf(TestUtils.user)

        val result = repositoryImpl.getMessages()

        assert(result.isNotEmpty())

    }

}