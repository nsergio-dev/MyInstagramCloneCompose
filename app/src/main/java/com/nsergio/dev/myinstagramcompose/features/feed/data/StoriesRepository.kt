package com.nsergio.dev.myinstagramcompose.features.feed.data

import com.nsergio.dev.myinstagramcompose.core.ui.components.StoryItem
import kotlinx.coroutines.flow.Flow

fun interface StoriesRepository {

    fun getStoriesFlow(): Flow<List<StoryItem>>

}