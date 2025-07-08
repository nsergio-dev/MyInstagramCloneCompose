package com.nsergio.dev.myinstagramcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import com.nsergio.dev.myinstagramcompose.core.ui.DimensDP
import com.nsergio.dev.myinstagramcompose.core.ui.components.BaseButton
import com.nsergio.dev.myinstagramcompose.core.ui.components.BaseTextField
import com.nsergio.dev.myinstagramcompose.core.ui.theme.MyInstagramTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyInstagramTheme {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = DimensDP.DP16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    BaseTextField(
                        value = "",
                        onValueChange = { },
                        hint = "Email"
                    )

                    BaseTextField(
                        value = "",
                        onValueChange = { },
                        hint = "Password",
                        keyboardType = KeyboardType.Password
                    )

                    BaseButton(
                        text = "Log in",
                        enabled = true,
                        onClick = {

                        }
                    )
                }
            }
        }
    }
}