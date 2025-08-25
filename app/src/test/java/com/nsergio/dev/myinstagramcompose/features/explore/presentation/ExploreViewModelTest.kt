package com.nsergio.dev.myinstagramcompose.features.explore.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.nsergio.dev.myinstagramcompose.BaseViewModelTest
import com.nsergio.dev.myinstagramcompose.features.explore.domain.model.ExplorePhoto
import com.nsergio.dev.myinstagramcompose.features.explore.domain.repository.ExploreRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ExploreViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val dispatcher = StandardTestDispatcher()

    @RelaxedMockK
    private lateinit var exploreRepository: ExploreRepository

    @Before
    fun onStart() {
        Dispatchers.setMain(dispatcher)
        MockKAnnotations.init(this)
    }

    @After
    fun onFinish() {
        unmockkAll()
    }

    @Test
    fun `Validate photos value`() = runTest(dispatcher) {
        val photo = ExplorePhoto(
            id = "id",
            url = "url",
            width = 1,
            height = 1
        )
        val expected = listOf(photo)
        every { exploreRepository.getPhotosFlow() } returns flowOf(expected)

        // SUT
        val viewModel = ExploreViewModel(exploreRepository)

        viewModel.photos.test {

            // 1) initialValue de stateIn -> emptyList()
            val emptyValue = awaitItem()
            assertEquals(emptyList<ExplorePhoto>(), emptyValue)

            // 2) emisión del repo -> expected
            val photosFromRepo = awaitItem()
            assertEquals(expected, photosFromRepo)

            cancelAndIgnoreRemainingEvents()
        }

    }

}