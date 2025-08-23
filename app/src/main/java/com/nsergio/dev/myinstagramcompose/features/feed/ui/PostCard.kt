package com.nsergio.dev.myinstagramcompose.features.feed.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import com.nsergio.dev.myinstagramcompose.core.ui.DimensDP
import com.nsergio.dev.myinstagramcompose.core.ui.components.CircularAvatar
import com.nsergio.dev.myinstagramcompose.core.ui.components.CommentsButton
import com.nsergio.dev.myinstagramcompose.core.ui.components.LikeButton
import com.nsergio.dev.myinstagramcompose.core.ui.components.RemoteAsyncImage
import com.nsergio.dev.myinstagramcompose.core.ui.components.ShareButton
import com.nsergio.dev.myinstagramcompose.core.utils.clickableNoRipple
import com.nsergio.dev.myinstagramcompose.core.utils.relativeTimeString
import com.nsergio.dev.myinstagramcompose.features.feed.domain.model.MediaType
import com.nsergio.dev.myinstagramcompose.features.feed.domain.model.PostWithMedia
import kotlinx.coroutines.delay


/**
 * @param post Post to display
 */
@Composable
fun PostCard(
    post: PostWithMedia,
    onLikeToggle: (String) -> Unit,
    onProfileClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {

        UserImage(
            authorName = post.authorName,
            imageUrl = post.authorAvatarUrl,
            onClick = onProfileClick
        )

        val imageUrls = post.media
            .filter { it.type == MediaType.IMAGE }
            .map { it.url }

        PostImage(
            imageUrls = imageUrls,
            startIndex = 0,
            onDoubleTap = { onLikeToggle(post.id.value) }
        )

        PostActions(
            liked = post.likedByMe,
            likes = post.likeCount,
            comments = post.commentCount,
            shares = post.shareCount,
            onLike = { onLikeToggle(post.id.value) }
        )

        UserNameAndCaption(
            user = post.authorName,
            caption = post.caption
        )
        TimeAgoText(epochMillis = post.createdAt)
    }
}

@Composable
private fun PostImage(
    imageUrls: List<String>,
    startIndex: Int,
    onDoubleTap: () -> Unit
) {
    var showHeart by remember { mutableStateOf(false) }

    val imageUrl = imageUrls[startIndex]


    /* Auto-hide heart after 400 ms */
    if (showHeart) {
        LaunchedEffect(Unit) {
            delay(400)
            showHeart = false
        }
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(DimensDP.DP300.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = {
                        showHeart = true
                        onDoubleTap()
                    }
                )
            }
    ) {

        RemoteAsyncImage(
            model = imageUrl,
            crossfade = true,
            content = {
                Image(
                    contentDescription = null,
                    modifier = Modifier
                        .matchParentSize()
                        .background(Color.Black),
                    contentScale = ContentScale.Crop,
                    painter = it,
                )
            },
            onLoading = {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        )


        HeartAnimationPost(showHeart)
    }
}

@Composable
private fun BoxScope.HeartAnimationPost(showHeart: Boolean) {
    /* Heart fades/scales in, then out */
    val alpha by animateFloatAsState(
        targetValue = if (showHeart) 3f else 0f,
        label = "heartAlpha"
    )
    val scale by animateFloatAsState(
        targetValue = if (showHeart) 3f else 0.3f,
        label = "heartScale"
    )
    /* Overlay heart */
    Icon(
        imageVector = Icons.Rounded.Favorite,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.secondary,
        modifier = Modifier
            .align(Alignment.Center)
            .scale(scale)
            .alpha(alpha)
    )
}

@Composable
private fun UserImage(
    authorName: String,
    imageUrl: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier.padding(DimensDP.DP12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        CircularAvatar(imageUrl = imageUrl)

        Text(
            text = authorName,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .padding(start = DimensDP.DP12.dp)
                .weight(1f)
                .clickableNoRipple(onClick),
        )
    }
}

//buttons

/**
 * Row with like, comment and share icons + counters.
 *
 * @param liked Whether the post is liked
 * @param likes Like counter
 * @param comments Comment counter
 * @param shares Share counter
 * @param onLike Click handler for like icon
 */
@Composable
private fun PostActions(
    liked: Boolean,
    likes: Int,
    comments: Int,
    shares: Int,
    onLike: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(DimensDP.DP12.dp),
        horizontalArrangement = Arrangement.spacedBy(DimensDP.DP16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HorizontalActionIconText(
            icon = {
                LikeButton(liked = liked, onToggle = onLike)
            },
            text = (likes + if (liked) 1 else 0).toString()
        )
        HorizontalActionIconText(
            icon = { CommentsButton() },
            text = comments.toString()
        )
        HorizontalActionIconText(
            icon = { ShareButton() },
            text = shares.toString()
        )
        Spacer(modifier = Modifier.weight(1f))
        Icon(Icons.Outlined.BookmarkBorder, contentDescription = null)
    }
}

/**
 * Helper composable that shows an icon followed by a label.
 *
 * @param icon Icon to display
 * @param text Label to display next to the icon
 */
@Composable
fun HorizontalActionIconText(
    icon: @Composable () -> Unit,
    text: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        icon()
        Spacer(Modifier.width(DimensDP.DP4.dp))
        Text(text, style = MaterialTheme.typography.bodySmall)
    }
}

/**
 * Helper composable that shows an icon followed by a label.
 *
 * @param icon Icon to display
 * @param text Label to display next to the icon
 */
@Composable
fun VerticalActionIconText(
    icon: @Composable () -> Unit,
    text: String,
    textColor: Color = Color.Unspecified
) {
    Column(
        modifier = Modifier
            .padding(end = DimensDP.DP12.dp, bottom = DimensDP.DP24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        icon()
        Spacer(Modifier.width(DimensDP.DP4.dp))
        Text(text, style = MaterialTheme.typography.bodySmall, color = textColor)
    }
}

/**
 * Caption with “see more” behaviour.
 *
 * @param user Display name of the author
 * @param caption Full caption text
 */
@Composable
fun UserNameAndCaption(
    user: String,
    caption: String
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    val combined = buildAnnotatedString {
        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append(user) }
        append(" ")
        append(caption)
    }

    val maxLines = if (expanded) Int.MAX_VALUE else 1

    Box(
        modifier = Modifier
            .padding(horizontal = DimensDP.DP12.dp)
            .heightIn(max = DimensDP.DP64.dp)
            .verticalScroll(
                rememberScrollState(),
                enabled = expanded
            )
    ) {
        Text(
            modifier = Modifier
                .clickableNoRipple { expanded = !expanded },
            text = combined,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = maxLines,
            overflow = TextOverflow.Ellipsis
        )

    }
}

/**
 * Timestamp text (“Hace 3 horas”, “Hace 5 días”).
 *
 * @param epochMillis Creation time of the post in millis
 */
@Composable
private fun TimeAgoText(epochMillis: Long) {
    Text(
        text = relativeTimeString(epochMillis),
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(top = DimensDP.DP4.dp, start = DimensDP.DP12.dp)
    )
}

