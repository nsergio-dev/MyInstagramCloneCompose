package com.nsergio.dev.myinstagramcompose.features.feed.data

// package tu.paquete

import app.cash.turbine.test
import com.nsergio.dev.myinstagramcompose.TestUtils
import com.nsergio.dev.myinstagramcompose.features.BaseTest
import com.nsergio.dev.myinstagramcompose.features.feed.domain.model.PostId
import com.nsergio.dev.myinstagramcompose.features.profile.data.FakeUserRepository
import com.nsergio.dev.myinstagramcompose.features.profile.domain.model.User
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import net.bytebuddy.matcher.ElementMatchers.any
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import kotlin.random.Random
import kotlin.random.nextInt

@OptIn(ExperimentalCoroutinesApi::class)
class StoriesRepositoryImplTest : BaseTest() {

    @RelaxedMockK
    private val userRepository: FakeUserRepository = mockk()

    private lateinit var repository: StoriesRepositoryImpl

    override fun onStart() {
        super.onStart()
        MockKAnnotations.init(this)
        repository = StoriesRepositoryImpl(userRepository)
    }

    @Test
    fun `Validate getStoriesFlow returns not empty data`() = runTest {
        mockkStatic("kotlin.random.RandomKt")
        every { userRepository.getRandomUser(any<Int>()) } returns listOf(user())

        every { Random.nextInt(any<IntRange>()) } returns 12

        repository.getStoriesFlow().test {
            val result = awaitItem()
            assertTrue(result.isNotEmpty())
            cancelAndConsumeRemainingEvents()
        }

    }

    @Test
    fun `Validate getStoriesFlow returns empty data`() = runTest {
        mockkStatic("kotlin.random.RandomKt")
        every { userRepository.getRandomUser(any<Int>()) } returns emptyList()

        every { Random.nextInt(any<IntRange>()) } returns 0

        repository.getStoriesFlow().test {
            val result = awaitItem()
            assertTrue(result.isEmpty())
            cancelAndConsumeRemainingEvents()
        }

    }

    private fun user(): User {
        val posts = listOf(TestUtils.postWithMedia)
        val user = TestUtils.user.copy(posts = posts)
        return user
    }
}