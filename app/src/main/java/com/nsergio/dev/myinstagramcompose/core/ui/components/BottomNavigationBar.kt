package com.nsergio.dev.myinstagramcompose.core.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import coil.compose.rememberAsyncImagePainter
import com.nsergio.dev.myinstagramcompose.core.ui.DimensDP

@Composable
fun BottomNavigationBar(
    avatarUserUrl: String,
    currentPage: Int,
    onSelectedPage: (Int) -> Unit,
    onExploreClick: () -> Unit = {},
    onCreatePostClick: () -> Unit = {},
    onReelsClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {

    if (currentPage != MainPagerPage.Feed.ordinal) return

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.background
    ) {
        NavigationBarItem(
            selected = currentPage == 0,
            onClick = { onSelectedPage.invoke(0) },
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") }
        )
        NavigationBarItem(
            selected = currentPage == 1,
            onClick = { onExploreClick.invoke() },
            icon = { Icon(Icons.Default.Search, contentDescription = "Search") }
        )
        NavigationBarItem(
            selected = currentPage == 2,
            onClick = { onCreatePostClick.invoke() },
            icon = { Icon(Icons.Default.AddCircleOutline, contentDescription = "More") }
        )
        NavigationBarItem(
            selected = currentPage == 3,
            onClick = { onReelsClick.invoke() },
            icon = { Icon(Icons.Default.VideoLibrary, contentDescription = "Reels") }
        )
        NavigationBarItem(
            selected = currentPage == 4,
            onClick = {
                //onSelectedPage.invoke(4)
                onProfileClick.invoke()
            },
            icon = {
                Image(
                    painter = rememberAsyncImagePainter(avatarUserUrl),
                    contentDescription = "Profile",
                    modifier = Modifier
                        .size(DimensDP.DP24.dp)
                        .clip(CircleShape)
                )
            }
        )
    }
}