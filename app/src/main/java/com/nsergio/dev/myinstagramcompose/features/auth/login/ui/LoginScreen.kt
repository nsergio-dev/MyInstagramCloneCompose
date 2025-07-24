package com.nsergio.dev.myinstagramcompose.features.auth.login.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import com.nsergio.dev.myinstagramcompose.core.ui.DimensDP
import com.nsergio.dev.myinstagramcompose.core.ui.components.BaseButton
import com.nsergio.dev.myinstagramcompose.core.ui.components.BaseTextField
import com.nsergio.dev.myinstagramcompose.features.auth.login.presentation.LoginViewModel

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onLoginSuccess: () -> Unit
) {

    val email     = viewModel.email.collectAsState()
    val password  = viewModel.password.collectAsState()
    val canLogin  = viewModel.isLoginButtonEnabled.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = DimensDP.DP16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        BaseTextField(
            value = email.value,
            onValueChange = viewModel::onEmailChange,
            hint = "Email"
        )

        BaseTextField(
            value = password.value,
            onValueChange = viewModel::onPasswordChange,
            hint = "Password",
            keyboardType = KeyboardType.Password
        )

        BaseButton(
            text = "Log in",
            enabled = canLogin.value
        ) {
            onLoginSuccess()
        }
    }
}