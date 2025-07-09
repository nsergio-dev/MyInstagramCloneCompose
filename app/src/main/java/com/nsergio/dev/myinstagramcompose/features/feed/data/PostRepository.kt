package com.nsergio.dev.myinstagramcompose.features.feed.data

import androidx.paging.PagingData
import com.nsergio.dev.myinstagramcompose.features.feed.domain.model.Post
import kotlinx.coroutines.flow.Flow

/**
 * Repository contract for feed posts.
 */
interface PostRepository {
    fun getPosts(): Flow<PagingData<Post>>
}