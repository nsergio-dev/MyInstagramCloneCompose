package com.nsergio.dev.myinstagramcompose.features.feed.domain.usecase

import androidx.paging.PagingData
import com.nsergio.dev.myinstagramcompose.features.feed.domain.model.PostWithMedia
import kotlinx.coroutines.flow.Flow

/**
 * Returns a paged stream of posts.
 */
fun interface GetPostsUseCase {
    operator fun invoke(): Flow<PagingData<PostWithMedia>>
}