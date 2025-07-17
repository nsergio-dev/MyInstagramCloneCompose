package com.nsergio.dev.myinstagramcompose.features.photo_preview.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import com.nsergio.dev.myinstagramcompose.core.ui.DimensDP
import com.nsergio.dev.myinstagramcompose.core.ui.components.CircularAvatar
import com.nsergio.dev.myinstagramcompose.core.ui.components.RemoteAsyncImage
import com.nsergio.dev.myinstagramcompose.core.utils.clickableNoRipple
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
                .pointerInput(Unit) {
                    detectTapGestures {

                    }
                }
                .systemBarsPadding()
        ) {
            HorizontalPager(state = pagerState, modifier = Modifier.matchParentSize()) { page ->
                val imageUrl = images[page]
                RemoteAsyncImage(
                    model = imageUrl,
                    crossfade = true,
                    content = {
                        Image(
                            contentDescription = null,
                            modifier = Modifier
                                .matchParentSize(),
                            contentScale = ContentScale.Crop,
                            painter = it,
                        )
                    },
                    onLoading = {
                        Box (modifier = Modifier.fillMaxSize()) {
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                        }
                    }
                )
            }

            PositionIndicator(
                size = images.size,
                currentPage = pagerState.currentPage
            )

            Caption(
                modifier = Modifier
                    .align(Alignment.BottomStart),
                caption = safePost.caption,
                userName = safePost.authorName,
                userAvatarUrl = safePost.authorAvatarUrl
            )

        }
        BackHandler(onBack = onClose)
    }
}

@Composable
private fun Caption(
    modifier: Modifier,
    caption: String,
    userName: String,
    userAvatarUrl: String,
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    val maxLines = if (expanded) Int.MAX_VALUE else 1

    Column(
        modifier = modifier
            .padding(
                start = DimensDP.DP16.dp,
                bottom = DimensDP.DP32.dp
            )
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            CircularAvatar(imageUrl = userAvatarUrl)
            Spacer(Modifier.width(DimensDP.DP8.dp))
            Text(
                text = userName,
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Box(
            modifier = modifier
                .padding(top = DimensDP.DP8.dp)
                .heightIn(max = DimensDP.DP64.dp)
                .verticalScroll(
                    rememberScrollState(),
                    enabled = expanded
                )
        ) {
            Text(
                modifier = Modifier
                    .clickableNoRipple {
                        expanded = !expanded
                    },
                text = caption,
                color = Color.White,
                style = MaterialTheme.typography.bodySmall,
                maxLines = maxLines,
                overflow = TextOverflow.Ellipsis
            )

        }

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
            .padding(bottom = DimensDP.DP16.dp),
        horizontalArrangement = Arrangement.spacedBy(DimensDP.DP4.dp)
    ) {
        if (size <= 1) return@Row
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