package com.nsergio.dev.myinstagramcompose.core.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size

@Composable
fun RemoteAsyncImage(
    model: String,
    size: Size = Size.ORIGINAL,
    crossfade: Boolean = false,
    content: @Composable (Painter) -> Unit = {},
    errorContent: @Composable () -> Unit = {},
    onLoading: @Composable () -> Unit = {}
) {

    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(model)
            .size(size)
            .crossfade(crossfade)
            .build()
    )

    when (painter.state) {
        is AsyncImagePainter.State.Loading -> onLoading.invoke()
        is AsyncImagePainter.State.Success -> content.invoke(painter)
        is AsyncImagePainter.State.Error -> errorContent.invoke()
        else -> errorContent.invoke()
    }

}