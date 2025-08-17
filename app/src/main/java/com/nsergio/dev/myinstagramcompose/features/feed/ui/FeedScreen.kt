package com.nsergio.dev.myinstagramcompose.features.feed.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.nsergio.dev.myinstagramcompose.core.ui.components.StoriesRow
import com.nsergio.dev.myinstagramcompose.core.ui.components.StoryItem
import com.nsergio.dev.myinstagramcompose.core.ui.components.StoryRing
import com.nsergio.dev.myinstagramcompose.features.common.fakeHistoriesItemsFeed
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
        item {
            StoriesRow(
                currentUser = StoryItem(
                    id = "me",
                    username = "Tu nombre",
                    avatarUrl = "https://randomuser.me/api/portraits/men/1.jpg",
                    ring = StoryRing.NONE
                ),
                stories = fakeHistoriesItemsFeed(),
                onClickStory = { /* navegar a historia / perfil */ },
                onClickAddStory = { /* abrir selector para crear historia */ }
            )
        }

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