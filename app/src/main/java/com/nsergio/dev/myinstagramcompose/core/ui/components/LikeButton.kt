package com.nsergio.dev.myinstagramcompose.core.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
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
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current,
) {

    val imageVector = getImageVector(liked)

    LikeIconButton(liked, imageVector, modifier, onToggle, tint = tint)

}

@Composable
fun CommentsButton(
    modifier: Modifier = Modifier,
    contextDescription: String = "Comments",
    onClick: () -> Unit = { },
    tint: Color = LocalContentColor.current,
) {

    IconButton(
        modifier = modifier,
        onClick = onClick
    ) {
        Icon(
            imageVector = Icons.Outlined.ChatBubbleOutline,
            contentDescription = contextDescription,
            tint = tint
        )
    }

}

@Composable
fun ShareButton(
    modifier: Modifier = Modifier,
    contextDescription: String = "Comments",
    tint: Color = LocalContentColor.current,
    onClick: () -> Unit = { },
) {

    IconButton(
        modifier = modifier,
        onClick = onClick
    ) {

        Icon(
            imageVector = Icons.Outlined.Send,
            contentDescription = contextDescription,
            tint = tint
        )
    }

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
private fun LikeIconButton(
    liked: Boolean,
    imageVector: ImageVector,
    modifier: Modifier,
    onToggle: () -> Unit,
    tint: Color = LocalContentColor.current
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
        tint = if (liked) Color.Red else tint,
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