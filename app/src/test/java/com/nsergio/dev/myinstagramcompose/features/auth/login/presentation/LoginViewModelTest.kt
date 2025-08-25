package com.nsergio.dev.myinstagramcompose.features.auth.login.presentation

import com.nsergio.dev.myinstagramcompose.BaseViewModelTest
import org.junit.Assert.assertEquals
import org.junit.Test

class LoginViewModelTest : BaseViewModelTest<LoginViewModel>() {

    override fun initViewModel(): LoginViewModel {
        return LoginViewModel()
    }

    @Test
    fun `Validate default email state`() {
        assert(viewModel.email.value.isEmpty())
    }

    @Test
    fun `Validate default password state`() {
        assert(viewModel.password.value.isEmpty())
    }

    @Test
    fun `Validate default login button state`() {
        assert(!viewModel.isLoginButtonEnabled.value)
    }

    @Test
    fun `Validate update email state`() {
        val expectedMessage = "test"
        viewModel.onEmailChange(expectedMessage)
        assertEquals(expectedMessage, viewModel.email.value)
    }

    @Test
    fun `Validate update password state`() {
        val expectedMessage = "test"
        viewModel.onPasswordChange(expectedMessage)
        assertEquals(expectedMessage, viewModel.password.value)
    }

    @Test
    fun `Validate update login button state`() {
        val expectedMessage = "test"
        viewModel.onEmailChange(expectedMessage)
        viewModel.onPasswordChange(expectedMessage)
        assert(viewModel.isLoginButtonEnabled.value)
    }

}

