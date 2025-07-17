package com.nsergio.dev.myinstagramcompose.core.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
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
    RemoteAsyncImage(
        model = imageUrl,
        crossfade = true,
        content = {
            Image(
                contentDescription = contentDescription,
                modifier = Modifier
                    .size(size)
                    .clip(CircleShape),
                painter = it,
            )
        },
        onLoading = {
            Image(
                contentDescription = contentDescription,
                modifier = Modifier
                    .size(size)
                    .clip(CircleShape),
                imageVector = Icons.Rounded.AccountCircle,
            )
        }
    )
}
