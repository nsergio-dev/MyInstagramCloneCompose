package com.nsergio.dev.myinstagramcompose.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.nsergio.dev.myinstagramcompose.features.auth.login.ui.LoginScreen
import com.nsergio.dev.myinstagramcompose.features.feed.ui.FeedScreen
import com.nsergio.dev.myinstagramcompose.features.profile.ui.ProfileScreen

@Composable
fun AppNavGraph(navController: NavHostController, contentPadding: PaddingValues) {

    NavHost(
        navController = navController,
        startDestination = AppDestination.Login.route
    ) {

        composable(AppDestination.Login.route) {
            LoginScreen(
                contentPadding = contentPadding,
                onLoginSuccess = {
                    navController.navigate(AppDestination.Feed.route) {
                        popUpTo(AppDestination.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(AppDestination.Feed.route) {
            FeedScreen(
                contentPadding = contentPadding,
                onClickProfile = {
                    navController.navigate(
                        route = AppDestination.Profile.createRoute(userId = it)
                    )
                }
            )
        }

        composable(
            route = AppDestination.Profile.route,
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) {backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: "me"
            ProfileScreen(
                userId = userId,
                contentPadding = contentPadding,
            )
        }
    }
}