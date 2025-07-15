package com.nsergio.dev.myinstagramcompose.features.feed.domain.model

/**
 * Identificador único y estable de un post.
 * Internamente es un String, pero el wrapper aporta seguridad de tipos
 * sin coste en tiempo de ejecución (inline value class).
 */
@JvmInline
value class PostId(val value: String)