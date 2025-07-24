package com.nsergio.dev.myinstagramcompose.features.chat.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.nsergio.dev.myinstagramcompose.R
import com.nsergio.dev.myinstagramcompose.core.ui.DimensDP
import com.nsergio.dev.myinstagramcompose.core.ui.DimensSp

@Composable
fun ChatTopAppBar(
    username: String,
    onClickBack: () -> Unit,
    onClickVideo: () -> Unit = {},
    onClickNewChat: () -> Unit = {}
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = DimensDP.DP16.dp, vertical = DimensDP.DP12.dp)
            .background(MaterialTheme.colorScheme.background),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Back",
            modifier = Modifier.clickable { onClickBack() }
        )
        Spacer(Modifier.width(DimensDP.DP24.dp))

        Text(username, fontWeight = FontWeight.Bold, fontSize = DimensSp.SP20.sp)
        Icon(Icons.Default.KeyboardArrowDown, contentDescription = null)
        Spacer(Modifier.weight(1f))

        Icon(
            painter = painterResource(id = R.drawable.ic_discover_channel_24),
            contentDescription = null,
            modifier = Modifier.clickable {
                onClickNewChat.invoke()
            }
        )
        Spacer(Modifier.width(DimensDP.DP24.dp))
        Icon(
            painter = painterResource(id = R.drawable.ic_new_message_24),
            contentDescription = null,
            modifier = Modifier.clickable {
                onClickVideo.invoke()
            }
        )
    }
}