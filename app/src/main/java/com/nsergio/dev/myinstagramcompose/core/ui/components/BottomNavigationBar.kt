package com.nsergio.dev.myinstagramcompose.core.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import coil.compose.rememberAsyncImagePainter
import com.nsergio.dev.myinstagramcompose.core.ui.DimensDP

@Composable
fun BottomNavigationBar(currentPage: Int) {

    var selectedIndex by remember { mutableIntStateOf(0) }

    if (currentPage != MainPagerPage.Feed.ordinal) return

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.background
    ) {
        NavigationBarItem(
            selected = selectedIndex == 0,
            onClick = { selectedIndex = 0 },
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") }
        )
        NavigationBarItem(
            selected = selectedIndex == 1,
            onClick = { selectedIndex = 1 },
            icon = { Icon(Icons.Default.Search, contentDescription = "Search") }
        )
        NavigationBarItem(
            selected = selectedIndex == 2,
            onClick = { selectedIndex = 2 },
            icon = { Icon(Icons.Default.Add, contentDescription = "Add") }
        )
        NavigationBarItem(
            selected = selectedIndex == 3,
            onClick = { selectedIndex = 3 },
            icon = { Icon(Icons.Default.VideoLibrary, contentDescription = "Reels") }
        )
        NavigationBarItem(
            selected = selectedIndex == 4,
            onClick = { selectedIndex = 4 },
            icon = {
                Image(
                    painter = rememberAsyncImagePainter("https://randomuser.me/api/portraits/men/1.jpg"),
                    contentDescription = "Profile",
                    modifier = Modifier
                        .size(DimensDP.DP24.dp)
                        .clip(CircleShape)
                )
            }
        )
    }
}