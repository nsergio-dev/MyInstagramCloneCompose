package com.nsergio.dev.myinstagramcompose.features.feed.domain.model

import com.nsergio.dev.myinstagramcompose.features.profile.domain.model.UserId

/**
 * Modelo principal de un post del feed o del perfil.
 *
 * @param id            Identificador único del post.
 * @param authorId      Id del autor (String por simplicidad).
 * @param authorName    Nombre visible del autor.
 * @param authorAvatarUrl URL del avatar del autor.
 * @param media         Lista de elementos multimedia (fotos/vídeos).
 * @param caption       Texto descriptivo que acompaña al post.
 * @param likeCount     Nº total de “me gusta”.
 * @param commentCount  Nº total de comentarios.
 * @param shareCount    Nº total de veces compartido.
 * @param createdAt     Epoch millis de creación.
 * @param likedByMe     Si el usuario actual lo ha marcado con “me gusta”.
 */
data class PostWithMedia(
    val id: PostId,
    val authorId: UserId,
    val authorName: String,
    val authorAvatarUrl: String,
    val media: List<Media>,
    val caption: String = "",
    val likeCount: Int = 0,
    val commentCount: Int = 0,
    val shareCount: Int = 0,
    val createdAt: Long,
    val likedByMe: Boolean = false
)