package com.nsergio.dev.myinstagramcompose.features.chat.data

import com.nsergio.dev.myinstagramcompose.core.utils.ImageUrlHelper
import com.nsergio.dev.myinstagramcompose.features.chat.domain.ChatRepository
import com.nsergio.dev.myinstagramcompose.features.chat.models.ChatItem
import com.nsergio.dev.myinstagramcompose.features.chat.models.ChatStory
import com.nsergio.dev.myinstagramcompose.features.common.FakeNameUser
import com.nsergio.dev.myinstagramcompose.features.profile.data.FakeUserRepository
import javax.inject.Inject
import kotlin.random.Random
import kotlin.random.nextInt

class ChatRepositoryImpl @Inject constructor(
    private val userRepository: FakeUserRepository,
    private val imageUrlHelper: ImageUrlHelper
) : ChatRepository {

    override suspend fun getStories(): List<ChatStory> {
        // Datos simulados (ya luego conectamos a backend/Room)
        val randomSizeList = Random.nextInt(5..10)
        val captionsList = listOf(
            "Feeling blessed and grateful …",
            "Work hard, play harder 🎉",
            "Just finished a great workout…",
            "Life’s too short for bad vibes ✌️",
        )
        val chatHistoryList = List(randomSizeList) { index ->
            val isFemale = Random.nextBoolean()
            val url = imageUrlHelper.createRandomUserImageUrl(isFemale)
            val name = FakeNameUser.getFullName(isFemale)
            ChatStory(
                userId = index.toString(),
                username = name.realName,
                photoUrl = url,
                caption = captionsList.random(),
                isOnline = Random.nextBoolean()
            )

        }

        return chatHistoryList
    }

    override suspend fun getMessages(): List<ChatItem> {
        val range = Random.nextInt(10..20)
        val users = userRepository.getRandomUser(range)
        val chatItems = users.map { user ->
            ChatItem(
                userId = user.id.value,
                username = user.name,
                photoUrl = user.avatarUrl,
                isOnline = Random.nextBoolean()
            )
        }
        return chatItems
    }
}