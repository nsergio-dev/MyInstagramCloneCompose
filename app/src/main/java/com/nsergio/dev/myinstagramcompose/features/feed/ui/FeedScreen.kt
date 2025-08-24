package com.nsergio.dev.myinstagramcompose.features.feed.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.nsergio.dev.myinstagramcompose.core.ui.components.StoriesRow
import com.nsergio.dev.myinstagramcompose.core.ui.components.StoryItem
import com.nsergio.dev.myinstagramcompose.core.ui.components.StoryRing
import com.nsergio.dev.myinstagramcompose.features.feed.domain.model.PostId
import com.nsergio.dev.myinstagramcompose.features.feed.domain.model.PostWithMedia
import com.nsergio.dev.myinstagramcompose.features.feed.presentation.FeedViewModel

@Composable
fun FeedScreen(
    contentPadding: PaddingValues,
    viewModel: FeedViewModel = hiltViewModel(),
    onClickProfile: (String) -> Unit,
    onClickStory: (StoryItem) -> Unit
) {
    val posts: LazyPagingItems<PostWithMedia> = viewModel.posts.collectAsLazyPagingItems()
    val stories by viewModel.stories.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues = contentPadding),
        contentAlignment = Alignment.Center
    ) {
        Posts(
            posts = posts,
            stories = stories,
            currentUserAvatarUrl = currentUser?.avatarUrl.orEmpty(),
            onLikeToggle = viewModel::onLikeToggle,
            onProfileClick = onClickProfile,
            onClickStory = { storyItem ->
                viewModel.updateStoryState(storyItem, onClickStory)
            }
        )
    }
}

@Composable
private fun Posts(
    currentUserAvatarUrl: String,
    posts: LazyPagingItems<PostWithMedia>,
    stories: List<StoryItem> = emptyList(),
    onLikeToggle: (String) -> Unit,
    onProfileClick: (String) -> Unit,
    onClickStory: (StoryItem) -> Unit
) {
    LazyColumn {
        item {
            StoriesRow(
                currentUser = StoryItem(
                    idHistory = PostId("me"),
                    username = "Me",
                    postId = PostId("me"),
                    avatarUrl = currentUserAvatarUrl,
                    ring = StoryRing.NONE
                ),
                stories = stories,
                onClickStory = onClickStory,
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