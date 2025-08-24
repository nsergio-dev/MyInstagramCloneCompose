package com.nsergio.dev.myinstagramcompose.navigation

import kotlinx.serialization.Serializable

/**
 * All top-level routes in the app.
 */
sealed class AppDestination {

    //object cause not need arguments required
    @Serializable
    object Login

    @Serializable
    object MainPager

    @Serializable
    data class Profile(val userId: String)

    @Serializable
    object Explore

    @Serializable
    data class ExploreViewer(val imageUrl: String)

    @Serializable
    data class PhotoViewer(val postId: String, val index: Int)

    @Serializable
    object CreatePost

    @Serializable
    object Reels

}