package com.nsergio.dev.myinstagramcompose.features.feed.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.nsergio.dev.myinstagramcompose.features.feed.domain.model.Post
import kotlin.random.Random

/**
 * Fake paging source that generates dummy posts on the fly.
 */
class PostPagingSource : PagingSource<Int, Post>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Post> {

        val page = params.key ?: 0
        val size = params.loadSize
        val posts = getMockPosts(size, page)

        val resultPosts = loadedResult(posts, page)

        return resultPosts
    }

    private fun loadedResult(
        posts: List<Post>,
        page: Int
    ): LoadResult.Page<Int, Post> = LoadResult.Page(
        data = posts,
        prevKey = if (page == 0) null else page - 1,
        nextKey = page + 1
    )

    private fun getMockPosts(
        size: Int,
        page: Int
    ): List<Post> = List(size) { index ->
        val id = "${page * size + index}"
        val randomForCaption = Random.nextBoolean()
        val lorem = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque ut dignissim est. Proin ut nisi ut lacus volutpat mollis quis a odio. Donec et tellus feugiat, efficitur urna ultrices, mattis orci. Nunc augue felis, viverra eget sagittis vel, tristique at lacus."
        val caption = if (randomForCaption) " $lorem" else ""

        Post(
            id = id,
            authorName = "user_$id",
            authorAvatar = "https://i.pravatar.cc/150?u=$id",
            imageUrl = "https://picsum.photos/seed/$id/600/600",
            caption = "Awesome photo #$id$caption",
            likes = Random.nextInt(1, 1_000),
            comments = Random.nextInt(1, 1_000),
            shares = Random.nextInt(1, 1_000),     // ahora menos…
            Random.nextLong(MILLIS_IN_HOUR,        // ≥ 1 h
                7 * MILLIS_IN_DAY + 1)

        )
    }

    override fun getRefreshKey(state: PagingState<Int, Post>): Int? = 0

    companion object {
        private const val MILLIS_IN_MIN  = 60_000L
        private const val MILLIS_IN_HOUR = 60 * MILLIS_IN_MIN        // 3_600_000
        private const val MILLIS_IN_DAY  = 24 * MILLIS_IN_HOUR
    }
}