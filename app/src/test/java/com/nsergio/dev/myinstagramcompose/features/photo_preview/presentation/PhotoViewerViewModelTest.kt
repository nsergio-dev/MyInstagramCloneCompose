package com.nsergio.dev.myinstagramcompose.features.photo_preview.presentation

import app.cash.turbine.test
import com.nsergio.dev.myinstagramcompose.BaseViewModelTest
import com.nsergio.dev.myinstagramcompose.TestUtils
import com.nsergio.dev.myinstagramcompose.features.feed.data.PostRepository
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test

class PhotoViewerViewModelTest : BaseViewModelTest<PhotoViewerViewModel>() {

    @RelaxedMockK
    lateinit var repository: PostRepository

    override fun initViewModel(): PhotoViewerViewModel {
        return PhotoViewerViewModel(repository)
    }

    @Test
    fun `Validate initial _params`() = runTest {
        viewModel.startIndex.test {
            assertEquals(0,awaitItem())
        }
    }

    @Test
    fun `Validate initial _params n number`() = runTest {
        val newViewModel = initViewModel()
        newViewModel.setParams("123", 1)
        newViewModel.startIndex.test {
            assertEquals(1,awaitItem())
        }
    }

    @Test
    fun `Validate post null`() = runTest {
        val newViewModel = initViewModel()
        every { repository.findById(any()) } returns null
        newViewModel.setParams("123", 1)
        newViewModel.post.test {
            assertEquals(null,awaitItem())
        }
    }

    @Test
    fun `Validate post not null`() = runTest {
        val postMedia = TestUtils.postWithMedia
        val newViewModel = initViewModel()

        every { repository.findById(any()) } returns postMedia

        newViewModel.setParams("123", 1)
        newViewModel.post.test {
            assertEquals(postMedia,awaitItem())
        }
    }

}