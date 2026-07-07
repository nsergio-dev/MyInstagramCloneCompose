package com.nsergio.dev.myinstagramcompose

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nsergio.dev.myinstagramcompose.features.BaseTest
import io.mockk.MockKAnnotations
import io.mockk.unmockkAll
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule

@OptIn(ExperimentalCoroutinesApi::class)
abstract class BaseViewModelTest<ViewModel: Any>: BaseTest() {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    lateinit var viewModel: ViewModel

    abstract fun initViewModel(): ViewModel

    open fun setDispatcher(): CoroutineDispatcher = UnconfinedTestDispatcher()

    @Before
    override fun onStart() {
        super.onStart()
        Dispatchers.setMain(setDispatcher())
        viewModel = initViewModel()
    }

    @After
    override fun onFinish() {
        super.onFinish()
        Dispatchers.resetMain()
    }

}