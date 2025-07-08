package com.nsergio.dev.myinstagramcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.nsergio.dev.myinstagramcompose.core.ui.theme.MyInstagramTheme
import com.nsergio.dev.myinstagramcompose.features.auth.login.ui.LoginScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyInstagramTheme {
                LoginScreen {

                }
            }
        }
    }
}