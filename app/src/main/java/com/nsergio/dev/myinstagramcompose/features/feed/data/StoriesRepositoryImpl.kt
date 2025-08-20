package com.nsergio.dev.myinstagramcompose.features.feed.data

import com.nsergio.dev.myinstagramcompose.core.ui.components.StoryItem
import com.nsergio.dev.myinstagramcompose.core.ui.components.StoryRing
import com.nsergio.dev.myinstagramcompose.features.profile.data.FakeUserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import kotlin.random.Random
import kotlin.random.nextInt

class StoriesRepositoryImpl @Inject constructor(
    private val userRepository: FakeUserRepository
) : StoriesRepository {

    override fun getStoriesFlow(): Flow<List<StoryItem>> = flow {
        val sizeList = Random.nextInt(10..20)
        val users = userRepository.getRandomUser(sizeList)
        val stories = mutableListOf<StoryItem>()
        users.forEachIndexed { index, user ->
            val post = user.posts.random()
            stories.add(
                StoryItem(
                    idHistory = post.id,
                    postId = post.id,
                    username = user.name,
                    avatarUrl = user.avatarUrl,
                    ring = StoryRing.entries.random()
                )
            )

        }
        val sortedList = stories.sortedByDescending { it.ring }
        emit(sortedList)
    }

}