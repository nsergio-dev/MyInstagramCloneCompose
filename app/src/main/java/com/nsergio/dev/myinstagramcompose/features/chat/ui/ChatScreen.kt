package com.nsergio.dev.myinstagramcompose.features.chat.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.nsergio.dev.myinstagramcompose.core.ui.DimensDP
import com.nsergio.dev.myinstagramcompose.features.chat.models.ChatItem
import com.nsergio.dev.myinstagramcompose.features.chat.models.ChatStory
import com.nsergio.dev.myinstagramcompose.features.chat.presentation.ChatViewModel

@Composable
fun ChatScreen(
    contentPadding: PaddingValues,
    onBack: () -> Unit,
    onOpenRequests: () -> Unit,
    onOpenComposer: () -> Unit,
    onClickStory: (String) -> Unit,
    onOpenConversation: (String) -> Unit,
    onOpenCamera: (String) -> Unit,
    viewModel: ChatViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsState()
    val query by viewModel.query.collectAsState()
    val user by viewModel.user.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getUser()
        viewModel.load()
    }

    user?.let { safeUser ->

        ChatInboxScreen(
            username = safeUser.name,
            currentQuery = query,
            onQueryChange = viewModel::updateQuery,
            contentPadding = contentPadding,
            stories = uiState.stories,
            messages = uiState.messages,
            onBack = onBack,
            onOpenRequests = onOpenRequests,
            onOpenComposer = onOpenComposer,
            onClickStory = onClickStory,
            onOpenConversation = onOpenConversation,
            onOpenCamera = onOpenCamera
        )
    }

}

@Composable
private fun ChatInboxScreen(
    username: String,
    currentQuery: String,
    onQueryChange: (String) -> Unit,
    contentPadding: PaddingValues = PaddingValues(DimensDP.DP0.dp),
    stories: List<ChatStory>,
    messages: List<ChatItem>,
    onBack: () -> Unit,
    onOpenRequests: () -> Unit,
    onOpenComposer: () -> Unit,
    onClickStory: (String) -> Unit,
    onOpenConversation: (String) -> Unit,
    onOpenCamera: (String) -> Unit,

) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
    ) {

        ChatTopRow(
            username = username,
            onBack = onBack,
            onOpenRequests = onOpenRequests,
            onOpenComposer = onOpenComposer
        )

        Spacer(modifier = Modifier.height(DimensDP.DP8.dp))

        SearchBar(
            currentQuery = currentQuery,
            onQueryChange = onQueryChange
        )

        Spacer(modifier = Modifier.height(DimensDP.DP8.dp))

        StoriesRow(stories = stories, onClickStory = onClickStory)

        Spacer(modifier = Modifier.height(DimensDP.DP12.dp))

        MessagesHeader(onOpenRequests = onOpenRequests)

        MessagesList(
            items = messages,
            onOpenConversation = onOpenConversation,
            onOpenCamera = onOpenCamera
        )
    }
}

@Composable
private fun ChatTopRow(
    username: String,
    onBack: () -> Unit,
    onOpenRequests: () -> Unit,
    onOpenComposer: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = DimensDP.DP16.dp, vertical = DimensDP.DP8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBack) {
            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
        }
        Text(
            text = username,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
            modifier = Modifier
                .padding(start = DimensDP.DP8.dp)
                .weight(1f)
        )
        IconButton(onClick = onOpenRequests) {
            Icon(imageVector = Icons.Filled.CameraAlt, contentDescription = "Requests")
        }
        IconButton(onClick = onOpenComposer) {
            Icon(imageVector = Icons.Filled.Create, contentDescription = "New message")
        }
    }
}

@Composable
private fun SearchBar(
    currentQuery: String,
    onQueryChange: (String) -> Unit
) {
    OutlinedTextField(
        value = currentQuery,
        onValueChange = onQueryChange,
        placeholder = { Text(text = "Search") },
        singleLine = true,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = DimensDP.DP16.dp),
        shape = RoundedCornerShape(DimensDP.DP24.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            cursorColor = MaterialTheme.colorScheme.primary,
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface
        )
    )
}

@Composable
private fun StoriesRow(
    stories: List<ChatStory>,
    onClickStory: (String) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = DimensDP.DP16.dp),
        horizontalArrangement = Arrangement.spacedBy(DimensDP.DP12.dp)
    ) {
        items(stories) { story ->
            StoryBubble(story = story) { onClickStory(story.userId) }
        }
    }
}

@Composable
private fun StoryBubble(
    story: ChatStory,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(DimensDP.DP92.dp)
    ) {
        Box(
            modifier = Modifier
                .size(DimensDP.DP72.dp)
                .clip(CircleShape)
                .border(
                    width = DimensDP.DP3.dp,
                    color = if (story.isOnline) Color(0xFF34C759) else Color(0xFFFF2D55),
                    shape = CircleShape
                )
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = rememberAsyncImagePainter(story.photoUrl),
                contentDescription = story.username,
                modifier = Modifier
                    .size(DimensDP.DP66.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }
        Box(
            modifier = Modifier
                .padding(top = DimensDP.DP8.dp)
                .shadow(DimensDP.DP2.dp, RoundedCornerShape(DimensDP.DP12.dp))
                .clip(RoundedCornerShape(DimensDP.DP12.dp))
                .background(Color.White)
                .padding(horizontal = DimensDP.DP8.dp, vertical = DimensDP.DP4.dp)
        ) {
            Text(
                text = story.caption,
                style = MaterialTheme.typography.labelSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Spacer(modifier = Modifier.height(DimensDP.DP4.dp))
        Text(
            text = story.username,
            style = MaterialTheme.typography.labelSmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun MessagesHeader(
    onOpenRequests: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = DimensDP.DP16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Messages",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            modifier = Modifier.weight(1f)
        )
        Text(
            text = "Requests",
            color = Color(0xFF2F80ED),
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            modifier = Modifier.clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onOpenRequests() }
        )
    }
}

@Composable
private fun MessagesList(
    items: List<ChatItem>,
    onOpenConversation: (String) -> Unit,
    onOpenCamera: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            top = DimensDP.DP12.dp,
            bottom = DimensDP.DP24.dp
        ),
        verticalArrangement = Arrangement.spacedBy(DimensDP.DP8.dp)
    ) {
        items(items) { chat ->
            MessageItem(
                item = chat,
                onOpen = { onOpenConversation(chat.userId) },
                onOpenCamera = { onOpenCamera(chat.userId) }
            )
            HorizontalDivider(
                Modifier,
                DividerDefaults.Thickness,
                color = MaterialTheme.colorScheme.surfaceVariant
            )
        }
    }
}

@Composable
private fun MessageItem(
    item: ChatItem,
    onOpen: () -> Unit,
    onOpenCamera: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onOpen() }
            .padding(horizontal = DimensDP.DP16.dp, vertical = DimensDP.DP8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(DimensDP.DP56.dp)
                .clip(CircleShape)
                .border(
                    width = DimensDP.DP3.dp,
                    color = if (item.isOnline) Color(0xFF34C759) else Color(0xFFFD7E14),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = rememberAsyncImagePainter(item.photoUrl),
                contentDescription = item.username,
                modifier = Modifier
                    .size(DimensDP.DP50.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = DimensDP.DP12.dp)
        ) {
            Text(
                text = item.username,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Active now",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        IconButton(onClick = onOpenCamera) {
            Icon(imageVector = Icons.Filled.CameraAlt, contentDescription = "Camera")
        }
    }
}