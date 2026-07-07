package com.nsergio.dev.myinstagramcompose.features.reels.presentation

import app.cash.turbine.test
import com.nsergio.dev.myinstagramcompose.BaseViewModelTest
import com.nsergio.dev.myinstagramcompose.TestUtils
import com.nsergio.dev.myinstagramcompose.features.common.fakeUsers
import kotlinx.coroutines.test.runTest
import org.junit.Test


class ReelsViewModelTest : BaseViewModelTest<ReelsViewModel>() {

    override fun initViewModel(): ReelsViewModel {
        return ReelsViewModel()
    }

    override fun onStart() {
        super.onStart()
        fakeUsers.clear()
    }

    override fun onFinish() {
        super.onFinish()
        fakeUsers.clear()
    }

    @Test
    fun `Validate reels empty value`() = runTest {
        viewModel.reels.test {
            val reels = awaitItem()
            assert(reels.isEmpty())
        }
    }

    @Test
    fun `Validate reels not empty value`() = runTest {
        val post = TestUtils.postWithMedia
        val user = TestUtils.user.copy(posts = listOf(post))

        fakeUsers.add(user)
        val newViewModel = initViewModel()

        newViewModel.reels.test {
            val reels = awaitItem()
            assert(reels.isNotEmpty())
        }
    }

    @Test
    fun `Valida onLikeToggle`() = runTest {
        val post = TestUtils.postWithMedia.copy(likedByMe = true)
        val user = TestUtils.user.copy(posts = listOf(post))
        fakeUsers.add(user)
        val newViewModel = initViewModel()
        newViewModel.onLikeToggle(post.id)
        newViewModel.reels.test {
            val reelsUpdated = awaitItem()

            assert(reelsUpdated.isNotEmpty())
        }
    }

}