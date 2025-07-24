package com.nsergio.dev.myinstagramcompose.core.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontWeight
import com.nsergio.dev.myinstagramcompose.core.ui.DimensDP

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(
    currentPage: Int,
    onClickDropdown: () -> Unit = {},
    onClickNotifications: () -> Unit = {},
    onClickMessages: () -> Unit = {}
) {

    if (currentPage != MainPagerPage.Feed.ordinal) return

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = DimensDP.DP16.dp, vertical = DimensDP.DP12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { onClickDropdown() }
        ) {
            Text(
                text = "For You",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = "Cambiar sección"
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(DimensDP.DP16.dp)) {
            Icon(
                imageVector = Icons.Outlined.FavoriteBorder,
                contentDescription = "Notificaciones",
                modifier = Modifier.clickable { onClickNotifications() }
            )
            Icon(
                imageVector = Icons.Outlined.Send,
                contentDescription = "Mensajes",
                modifier = Modifier
                    .rotate(-30f)
                    .clickable { onClickMessages() }
            )
        }
    }
}