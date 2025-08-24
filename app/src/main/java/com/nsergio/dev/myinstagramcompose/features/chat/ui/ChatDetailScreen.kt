package com.nsergio.dev.myinstagramcompose.features.chat.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.Videocam
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.nsergio.dev.myinstagramcompose.core.ui.DimensDP
import com.nsergio.dev.myinstagramcompose.core.ui.components.CircularAvatar
import com.nsergio.dev.myinstagramcompose.core.utils.clickableNoRipple
import com.nsergio.dev.myinstagramcompose.features.chat.models.Message
import com.nsergio.dev.myinstagramcompose.features.chat.presentation.ChatDetailViewModel

@Composable
fun ChatDetailScreen(
    userId: String,
    onBack: () -> Unit,
    onUserProfile: () -> Unit,
    viewModel: ChatDetailViewModel = hiltViewModel()
) {
    val (text, setText) = remember { mutableStateOf("") }
    val messages by viewModel.messages.collectAsState()
    val user by viewModel.user.collectAsState()

    LaunchedEffect(userId) {
        viewModel.getUser(userId)
        viewModel.loadConversation()
    }

    user?.let { safeUser ->
        Scaffold(
            topBar = {
                ChatDetailTopBar(
                    userName = safeUser.name,
                    realName = safeUser.realName,
                    photoUrl = safeUser.avatarUrl,
                    onBack = onBack,
                    onUserProfile = onUserProfile,
                    onCall = {  },
                    onVideoCall = {  }
                )
            },
            bottomBar = {
                ChatInputBar(
                    text = text,
                    onTextChange = setText,
                    onSend = {
                        viewModel.sendMessage(text)
                        setText("")
                    }
                )
            }
        ) { innerPadding ->
            BodyMessages(
                messages = messages,
                innerPadding = innerPadding
            )
        }
    }
}

@Composable
private fun BodyMessages(
    messages: List<Message>,
    innerPadding: PaddingValues
) {
    val listState = rememberLazyListState()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.count())
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        state = listState,
        contentPadding = PaddingValues(
            start = DimensDP.DP16.dp,
            end = DimensDP.DP16.dp,
            top = DimensDP.DP12.dp,
            bottom = DimensDP.DP24.dp
        ),
        verticalArrangement = Arrangement.spacedBy(DimensDP.DP8.dp)
    ) {
        items(messages) { msg ->
            MessageBubble(message = msg)
        }
    }
}

@Composable
private fun ChatDetailTopBar(
    userName: String,
    realName: String,
    photoUrl: String,
    onBack: () -> Unit,
    onCall: () -> Unit,
    onVideoCall: () -> Unit,
    onUserProfile: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = DimensDP.DP12.dp, vertical = DimensDP.DP8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBack) {
            Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
        }

        CircularAvatar(
            imageUrl = photoUrl,
            size = DimensDP.DP36.dp,
            contentDescription = userName
        )

        Column(
            modifier = Modifier
                .padding(start = DimensDP.DP12.dp)
                .weight(1f)
                .clickableNoRipple(onUserProfile)
        ) {
            Text(
                text = userName,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = realName,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodySmall
            )
        }

        IconButton(onClick = onCall) {
            Icon(imageVector = Icons.Outlined.Call, contentDescription = "Call")
        }
        IconButton(onClick = onVideoCall) {
            Icon(imageVector = Icons.Outlined.Videocam, contentDescription = "Video call")
        }
    }
}

@Composable
private fun ChatInputBar(
    text: String,
    onTextChange: (String) -> Unit,
    onSend: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .imePadding()
            .padding(
                horizontal = DimensDP.DP8.dp,
                vertical = DimensDP.DP8.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = onTextChange,
            placeholder = { Text("Message...") },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(DimensDP.DP24.dp),
            colors = TextFieldDefaults.colors(
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            )
        )
        Spacer(modifier = Modifier.widthIn(min = DimensDP.DP8.dp))
        TextButton(
            onClick = onSend,
            enabled = text.isNotBlank()
        ) {
            Text(text = "Send")
        }
    }
}

@Composable
private fun MessageBubble(message: Message) {
    val background = if (message.fromMe) Color(0xFF1E90FF) else Color(0xFFE5E5EA)
    val textColor = if (message.fromMe) Color.White else Color.Black
    val align = if (message.fromMe) Alignment.CenterEnd else Alignment.CenterStart
    val shape = if (message.fromMe) {
        RoundedCornerShape(
            topStart = DimensDP.DP16.dp, topEnd = DimensDP.DP16.dp,
            bottomStart = DimensDP.DP16.dp, bottomEnd = DimensDP.DP6.dp
        )
    } else {
        RoundedCornerShape(
            topStart = DimensDP.DP16.dp, topEnd = DimensDP.DP16.dp,
            bottomStart = DimensDP.DP6.dp, bottomEnd = DimensDP.DP16.dp
        )
    }

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = align
    ) {
        Box(
            modifier = Modifier
                .background(background, shape)
                .padding(
                    start = DimensDP.DP12.dp,
                    end = DimensDP.DP12.dp,
                    top = DimensDP.DP8.dp,
                    bottom = DimensDP.DP8.dp
                )
                .widthIn(max = DimensDP.DP280.dp)
        ) {
            Text(
                text = message.text,
                color = textColor,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Start
            )
        }
    }
}