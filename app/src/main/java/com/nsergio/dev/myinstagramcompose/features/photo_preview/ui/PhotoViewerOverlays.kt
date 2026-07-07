package com.nsergio.dev.myinstagramcompose.features.photo_preview.ui


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import com.nsergio.dev.myinstagramcompose.core.ui.DimensDP
import com.nsergio.dev.myinstagramcompose.core.ui.components.CircularAvatar
import com.nsergio.dev.myinstagramcompose.core.utils.clickableNoRipple

@Composable
fun CaptionAndUserInfo(
    modifier: Modifier,
    caption: String,
    userName: String,
    userAvatarUrl: String,
    showBackgroundGradient: Boolean = true
) {
    var isExpandedCaption by rememberSaveable { mutableStateOf(false) }
    val maxLines = calculateCaptionMaxLines(expanded = isExpandedCaption)
    var colorBackground = Color.Transparent
    if (showBackgroundGradient) {
        colorBackground = Color.Gray.copy(alpha = 0.1f)
    }
    Color.Transparent
    Column(
        modifier = modifier
            .padding(
                start = DimensDP.DP16.dp,
                end = DimensDP.DP16.dp,
            ).background(
                colorBackground,
                shape = MaterialTheme.shapes.small
            )
            .padding(DimensDP.DP8.dp)
    ) {
        UserInfo(userAvatarUrl, userName)

        CaptionAndUserInfo(
            isExpanded = isExpandedCaption,
            caption = caption,
            maxLines = maxLines,
            onChangeIsExpanded = { isExpandedCaption = it }
        )
    }
}

private fun calculateCaptionMaxLines(expanded: Boolean): Int {
    return if (expanded) Int.MAX_VALUE else 1
}

@Composable
private fun CaptionAndUserInfo(
    isExpanded: Boolean,
    caption: String,
    maxLines: Int,
    onChangeIsExpanded: (Boolean) -> Unit
) {

    Box(
        modifier = Modifier
            .padding(top = DimensDP.DP8.dp)
            .heightIn(max = DimensDP.DP64.dp)
            .verticalScroll(
                rememberScrollState(),
                enabled = isExpanded
            )
    ) {
        Text(
            modifier = Modifier
                .clickableNoRipple {
                    onChangeIsExpanded.invoke(!isExpanded)
                },
            text = caption,
            color = Color.White,
            style = MaterialTheme.typography.bodySmall,
            maxLines = maxLines,
            overflow = TextOverflow.Ellipsis
        )
    }

}

@Composable
private fun UserInfo(userAvatarUrl: String, userName: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        CircularAvatar(imageUrl = userAvatarUrl)
        Spacer(Modifier.width(DimensDP.DP8.dp))
        Text(
            text = userName,
            color = Color.White,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}


@Composable
fun PositionIndicator(
    modifier: Modifier,
    size: Int,
    currentPage: Int
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(DimensDP.DP4.dp)
    ) {
        if (size <= 1) return@Row
        ShowIndicators(size, currentPage)
    }
}

@Composable
private fun ShowIndicators(size: Int, currentPage: Int) {
    repeat(size) { index ->
        val selected = calculateIndicatorIsSelected(index, currentPage)
        val dotSize = calculateIndicatorSize(selected)
        val color = getColorIndicator(selected)
        Box(
            Modifier
                .size(dotSize)
                .background(
                    color,
                    shape = MaterialTheme.shapes.small
                )
        )
    }
}

private fun getColorIndicator(selected: Boolean): Color {
    return if (selected) {
        Color.White
    } else {
        Color.White.copy(alpha = 0.4f)
    }
}

@Composable
private fun calculateIndicatorIsSelected(index: Int, currentPage: Int): Boolean =
    index == currentPage

private fun calculateIndicatorSize(selected: Boolean): Dp {
    return if (selected) DimensDP.DP8.dp else DimensDP.DP6.dp
}