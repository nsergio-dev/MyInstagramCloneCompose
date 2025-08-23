package com.nsergio.dev.myinstagramcompose.features.reels.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nsergio.dev.myinstagramcompose.core.ui.DimensDP
import com.nsergio.dev.myinstagramcompose.core.ui.components.CommentsButton
import com.nsergio.dev.myinstagramcompose.core.ui.components.LikeButton
import com.nsergio.dev.myinstagramcompose.core.ui.components.RemoteAsyncImage
import com.nsergio.dev.myinstagramcompose.core.ui.components.ShareButton
import com.nsergio.dev.myinstagramcompose.features.feed.domain.model.PostWithMedia
import com.nsergio.dev.myinstagramcompose.features.feed.ui.VerticalActionIconText
import com.nsergio.dev.myinstagramcompose.features.photo_preview.ui.CaptionAndUserInfo
import com.nsergio.dev.myinstagramcompose.features.reels.presentation.ReelsViewModel

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ReelsScreen(
    onBack: () -> Unit,
    viewModel: ReelsViewModel = hiltViewModel()
) {
    val reels by viewModel.reels.collectAsState()

    Scaffold { innerPadding ->
        if (reels.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator() }
        } else {

            val pagerState = rememberPagerState(initialPage = 0) { reels.size }

            VerticalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(Color.Black)
            ) { page ->
                val post = reels[page]
                ReelPage(
                    post = post,
                    onLike = { viewModel.onLikeToggle(post.id) },
                    onComment = { viewModel.onComment(post.id) }
                )
            }
        }
    }
}

@Composable
private fun ReelPage(
    post: PostWithMedia,
    onLike: () -> Unit,
    onComment: () -> Unit
) {
    val media = post.media.firstOrNull()

    var showBackgroundGradient by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        RemoteAsyncImage(
            model = media?.url.orEmpty(),
            crossfade = true,
            content = {
                showBackgroundGradient = true
                Image(
                    painter = it,
                    contentDescription = post.caption,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            },
            onLoading = {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp)
                }
            }
        )

        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .height(220.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color(0xAA000000))
                    )
                )
        )

        CaptionAndUserInfo(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(bottom = DimensDP.DP48.dp),
            caption = post.caption,
            userName = post.authorName,
            userAvatarUrl = post.authorAvatarUrl,
            showBackgroundGradient = showBackgroundGradient
        )

        VerticalReelsButtons(
            liked = post.likedByMe,
            likes = post.likeCount,
            comments = post.commentCount,
            shares = post.shareCount,
            onClickLikeButton = onLike,
            onClickCommentsButton = onComment,
            showBackgroundGradient = showBackgroundGradient
        )
    }
}
@Composable
private fun BoxScope.VerticalReelsButtons(
    liked: Boolean,
    likes: Int,
    comments: Int,
    shares: Int,
    onClickLikeButton: () -> Unit,
    onClickCommentsButton: () -> Unit,
    showBackgroundGradient: Boolean = true
) {
    var colorBackground = Color.Transparent
    if (showBackgroundGradient) {
        colorBackground = Color.Gray.copy(alpha = 0.1f)
    }

    Column(
        modifier = Modifier
            .align(Alignment.CenterEnd)
            .padding(bottom = 112.dp)
            .background(
                colorBackground, // Subtle background for better readability
                shape = MaterialTheme.shapes.small
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val defaultTint = Color.White
        VerticalActionIconText(
            textColor = defaultTint,
            icon = {
                LikeButton(liked = liked, onToggle = onClickLikeButton, tint = defaultTint)
            },
            text = (likes + if (liked) 1 else 0).toString()
        )
        VerticalActionIconText(
            textColor = defaultTint,
            icon = { CommentsButton(onClick = onClickCommentsButton, tint = defaultTint) },
            text = comments.toString()
        )
        VerticalActionIconText(
            textColor = defaultTint,
            icon = { ShareButton(tint = defaultTint) },
            text = shares.toString()
        )

        IconButton(onClick = {  }) {
            Icon(
                imageVector = Icons.Outlined.MoreVert,
                contentDescription = "More",
                tint = defaultTint,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}