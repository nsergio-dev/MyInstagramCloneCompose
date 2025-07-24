package com.nsergio.dev.myinstagramcompose.features.chat.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun ChatScreen(
    username: String = "mike_williams",
    onClickBack: () -> Unit,
    onClickVideo: () -> Unit = {},
    onClickNewChat: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            ChatTopAppBar(
                username = username,
                onClickBack = onClickBack,
                onClickVideo = onClickVideo,
                onClickNewChat = onClickNewChat
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Aquí irá el contenido del chat")
        }
    }
}