package com.nsergio.dev.myinstagramcompose.features.feed.presentation

import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import app.cash.turbine.test
import com.nsergio.dev.myinstagramcompose.MainDispatcherRule
import com.nsergio.dev.myinstagramcompose.TestUtils
import com.nsergio.dev.myinstagramcompose.core.ui.components.StoryItem
import com.nsergio.dev.myinstagramcompose.core.ui.components.StoryRing
import com.nsergio.dev.myinstagramcompose.features.feed.data.LocalFeedOverlay
import com.nsergio.dev.myinstagramcompose.features.feed.data.StoriesRepository
import com.nsergio.dev.myinstagramcompose.features.feed.domain.model.PostId
import com.nsergio.dev.myinstagramcompose.features.feed.domain.usecase.GetPostsUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FeedViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: FeedViewModel
    private lateinit var getPosts: GetPostsUseCase
    private lateinit var storiesRepository: StoriesRepository
    private lateinit var overlay: LocalFeedOverlay

    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        getPosts = mockk()
        storiesRepository = mockk(relaxed = true)
        overlay = mockk()

        every { getPosts.invoke() } returns flowOf(PagingData.empty())
        every { storiesRepository.getStoriesFlow() } returns MutableStateFlow(emptyList())
        every { overlay.headPosts } returns MutableStateFlow(emptyList())

        viewModel = FeedViewModel(getPosts, storiesRepository, overlay)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onLikeToggle adds and removes postId from likedIds`() = testScope.runTest {
        val postId = "123"

        viewModel.likedIds.test {
            assertEquals(emptySet<String>(), awaitItem())

            viewModel.onLikeToggle(postId)
            assertEquals(setOf(postId), awaitItem())

            viewModel.onLikeToggle(postId)
            assertEquals(emptySet<String>(), awaitItem())
        }
    }

    @Test
    fun `posts reflect likedIds and overlay headers`() = testScope.runTest {
        val likedId = "123"
        val post1 = TestUtils.postWithMedia.copy(id = PostId("1"))
        val post2 = TestUtils.postWithMedia.copy(id = PostId(likedId))
        val header = TestUtils.postWithMedia.copy(id = PostId("999"))

        val paging = flowOf(PagingData.from(listOf(post1, post2)))
        val overlayPosts = MutableStateFlow(listOf(header))

        every { getPosts.invoke() } returns paging
        every { overlay.headPosts } returns overlayPosts

        viewModel = FeedViewModel(getPosts, storiesRepository, overlay)
        viewModel.onLikeToggle(likedId)

        advanceUntilIdle()
        val items = viewModel.posts.value.collectItemsSync()

        assertEquals(header.id, items.first().id)

        val likedPost = items.find { it.id.value == likedId }
        assertEquals(true, likedPost?.likedByMe)
    }

    @Test
    fun `stories refleja historias vistas y se actualiza con updateStoryState`() = testScope.runTest {
        val idHistory = "hist1"
        val storyItem = TestUtils.storyItem
        val storiesFlow = MutableStateFlow(listOf(storyItem))

        every { storiesRepository.getStoriesFlow() } returns storiesFlow

        viewModel = FeedViewModel(getPosts, storiesRepository, overlay)

        val callback = mockk<(StoryItem) -> Unit>(relaxed = true)
        viewModel.updateStoryState(storyItem, callback)

        viewModel.stories.test {
            val result = awaitItem()
            val updated = result.find { it.idHistory.value == idHistory }
            assert(updated == null)
            verify { callback.invoke(storyItem) }
            cancelAndIgnoreRemainingEvents()
        }
    }

    // Helper para colectar PagingData usando AsyncPagingDataDiffer
    private fun <T : Any> PagingData<T>.collectItemsSync(): List<T> {
        val differ = AsyncPagingDataDiffer(
            diffCallback = object : DiffUtil.ItemCallback<T>() {
                override fun areItemsTheSame(oldItem: T, newItem: T) = oldItem == newItem
                override fun areContentsTheSame(oldItem: T, newItem: T) = oldItem == newItem
            },
            updateCallback = object : ListUpdateCallback {
                override fun onInserted(position: Int, count: Int) {}
                override fun onRemoved(position: Int, count: Int) {}
                override fun onMoved(fromPosition: Int, toPosition: Int) {}
                override fun onChanged(position: Int, count: Int, payload: Any?) {}
            },
            mainDispatcher = Dispatchers.Main,
            workerDispatcher = Dispatchers.IO
        )

        val latch = CompletableDeferred<Unit>()
        CoroutineScope(Dispatchers.Main).launch {
            differ.submitData(this@collectItemsSync)
            latch.complete(Unit)
        }
        runBlocking { latch.await() }
        return differ.snapshot().items
    }
}