package com.nsergio.dev.myinstagramcompose.navigation

import android.net.Uri.encode

/**
 * All top-level routes in the app.
 */
sealed class AppDestination(val route: String) {

    /** Login screen (start). */
    object Login : AppDestination("login")

    /** Feed after successful login. */
    object MainPager  : AppDestination("main_pager")

    object Profile : AppDestination("profile/{userId}") {
        fun createRoute(userId: String) = "profile/$userId"
    }

    object Explore : AppDestination("explore")

    object ExploreViewer : AppDestination("explore/viewer/{url}") {
        fun createRoute(imageUrl: String): String {
            return "explore/viewer/${encode(imageUrl)}"
        }
    }

    object PhotoViewer : AppDestination("photo/{postId}/{index}") {
        fun createRoute(postId: String, index: Int) = "photo/$postId/$index"
    }

}