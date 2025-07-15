package com.nsergio.dev.myinstagramcompose.features.feed.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.nsergio.dev.myinstagramcompose.features.feed.domain.model.PostWithMedia
import com.nsergio.dev.myinstagramcompose.features.feed.presentation.FeedViewModel

@Composable
fun FeedScreen(
    contentPadding: PaddingValues,
    viewModel: FeedViewModel = hiltViewModel(),
    onClickProfile: (String) -> Unit
) {
    val posts: LazyPagingItems<PostWithMedia> = viewModel.posts.collectAsLazyPagingItems()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues = contentPadding),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Welcome to the feed!",
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.TopCenter)
        )
        Posts(
            posts = posts,
            onLikeToggle = viewModel::onLikeToggle,
            onProfileClick = onClickProfile
        )
    }
}

@Composable
private fun Posts(
    posts: LazyPagingItems<PostWithMedia>,
    onLikeToggle: (String) -> Unit,
    onProfileClick: (String) -> Unit
) {
    LazyColumn {
        items(posts.itemCount) { index ->
            posts[index]?.let {
                PostCard(
                    post = it,
                    onLikeToggle = onLikeToggle,
                    onProfileClick = {
                        onProfileClick.invoke(it.authorId.value)
                    }
                )
            }
        }
    }
}