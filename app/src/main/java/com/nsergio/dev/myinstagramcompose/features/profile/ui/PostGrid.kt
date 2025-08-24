package com.nsergio.dev.myinstagramcompose.features.profile.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import com.nsergio.dev.myinstagramcompose.core.ui.DimensDP
import com.nsergio.dev.myinstagramcompose.core.ui.components.RemoteAsyncImage
import com.nsergio.dev.myinstagramcompose.core.utils.clickableNoRipple
import com.nsergio.dev.myinstagramcompose.features.feed.domain.model.PostWithMedia

/**
 * Grid of square images for a profile.
 *
 * @param userId Id used to seed the mock images
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PostGrid(
    posts: List<PostWithMedia>,
    onClickImageDetail: (userId: String, index: Int) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(DimensDP.DP1.dp)
    ) {
        items(posts) { post ->
            val firsImage = post.media.first()
            UserProfileImage(
                url = firsImage.url,
                onClick = {
                    onClickImageDetail.invoke(post.id.value, 0) //always in firs position
                }
            )
        }
    }
}

@Composable
private fun UserProfileImage(url: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .aspectRatio(1f)
    ) {
        RemoteAsyncImage(
            model = url,
            crossfade = true,
            content = {
                Image(
                    contentDescription = null,
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clickableNoRipple(onClick),
                    contentScale = ContentScale.Crop,
                    painter = it,
                )
            },
            onLoading = {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }
        )
    }
}