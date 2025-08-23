package com.nsergio.dev.myinstagramcompose.features.photo_preview.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity

@Composable
fun PhotoViewerImageScreen(
    imageUrl: String,
    onBack: () -> Unit
) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        val density = LocalDensity.current
        val containerSize = remember(maxWidth, maxHeight) {
            Size(
                width = with(density) { maxWidth.toPx() },
                height = with(density) { maxHeight.toPx() }
            )
        }
        val zoomState = rememberImageZoomState(maxScale = 4f)

        DragToDismissContainer(
            zoomScale = zoomState.scale.value,
            zoomOffset = zoomState.offset.value,
            onDismiss = onBack
        ) {
            this.ZoomableImage(
                imageUrl = imageUrl,
                zoomState = zoomState,
                containerSize = containerSize
            )
        }

        IconButton(
            onClick = onBack,
            modifier = Modifier
                .align(Alignment.TopStart)
                .statusBarsPadding()
        ) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = "Cerrar",
                tint = Color.White
            )
        }
    }
}


/*

@Composable
fun PhotoViewerImageScreen(
    imageUrl: String,
    onBack: () -> Unit
) {
    BoxWithConstraints (
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {

        val zoomState =  rememberImageZoomState(maxScale = 4f)
        val containerWidthPx = with(LocalDensity.current) { maxWidth.toPx() }
        val containerHeightPx = with(LocalDensity.current) { maxHeight.toPx() }

        ZoomableImage(
            imageUrl = imageUrl,
            zoomState = zoomState,
            containerSize = Size(
                width = containerWidthPx,
                height = containerHeightPx
            )
        )

        IconButton(
            onClick = onBack,
            modifier = Modifier
                .align(Alignment.TopStart)
                .statusBarsPadding()
        ) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = "Cerrar",
                tint = Color.White
            )
        }
    }
}*/
