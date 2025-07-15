package com.nsergio.dev.myinstagramcompose.features.feed.data

import androidx.paging.PagingData
import com.nsergio.dev.myinstagramcompose.features.feed.domain.model.PostId
import com.nsergio.dev.myinstagramcompose.features.feed.domain.model.PostWithMedia
import com.nsergio.dev.myinstagramcompose.features.profile.domain.model.UserId
import kotlinx.coroutines.flow.Flow

/**
 * Repository contract for feed posts.
 */
interface PostRepository {

    fun getPosts(): Flow<PagingData<PostWithMedia>>

    fun findById(id: PostId): PostWithMedia?

    fun postsOf(authorId: UserId): List<PostWithMedia>

}