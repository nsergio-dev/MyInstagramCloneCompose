package com.nsergio.dev.myinstagramcompose.core.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import coil.compose.AsyncImage
import com.nsergio.dev.myinstagramcompose.core.ui.DimensDP

/**
 * Circular user avatar.
 *
 * @param imageUrl URL of the avatar image
 */
@Composable
fun CircularAvatar(
    imageUrl: String,
    size: Dp = DimensDP.DP56.dp,
    contentDescription: String? = null
) {
    AsyncImage(
        model = imageUrl,
        contentDescription = contentDescription,
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
    )
}
