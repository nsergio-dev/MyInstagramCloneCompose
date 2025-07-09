package com.nsergio.dev.myinstagramcompose.features.profile.domain.model

data class User(
    val id: String,
    val name: String,
    val avatarUrl: String,
    val bio: String,
    val posts: Int,
    val followers: Int,
    val following: Int
)