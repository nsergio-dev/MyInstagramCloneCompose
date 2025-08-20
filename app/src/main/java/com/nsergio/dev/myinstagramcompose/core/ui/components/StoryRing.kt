package com.nsergio.dev.myinstagramcompose.core.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.nsergio.dev.myinstagramcompose.core.utils.clickableNoRipple
import com.nsergio.dev.myinstagramcompose.features.feed.domain.model.PostId
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlin.math.abs

/**
 * Estado visual del aro alrededor del avatar.
 */
enum class StoryRing {
    NONE, //old
    RED, //new
    GREEN // seen
}

@Immutable
data class StoryItem(
    val idHistory: PostId,
    val postId: PostId,
    val username: String,
    val avatarUrl: String,
    val ring: StoryRing
)

/**
 * Row de stories/reels recientes.
 * El primer elemento suele ser el usuario de sesión con botón "+" en la esquina.
 */
@Composable
fun StoriesRow(
    stories: List<StoryItem>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(horizontal = 12.dp),
    horizontalSpacing: Dp = 12.dp,
    currentUser: StoryItem? = null,
    onClickStory: (StoryItem) -> Unit,
    onClickAddStory: () -> Unit
) {

    val listState = rememberLazyListState()

    LaunchedEffect(Unit) {

        snapshotFlow { listState.layoutInfo.totalItemsCount }
            .filter { it > 0 }
            .first()

        listState.scrollToItem(0)
    }

    val nested = remember {

        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {

                if (abs(available.x) > abs(available.y)) {
                    val draggingRight = available.x > 0f
                    val draggingLeft = available.x < 0f
                    val atStart = !listState.canScrollBackward
                    val atEnd = !listState.canScrollForward


                    val shouldConsume =
                        (draggingRight && atStart) || (draggingLeft && atEnd)

                    if (shouldConsume) {

                        return Offset(available.x, 0f)
                    }
                }

                return Offset.Zero
            }

            override suspend fun onPreFling(available: Velocity): Velocity {
                if (abs(available.x) > abs(available.y)) {
                    val flingRight = available.x > 0f
                    val flingLeft = available.x < 0f
                    val atStart = !listState.canScrollBackward
                    val atEnd = !listState.canScrollForward

                    val shouldConsume =
                        (flingRight && atStart) || (flingLeft && atEnd)

                    if (shouldConsume) return available
                }
                return Velocity.Zero
            }
        }
    }

    LazyRow(
        state = listState,
        modifier = modifier.nestedScroll(nested),
        contentPadding = contentPadding,
        horizontalArrangement = Arrangement.spacedBy(horizontalSpacing)
    ) {

        item(key = "me") {
            if (currentUser != null) {
                StoryAvatarWithLabel(
                    item = currentUser,
                    showAddButton = true,
                    onClick = { onClickStory(currentUser) },
                    onClickAdd = onClickAddStory
                )
            } else {
                Spacer(modifier = Modifier.size(1.dp))
            }
        }

        items(
            items = stories,
            key = { it.idHistory.value }
        ) { item ->
            StoryAvatarWithLabel(
                item = item,
                showAddButton = false,
                onClick = { onClickStory(item) },
                onClickAdd = {}
            )
        }
    }
}

/**
 * Avatar circular con aro, más el label con el nombre debajo.
 * El nombre usa minWidth y elipsis si se excede el ancho.
 */
@Composable
private fun StoryAvatarWithLabel(
    item: StoryItem,
    showAddButton: Boolean,
    onClick: () -> Unit,
    onClickAdd: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        StoryAvatar(
            imageUrl = item.avatarUrl,
            ring = item.ring,
            size = 68.dp,
            showAddButton = showAddButton,
            onClick = onClick,
            onClickAdd = onClickAdd
        )
        Spacer(modifier = Modifier.size(6.dp))
        Text(
            text = item.username,
            style = MaterialTheme.typography.labelMedium,
            maxLines = 1,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .widthIn(min = 56.dp, max = 76.dp),
        )
    }
}

/**
 * Muestra el avatar con aro rojo/verde/ninguno.
 * Si showAddButton = true, coloca un botón "+" en la esquina inferior-derecha (posición 4:16).
 */
@Composable
private fun StoryAvatar(
    imageUrl: String,
    ring: StoryRing,
    size: Dp,
    showAddButton: Boolean,
    onClick: () -> Unit,
    onClickAdd: () -> Unit
) {
    val ringBrush = when (ring) {
        StoryRing.NONE -> null
        StoryRing.RED -> Brush.sweepGradient(
            colors = listOf(
                Color(0xFFE3003A),
                Color(0xFFE3003A)
            )
        )

        StoryRing.GREEN -> Brush.sweepGradient(
            colors = listOf(
                Color(0xFF2ECC71),
                Color(0xFF2ECC71)
            )
        )
    }

    Box(
        modifier = Modifier
            .size(size)
            .clickableNoRipple(onClick),
        contentAlignment = Alignment.Center
    ) {

        if (ringBrush != null) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .border(
                        border = BorderStroke(3.dp, ringBrush),
                        shape = CircleShape
                    )
            )
        }


        AsyncImage(
            model = imageUrl,
            contentDescription = "story avatar",
            modifier = Modifier
                .size(size - 8.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentScale = ContentScale.Crop
        )


        if (showAddButton) {
            Surface(
                shape = CircleShape,
                color = Color.Black,
                contentColor = Color.White,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = (-4).dp, y = (-4).dp)
                    .size(20.dp)
                    .clickable { onClickAdd() }
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "add story",
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}