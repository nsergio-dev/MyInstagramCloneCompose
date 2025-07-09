package com.nsergio.dev.myinstagramcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.nsergio.dev.myinstagramcompose.core.ui.theme.MyInstagramTheme
import com.nsergio.dev.myinstagramcompose.navigation.AppNavGraph
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyInstagramTheme {
                MyInstagramTheme {
                    val navController = rememberNavController()
                    AppNavGraph(navController)
                }
            }
        }
    }
}