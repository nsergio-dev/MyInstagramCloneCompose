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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.nsergio.dev.myinstagramcompose.core.ui.DimensDP
import com.nsergio.dev.myinstagramcompose.core.ui.components.LikeButton
import com.nsergio.dev.myinstagramcompose.features.feed.domain.model.Post
import com.nsergio.dev.myinstagramcompose.features.feed.presentation.FeedViewModel

@Composable
fun FeedScreen(
    viewModel: FeedViewModel = hiltViewModel()
) {
    val posts: LazyPagingItems<Post> = viewModel.posts.collectAsLazyPagingItems()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Welcome to the feed!",
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.TopCenter)
        )
        Posts(posts, viewModel::onLikeToggle)
    }
}

@Composable
private fun Posts(
    posts: LazyPagingItems<Post>,
    onPostLiked: (String) -> Unit
) {
    LazyColumn {
        items(posts.itemCount) { index ->
            posts[index]?.let {
                ImagePost(it) {
                    onPostLiked.invoke(it.id)
                }
            }
        }
    }
}

@Composable
private fun ImagePost(post: Post, onPostLiked: () -> Unit) {
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
    LikeButton(liked = post.liked, onToggle = onPostLiked)
}