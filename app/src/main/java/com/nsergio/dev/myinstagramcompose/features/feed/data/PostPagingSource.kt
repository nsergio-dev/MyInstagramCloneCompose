package com.nsergio.dev.myinstagramcompose.features.feed.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.nsergio.dev.myinstagramcompose.features.common.createUserWithMedia
import com.nsergio.dev.myinstagramcompose.features.common.fakeUsers
import com.nsergio.dev.myinstagramcompose.features.feed.domain.model.Media
import com.nsergio.dev.myinstagramcompose.features.feed.domain.model.PostId
import com.nsergio.dev.myinstagramcompose.features.feed.domain.model.PostWithMedia
import com.nsergio.dev.myinstagramcompose.features.profile.domain.model.User
import com.nsergio.dev.myinstagramcompose.features.profile.domain.model.UserId
import kotlin.random.Random

/**
 * Fake paging source that generates dummy posts on the fly.
 */
class PostPagingSource() : PagingSource<Int, PostWithMedia>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PostWithMedia> {

        val page = params.key ?: 0
        val size = params.loadSize
        val posts = getMockPosts(size, page)

        val resultPosts = loadedResult(posts, page)

        return resultPosts
    }

    private fun loadedResult(
        posts: List<PostWithMedia>,
        page: Int
    ): LoadResult.Page<Int, PostWithMedia> = LoadResult.Page(
        data = posts,
        prevKey = if (page == 0) null else page - 1,
        nextKey = page + 1
    )

    private fun getMockPosts(
        size: Int,
        page: Int
    ): List<PostWithMedia> {

        for (index in 0..size) {
            val userWithPost = createUserWithMedia(index, page)

            if (!fakeUsers.contains(userWithPost)) {
                fakeUsers.add(userWithPost)
            }
            userWithPost.posts.first()
        }

        val firstUserPostLis = fakeUsers.map { user -> user.posts.first() }
        return firstUserPostLis

    }

    override fun getRefreshKey(state: PagingState<Int, PostWithMedia>): Int? = 0

    /*

    override fun getRefreshKey(state: PagingState<Int, Post>): Int? =
        state.anchorPosition?.let { pos ->
            state.closestPageToPosition(pos)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(pos)?.nextKey?.minus(1)
        }*/

    private companion object {
        const val MILLIS_IN_MIN = 60_000L
        const val MILLIS_IN_HOUR = 60 * MILLIS_IN_MIN        // 3_600_000
        const val MILLIS_IN_DAY = 24 * MILLIS_IN_HOUR
    }
}