package com.nsergio.dev.myinstagramcompose.features.feed.domain.model

data class Post(
    val id: String,
    val authorName: String,
    val authorAvatar: String,
    val imageUrl: String,
    val caption: String,
    val likes: Int,
    val comments: Int,
    val shares: Int,
    val createdAt: Long,   // epoch time millis
    val liked: Boolean = false
)