package com.nsergio.dev.myinstagramcompose.features.photo_preview.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
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
        val images = safePost.media.filter { it.type == MediaType.IMAGE }.map { it.url }
        val pagerState = rememberPagerState(
            pageCount = { images.size }
        )
        LaunchedEffect(startIndex) {
            pagerState.scrollToPage(startIndex)
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .pointerInput(Unit) { detectTapGestures { onClose() } }
                .systemBarsPadding()
        ) {
            HorizontalPager(state = pagerState) { page ->
                AsyncImage(
                    model = images[page],
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxSize()
                )
            }

            PositionIndicator(
                size = images.size,
                currentPage = pagerState.currentPage
            )
        }
        BackHandler(onBack = onClose)
    }
}

@Composable
private fun BoxScope.PositionIndicator(
    size: Int,
    currentPage: Int
) {
    Row(
        Modifier
            .align(Alignment.BottomCenter)
            .padding(bottom = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        repeat(size) { index ->
            val selected = index == currentPage
            val sizeBox = if (selected) DimensDP.DP8.dp else DimensDP.DP6.dp
            Box(
                Modifier
                    .size(sizeBox)
                    .background(
                        if (selected) Color.White
                        else Color.White.copy(alpha = 0.4f),
                        shape = MaterialTheme.shapes.small
                    )
            )
        }
    }
}