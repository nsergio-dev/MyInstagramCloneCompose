package com.nsergio.dev.myinstagramcompose.navigation

/**
 * All top-level routes in the app.
 */
sealed class AppDestination(val route: String) {

    /** Login screen (start). */
    object Login : AppDestination("login")

    /** Feed after successful login. */
    object Feed  : AppDestination("feed")

    object Profile : AppDestination("profile/{userId}") {
        fun createRoute(userId: String) = "profile/$userId"
    }

    object PhotoViewer : AppDestination("photo/{postId}/{index}") {
        fun createRoute(postId: String, index: Int) = "photo/$postId/$index"
    }

}