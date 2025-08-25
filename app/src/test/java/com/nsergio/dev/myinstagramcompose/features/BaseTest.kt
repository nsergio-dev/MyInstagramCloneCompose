package com.nsergio.dev.myinstagramcompose.features

import io.mockk.MockKAnnotations
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Before

abstract class BaseTest {

    @Before
    open fun onStart() {
        MockKAnnotations.init(this)
    }

    @After
    open fun onFinish() {
        unmockkAll()
    }

}