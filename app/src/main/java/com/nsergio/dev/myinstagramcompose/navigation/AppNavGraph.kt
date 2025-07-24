package com.nsergio.dev.myinstagramcompose.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.nsergio.dev.myinstagramcompose.core.ui.components.MainPagerScreen
import com.nsergio.dev.myinstagramcompose.features.auth.login.ui.LoginScreen
import com.nsergio.dev.myinstagramcompose.features.photo_preview.ui.PhotoViewerScreen
import com.nsergio.dev.myinstagramcompose.features.profile.ui.ProfileScreen

@Composable
fun AppNavGraph(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = AppDestination.Login.route
    ) {

        composable(AppDestination.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(AppDestination.MainPager.route) {
                        popUpTo(AppDestination.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(AppDestination.MainPager.route) {
            MainPagerScreen (
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
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: "me"
            ProfileScreen(
                userId = userId,
                onClickImageDetail = { postId, index ->
                    navController.navigate(
                        route = AppDestination.PhotoViewer.createRoute(postId, index)
                    )
                }
            )
        }

        composable(
            route = AppDestination.PhotoViewer.route,
            arguments = listOf(
                navArgument("postId") { type = NavType.StringType },
                navArgument("index") { type = NavType.IntType }
            )
        ) { entry ->
            val postId = entry.arguments?.getString("postId").orEmpty()
            val index = entry.arguments?.getInt("index") ?: 0
            PhotoViewerScreen(
                postId = postId,
                index = index,
                onClose = { navController.popBackStack() }
            )
        }
    }
}