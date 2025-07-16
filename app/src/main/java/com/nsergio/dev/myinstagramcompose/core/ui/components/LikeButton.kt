package com.nsergio.dev.myinstagramcompose.core.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.nsergio.dev.myinstagramcompose.core.utils.clickableNoRipple

@Composable
fun LikeButton(
    liked: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {

    val imageVector = getImageVector(liked)

    IconButton(liked, imageVector, modifier, onToggle)

}

@Composable
private fun getImageVector(liked: Boolean): ImageVector {
    return if (liked) {
        Icons.Rounded.Favorite
    } else {
        Icons.Rounded.FavoriteBorder
    }
}

@Composable
private fun IconButton(
    liked: Boolean,
    imageVector: ImageVector,
    modifier: Modifier,
    onToggle: () -> Unit
) {
    //val target = if (liked) 1f else 0.8f
    //val scale by animateFloatAsState(target, label = "likeScale")
    val scale by animateFloatAsState(
        targetValue = if (liked) 1.2f else 1f,
        animationSpec = tween(200),
        label = "likeBounce"
    )

    Icon(
        imageVector = imageVector,
        contentDescription = null,
        tint = if (liked) Color.Red else MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = modifier
            .scale(scale)
            .clickableNoRipple(onToggle)
    )
    /*Icon(
        imageVector = imageVector,
        contentDescription = null,
        tint = if (liked) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = modifier
            .scale(scale)
            .clickable { onToggle.invoke() }
    )*/
}