package com.nsergio.dev.myinstagramcompose.features.feed.domain.model

/**
 * Elemento de media (por ejemplo, una foto) que forma parte de un Post.
 *
 * @param url   Dirección http/https que cargará Coil.
 * @param type  Tipo de media (IMAGE, VIDEO…).
 * @param alt   Texto alternativo para accesibilidad.
 */
data class Media(
    val url: String,
    val type: MediaType = MediaType.IMAGE,
    val alt: String = ""
)