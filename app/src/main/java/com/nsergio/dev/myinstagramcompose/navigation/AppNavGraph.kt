package com.nsergio.dev.myinstagramcompose.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.nsergio.dev.myinstagramcompose.core.ui.components.MainPagerScreen
import com.nsergio.dev.myinstagramcompose.features.auth.login.ui.LoginScreen
import com.nsergio.dev.myinstagramcompose.features.chat.ui.ChatDetailScreen
import com.nsergio.dev.myinstagramcompose.features.create_post.ui.CreatePostScreen
import com.nsergio.dev.myinstagramcompose.features.explore.presentation.ExploreRoute
import com.nsergio.dev.myinstagramcompose.features.photo_preview.ui.PhotoViewerImageScreen
import com.nsergio.dev.myinstagramcompose.features.photo_preview.ui.PhotoViewerScreen
import com.nsergio.dev.myinstagramcompose.features.profile.ui.ProfileScreen
import com.nsergio.dev.myinstagramcompose.features.reels.ui.ReelsScreen

@Composable
fun AppNavGraph(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = AppDestination.Login
    ) {

        composable<AppDestination.Login> {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(AppDestination.MainPager) {
                        popUpTo(AppDestination.Login) { inclusive = true }
                    }
                }
            )
        }

        composable<AppDestination.Explore> { backStackEntry ->
            ExploreRoute(
                onBackClick = { navController.popBackStack() },
                onPhotoClick = { url ->
                    navController.navigate(
                        route = AppDestination.ExploreViewer(url)
                    )
                }
            )
        }

        composable<AppDestination.MainPager> {
            MainPagerScreen(
                onExploreClick = {
                    navController.navigate(AppDestination.Explore)
                },
                onClickProfile = {
                    navController.navigate(
                        route = AppDestination.Profile(userId = it)
                    )
                },
                onClickStory = { postId ->
                    navController.navigate(
                        route = AppDestination.PhotoViewer(postId, 0)
                    )
                },
                onCreatePostClick = {
                    navController.navigate(AppDestination.CreatePost)
                },
                onOpenConversation = { userId ->
                    navController.navigate(
                        route = AppDestination.ChatDetail(userId)
                    )
                },
                onReelsClick = {
                    navController.navigate(AppDestination.Reels)
                }
            )
        }

        composable<AppDestination.Profile> { backStackEntry ->
            val objectDetail: AppDestination.Profile = backStackEntry
                .toRoute<AppDestination.Profile>()

            val userId = objectDetail.userId
            ProfileScreen(
                userId = userId,
                onClickImageDetail = { postId, index ->
                    navController.navigate(
                        route = AppDestination.PhotoViewer(postId, index)
                    )
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable<AppDestination.ChatDetail> { backStackEntry ->

            val screen: AppDestination.ChatDetail = backStackEntry
                .toRoute<AppDestination.ChatDetail>()

            val userId = screen.userId

            ChatDetailScreen(
                userId = userId,
                onBack = { navController.popBackStack() },
                onUserProfile = {
                    navController.navigate(
                        route = AppDestination.Profile(userId = userId)
                    )
                }
            )
        }

        composable<AppDestination.PhotoViewer> { backStackEntry ->

            val screen: AppDestination.PhotoViewer = backStackEntry
                .toRoute<AppDestination.PhotoViewer>()

            val postId = screen.postId
            val index = screen.index

            PhotoViewerScreen(
                postId = postId,
                index = index,
                onClose = { navController.popBackStack() }
            )
        }

        composable<AppDestination.ExploreViewer> { backStackEntry ->

            val screen: AppDestination.ExploreViewer = backStackEntry
                .toRoute<AppDestination.ExploreViewer>()

            val url = screen.imageUrl
            PhotoViewerImageScreen(
                imageUrl = url,
                onBack = { navController.popBackStack() }
            )
        }

        composable<AppDestination.CreatePost> {
            CreatePostScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable<AppDestination.Reels> {
            ReelsScreen(
                onBack = { navController.popBackStack() }
            )
        }

    }
}