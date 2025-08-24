package com.nsergio.dev.myinstagramcompose.features.common

import com.nsergio.dev.myinstagramcompose.core.utils.ImageUrlHelper
import com.nsergio.dev.myinstagramcompose.core.utils.Utils
import com.nsergio.dev.myinstagramcompose.features.feed.domain.model.Media
import com.nsergio.dev.myinstagramcompose.features.feed.domain.model.PostId
import com.nsergio.dev.myinstagramcompose.features.feed.domain.model.PostWithMedia
import com.nsergio.dev.myinstagramcompose.features.profile.domain.model.User
import com.nsergio.dev.myinstagramcompose.features.profile.domain.model.UserId
import kotlin.random.Random

val fakeUsers = mutableListOf<User>()

fun insertUsersWithMedia(numberOfUsers: Int) {
    repeat(numberOfUsers) {
        val userWithPost = createUserWithMedia()
        if (!fakeUsers.contains(userWithPost)) {
            fakeUsers.add(userWithPost)
        }
    }
}

fun createMeUser() {

    val userId = UserId("me")

    val user = createUser(userId)

    val postByUser = getListMedia(user)

    val userWitMedia = user.copy(posts = postByUser)

    if (!fakeUsers.contains(userWitMedia)) {
        fakeUsers.add(userWitMedia)
    }

}

fun createUserWithMedia(): User {

    val userId = UserId(Utils.generateRandomString())
    val user = createUser(userId)

    val postByUser = getListMedia(user)

    val userWitMedia = user.copy(posts = postByUser)
    return userWitMedia
}

private fun getListMedia(
    user: User
): List<PostWithMedia> {
    val isRandom = Random.nextBoolean()
    val sizeMedia = if (isRandom) 30 else 100
    val urlHelper = ImageUrlHelper()
    val postWithMedia = List(sizeMedia) { indexPost ->
        val postId = PostId("post_id_${user.id.value}_$indexPost")

        val imageCount = Random.nextInt(1, 6)
        val media = List(imageCount) { indexMedia ->

            val url = urlHelper.createImageUrl()
            Media(url = url.url)
        }

        val caption =
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque ut dignissim est. Proin ut nisi ut lacus volutpat mollis quis a odio. Donec et tellus feugiat, efficitur urna ultrices, mattis orci. Nunc augue felis, viverra eget sagittis vel, tristique at lacus."

        PostWithMedia(
            id = postId,
            authorId = user.id,
            authorName = user.name,
            authorAvatarUrl = user.avatarUrl,
            media = media,
            caption = caption,
            likeCount = Random.nextInt(10, 1_000),
            commentCount = Random.nextInt(0, 200),
            shareCount = Random.nextInt(0, 100),
            createdAt = Utils.generateRandomFloat() -
                    Random.nextLong(MILLIS_IN_HOUR, 7 * MILLIS_IN_DAY + 1)
        )
    }

    return postWithMedia
}

fun createUser(userId: UserId): User {
    val isFemale = Random.nextBoolean()
    val userName = FakeNameUser.getFullName(isFemale)
    val urlHelper = ImageUrlHelper()
    val avatarUrl = urlHelper.createRandomUserImageUrl(isFemale)

    return User(
        id = userId,
        name = userName.nickName,
        realName = userName.realName,
        avatarUrl = avatarUrl,
        bio = "Just a mock bio for Name ${userId.value}. Lorem ipsum dolor sit amet…",
        followers = Random.nextInt(1_000, 20_000),
        following = Random.nextInt(100, 900)
    )
}

private const val MILLIS_IN_MIN = 60_000L
private const val MILLIS_IN_HOUR = 60 * MILLIS_IN_MIN        // 3_600_000
private const val MILLIS_IN_DAY = 24 * MILLIS_IN_HOUR
