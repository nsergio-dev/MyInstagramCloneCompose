package com.nsergio.dev.myinstagramcompose.features.profile.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import com.nsergio.dev.myinstagramcompose.core.ui.DimensDP
import com.nsergio.dev.myinstagramcompose.core.ui.components.CircularAvatar
import com.nsergio.dev.myinstagramcompose.features.profile.domain.model.User
import com.nsergio.dev.myinstagramcompose.features.profile.presentation.ProfileViewModel

/**
 * Parent composable that renders the user profile screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    userId: String,
    viewModel: ProfileViewModel = hiltViewModel(),
    onClickImageDetail: (userId: String, index: Int) -> Unit,
    onBackClick: () -> Unit
) {

    LaunchedEffect(userId) {
        viewModel.setUserId(userId)
    }

    /* Reactively collect user */
    val user by viewModel.user.collectAsState()

    val posts by viewModel.posts.collectAsState()

    user?.let { safeUser ->
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(safeUser.name) },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Volver"
                            )
                        }
                    },
                )
            }
        ) { innerPadding ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                ProfileHeader(user = safeUser)
                Spacer(Modifier.height(DimensDP.DP16.dp))
                PostGrid(
                    posts = posts,
                    onClickImageDetail = onClickImageDetail
                )
            }
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
            posts = user.posts.count(),
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

