package com.nsergio.dev.myinstagramcompose.core.ui.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.nsergio.dev.myinstagramcompose.features.chat.ui.ChatScreen
import com.nsergio.dev.myinstagramcompose.features.feed.ui.FeedScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MainPagerScreen(
    onClickProfile: (String) -> Unit,
    onClickStory: (String) -> Unit
) {
    val pagerState = rememberPagerState(
        initialPage = MainPagerPage.Feed.ordinal,
        pageCount = { MainPagerPage.entries.count() }
    )

    val scope = rememberCoroutineScope()

    BackHandler(enabled = pagerState.currentPage != MainPagerPage.Feed.ordinal) {
        scope.launch {
            delay(150)
            pagerState.animateScrollToPage(MainPagerPage.Feed.ordinal)
        }
    }

    Scaffold(

        topBar = {

            MyTopAppBar(
                currentPage = pagerState.currentPage,
                onClickMessages = {
                    scope.launch {
                        pagerState.animateScrollToPage(MainPagerPage.Chat.ordinal)
                    }
                },
            )

        },
        bottomBar = {
            BottomNavigationBar(
                currentPage = pagerState.currentPage,
                onSelectedPage = { index ->
                    scope.launch {
                        delay(150)
                        pagerState.animateScrollToPage(index)
                    }
                },
                onProfileClick = {
                    onClickProfile.invoke("me")
                }
            )
        }
    ) { innerPadding ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            MainPagerController(
                page = page,
                innerPadding = innerPadding,
                onClickProfile = onClickProfile,
                onClickStory = onClickStory,
                onClickBack = {
                    scope.launch {
                        delay(150)
                        pagerState.animateScrollToPage(MainPagerPage.Feed.ordinal)
                    }
                }
            )
        }
    }
}

@Composable
private fun MainPagerController(
    innerPadding: PaddingValues,
    page: Int,
    onClickProfile: (String) -> Unit,
    onClickBack: () -> Unit,
    onClickStory: (String) -> Unit,
) {
    when (page) {

        MainPagerPage.Feed.ordinal -> FeedScreen(
            contentPadding = innerPadding,
            onClickProfile = onClickProfile,
            onClickStory = { storyItem ->
                onClickStory.invoke(storyItem.postId.value)
            }
        )

        MainPagerPage.Chat.ordinal -> ChatScreen(
            username = "jhon_doe",
            onClickBack = onClickBack,
        )
    }
}