package com.nsergio.dev.myinstagramcompose.features.profile.presentation

import app.cash.turbine.test
import com.nsergio.dev.myinstagramcompose.BaseViewModelTest
import com.nsergio.dev.myinstagramcompose.TestUtils
import com.nsergio.dev.myinstagramcompose.features.feed.data.PostRepository
import com.nsergio.dev.myinstagramcompose.features.profile.data.FakeUserRepository
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test


class ProfileViewModelTest : BaseViewModelTest<ProfileViewModel>() {

    @RelaxedMockK
    lateinit var userRepo: FakeUserRepository

    @RelaxedMockK
    lateinit var postRepository: PostRepository

    override fun initViewModel(): ProfileViewModel {
        return ProfileViewModel(userRepo, postRepository)
    }

    @Test
    fun `Validate posts empty value`() = runTest {
        every {
            postRepository.postsOf(any())
        } returns emptyList()

        viewModel.setUserId("123")

        viewModel.posts.test {
            assert(awaitItem().isEmpty())
        }

    }

    @Test
    fun `Validate posts not empty value`() = runTest {
        val post = TestUtils.postWithMedia
        val postsList = listOf(post)
        every {
            postRepository.postsOf(any())
        } returns postsList

        viewModel.setUserId("123")

        viewModel.posts.test {
            val postFromRepository = awaitItem()
            assert(postFromRepository.isNotEmpty())
        }

    }

    @Test
    fun `Validate user null value`() = runTest {

        viewModel.setUserId("")

        viewModel.user.test {
            val user = awaitItem()
            assertNull(user)
        }

    }

    @Test
    fun `Validate user not empty value`() = runTest {

        val user = TestUtils.user
        every { userRepo.getUser(any()) } returns user

        viewModel.setUserId("3")

        viewModel.user.test {
            val userFromRepository = awaitItem()
            assertEquals(user, userFromRepository)
        }

    }

    @Test
    fun `Validate user null value with userId me`() = runTest {

        every { userRepo.getUser(any()) } returns null

        viewModel.setUserId("me")

        viewModel.user.test {
            val userFromRepository = awaitItem()
            assertNull(userFromRepository)
        }

    }

    @Test
    fun `Validate user value with userId me`() = runTest {

        val user = TestUtils.user
        every { userRepo.getUser(any()) } returns user

        viewModel.setUserId("me")

        viewModel.user.test {
            val userFromRepository = awaitItem()
            assertEquals(user, userFromRepository)
        }

    }

}