package com.nsergio.dev.myinstagramcompose.features.feed.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import coil.compose.AsyncImage
import com.nsergio.dev.myinstagramcompose.core.ui.DimensDP
import com.nsergio.dev.myinstagramcompose.features.feed.domain.model.Post
import kotlin.random.Random

@Composable
fun FeedScreen() {

    val posts = getMockPosts(100)
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Welcome to the feed!",
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.TopCenter)
        )
        Posts(posts)
    }
}

@Composable
private fun Posts(
    posts: List<Post>
) {
    LazyColumn {
        items(posts.size) { index ->
            posts[index]?.let { ImagePost(it) }
        }
    }
}

@Composable
private fun ImagePost(post: Post) {
    AsyncImage(
        model = post.imageUrl,
        contentDescription = null,
        modifier = Modifier.fillMaxWidth(),
        contentScale = ContentScale.Crop
    )
    Row(modifier = Modifier.padding(DimensDP.DP12.dp)) {

        AsyncImage(
            model = post.imageUrl,
            contentDescription = null,
            modifier = Modifier
                .size(DimensDP.DP56.dp)
                .clip(CircleShape)
        )

        Text(
            text = post.authorName,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .padding(start = DimensDP.DP12.dp)
                .weight(1f)
        )
    }
}

private fun getMockPosts(
    size: Int
): List<Post> = List(size) { index ->
    val id = "${size * size + index}"
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
private val MILLIS_IN_MIN  = 60_000L
private val MILLIS_IN_HOUR = 60 * MILLIS_IN_MIN        // 3_600_000
private val MILLIS_IN_DAY  = 24 * MILLIS_IN_HOUR
