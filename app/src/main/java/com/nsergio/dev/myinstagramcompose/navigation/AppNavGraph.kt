package com.nsergio.dev.myinstagramcompose.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.nsergio.dev.myinstagramcompose.features.auth.login.ui.LoginScreen
import com.nsergio.dev.myinstagramcompose.features.feed.ui.FeedScreen

@Composable
fun AppNavGraph(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = AppDestination.Login.route
    ) {

        composable(AppDestination.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(AppDestination.Feed.route) {
                        popUpTo(AppDestination.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(AppDestination.Feed.route) {
            FeedScreen()
        }
    }
}