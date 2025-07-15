package com.nsergio.dev.myinstagramcompose.features.feed.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.nsergio.dev.myinstagramcompose.features.common.fakeUsers
import com.nsergio.dev.myinstagramcompose.features.feed.domain.model.PostId
import com.nsergio.dev.myinstagramcompose.features.feed.domain.model.PostWithMedia
import com.nsergio.dev.myinstagramcompose.features.profile.domain.model.UserId
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class FakePostRepository @Inject constructor() : PostRepository {

    override fun getPosts(): Flow<PagingData<PostWithMedia>> =
        Pager(PagingConfig(pageSize = 10)) {
            PostPagingSource()
        }.flow

    override fun findById(id: PostId): PostWithMedia? {

        val postsByUser = fakeUsers.map { user -> user.posts }
        val posts = postsByUser
            .flatten()
            .find { it.id == id }

        return posts
    }

    override fun postsOf(authorId: UserId): List<PostWithMedia> =
        fakeUsers.find { it.id == authorId }?.posts ?: emptyList()
}