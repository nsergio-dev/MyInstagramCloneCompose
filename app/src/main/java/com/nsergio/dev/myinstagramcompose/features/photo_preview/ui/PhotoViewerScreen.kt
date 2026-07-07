package com.nsergio.dev.myinstagramcompose.features.photo_preview.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.hilt.navigation.compose.hiltViewModel
import com.nsergio.dev.myinstagramcompose.core.ui.DimensDP
import com.nsergio.dev.myinstagramcompose.features.feed.domain.model.MediaType
import com.nsergio.dev.myinstagramcompose.features.photo_preview.presentation.PhotoViewerViewModel

@Composable
fun PhotoViewerScreen(
    postId: String,
    index: Int,
    viewModel: PhotoViewerViewModel = hiltViewModel(),
    onClose: () -> Unit,
) {
    LaunchedEffect(postId, index) {
        viewModel.setParams(postId, index)
    }

    val post by viewModel.post.collectAsState()
    val startIndex by viewModel.startIndex.collectAsState()

    post?.let { safePost ->
        PhotoViewerContent(
            images = safePost.media
                .filter { it.type == MediaType.IMAGE }
                .map { it.url },
            caption = safePost.caption,
            authorName = safePost.authorName,
            authorAvatarUrl = safePost.authorAvatarUrl,
            initialPage = startIndex,
            onClose = onClose
        )
    }
}

@Composable
fun PhotoViewerContent(
    images: List<String>,
    caption: String,
    authorName: String,
    authorAvatarUrl: String,
    initialPage: Int,
    onClose: () -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { images.size })
    LaunchedEffect(initialPage) { pagerState.scrollToPage(initialPage) }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .systemBarsPadding()
    ) {
        val containerWidthPx = with(LocalDensity.current) { maxWidth.toPx() }
        val containerHeightPx = with(LocalDensity.current) { maxHeight.toPx() }

        ZoomableImagePager(
            images = images,
            pagerState = pagerState,
            containerSize = Size(
                width = containerWidthPx,
                height = containerHeightPx
            )
        )

        PositionIndicator(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = DimensDP.DP16.dp),
            size = images.size,
            currentPage = pagerState.currentPage
        )

        CaptionAndUserInfo(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(bottom = DimensDP.DP48.dp),
            caption = caption,
            userName = authorName,
            userAvatarUrl = authorAvatarUrl
        )
    }

    BackHandler(onBack = onClose)
}