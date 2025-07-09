package com.nsergio.dev.myinstagramcompose.features.profile.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.nsergio.dev.myinstagramcompose.core.ui.DimensDP
import com.nsergio.dev.myinstagramcompose.core.ui.components.CircularAvatar
import com.nsergio.dev.myinstagramcompose.core.utils.clickableNoRipple
import com.nsergio.dev.myinstagramcompose.features.profile.domain.model.User
import com.nsergio.dev.myinstagramcompose.features.profile.presentation.ProfileViewModel

/**
 * Parent composable that renders the user profile screen.
 */
@Composable
fun ProfileScreen(
    contentPadding: PaddingValues,
    userId: String,
    viewModel: ProfileViewModel = hiltViewModel()
) {

    LaunchedEffect(userId) {
        viewModel.setUserId(userId)
    }

    /* Reactively collect user */
    val user by viewModel.user.collectAsState()

    user?.let { safeUser ->

        Column(modifier = Modifier.fillMaxSize().padding(paddingValues = contentPadding)) {
            ProfileHeader(user = safeUser)
            Spacer(Modifier.height(DimensDP.DP16.dp))
            PostGrid(userId = safeUser.id)          // grid de fotos mock
        }

    }
}

/** Header with avatar, stats and bio. */
@Composable
private fun ProfileHeader(user: User) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(DimensDP.DP16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularAvatar(imageUrl = user.avatarUrl)
        Spacer(Modifier.height(DimensDP.DP12.dp))
        UsernameText(name = user.name)
        Spacer(Modifier.height(DimensDP.DP8.dp))
        StatsRow(
            posts = user.posts,
            followers = user.followers,
            following = user.following
        )
        Spacer(Modifier.height(DimensDP.DP8.dp))
        BioText(text = user.bio)
    }
}

/**
 * Username label.
 *
 * @param name Username to display
 */
@Composable
fun UsernameText(name: String) {
    Text(
        text = name,
        style = MaterialTheme.typography.bodyMedium
    )
}

/** Row with posts / followers / following. */
@Composable
private fun StatsRow(posts: Int, followers: Int, following: Int) {
    Row(horizontalArrangement = Arrangement.spacedBy(DimensDP.DP24.dp)) {
        StatItem(label = "Posts", value = posts)
        StatItem(label = "Followers", value = followers)
        StatItem(label = "Following", value = following)
    }
}

/** Single stat item. */
@Composable
private fun StatItem(label: String, value: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value.toString(),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
        Text(text = label, style = MaterialTheme.typography.labelSmall)
    }
}

/** Bio paragraph. */
@Composable
private fun BioText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall,
        modifier = Modifier.padding(horizontal = DimensDP.DP16.dp)
    )
}

/**
 * Grid of square images for a profile.
 *
 * @param userId Id used to seed the mock images
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PostGrid(userId: String) {
    val count = 60 // mock 60 images
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(DimensDP.DP1.dp)
    ) {
        items(count) { index ->
            val url = "https://picsum.photos/seed/${userId}_$index/300/300"
            AsyncImage(
                model = url,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .aspectRatio(1f)
                    .clickableNoRipple { /* TODO open detail */ }
            )
        }
    }
}