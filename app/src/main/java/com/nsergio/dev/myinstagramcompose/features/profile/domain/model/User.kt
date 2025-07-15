package com.nsergio.dev.myinstagramcompose.features.profile.domain.model

import com.nsergio.dev.myinstagramcompose.features.feed.domain.model.PostWithMedia

/**
 * Identificador único y estable de un post.
 * Internamente es un String, pero el wrapper aporta seguridad de tipos
 * sin coste en tiempo de ejecución (inline value class).
 */
@JvmInline
value class UserId(val value: String)

data class User(
    val id: UserId,
    val name: String,
    val avatarUrl: String,
    val bio: String,
    val followers: Int,
    val following: Int,
    val posts: List<PostWithMedia> = listOf()
)