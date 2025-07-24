package com.nsergio.dev.myinstagramcompose.features.photo_preview.ui


import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import com.nsergio.dev.myinstagramcompose.core.ui.components.RemoteAsyncImage


data class ImageZoomState(
    val scale: MutableState<Float>,
    val offset: MutableState<Offset>,
    val maxScale: Float
)

@Composable
fun rememberImageZoomState(maxScale: Float) = ImageZoomState(
    scale = remember { mutableFloatStateOf(1f) },
    offset = remember { mutableStateOf(Offset.Zero) },
    maxScale = maxScale
)

@Composable
fun BoxWithConstraintsScope.ZoomableImagePager(
    images: List<String>,
    pagerState: PagerState,
    containerSize: Size
) {
    val zoomState = rememberImageZoomState(maxScale = 4f)

    HorizontalPager(
        state = pagerState,
        modifier = Modifier.matchParentSize(),
        userScrollEnabled = (zoomState.scale.value == 1f)
    ) { page ->
        val imageUrl = images[page]
        ZoomableImage(
            imageUrl = imageUrl,
            zoomState = zoomState,
            containerSize = containerSize
        )
    }
}

@Composable
fun BoxWithConstraintsScope.ZoomableImage(
    imageUrl: String,
    zoomState: ImageZoomState,
    containerSize: Size
) {
    RemoteAsyncImage(
        model = imageUrl,
        crossfade = true,
        content = { painter ->
            Image(
                painter = painter,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .matchParentSize()
                    .graphicsLayer(
                        scaleX = zoomState.scale.value,
                        scaleY = zoomState.scale.value,
                        translationX = zoomState.offset.value.x,
                        translationY = zoomState.offset.value.y
                    )
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onDoubleTap = {
                                zoomState.scale.value = 1f
                                zoomState.offset.value = Offset.Zero
                            }
                        )
                    }
                    .pointerInput(Unit) {
                        detectConditionalTransformGestures { _, pan, zoom, _ ->
                            val newZoomValue = zoomState.scale.value * zoom
                            val newScale = newZoomValue.coerceIn(1f, zoomState.maxScale)

                            val maxWidth = (containerSize.width * (newScale - 1f)) / 2f
                            val maxHeight = (containerSize.height * (newScale - 1f)) / 2f

                            val scaledPan = pan * zoomState.scale.value

                            zoomState.offset.value = calculateOffset(
                                newScale = newScale,
                                zoomState = zoomState,
                                scaledPan = scaledPan,
                                maxX = maxWidth,
                                maxY = maxHeight
                            )

                            zoomState.scale.value = newScale
                        }
                    }
            )
        },
        onLoading = {
            Box(Modifier.fillMaxSize()) {
                CircularProgressIndicator(Modifier.align(androidx.compose.ui.Alignment.Center))
            }
        }
    )
}

private fun calculateOffset(
    newScale: Float,
    zoomState: ImageZoomState,
    scaledPan: Offset,
    maxX: Float,
    maxY: Float
): Offset {
    return if (newScale > 1f) {
        Offset(
            x = (zoomState.offset.value.x + scaledPan.x).coerceIn(-maxX, maxX),
            y = (zoomState.offset.value.y + scaledPan.y).coerceIn(-maxY, maxY)
        )
    } else {
        Offset.Zero
    }
}