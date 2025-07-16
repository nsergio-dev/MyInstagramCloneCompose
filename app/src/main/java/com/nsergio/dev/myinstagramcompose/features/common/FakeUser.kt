package com.nsergio.dev.myinstagramcompose.features.common

import com.nsergio.dev.myinstagramcompose.features.feed.domain.model.Media
import com.nsergio.dev.myinstagramcompose.features.feed.domain.model.PostId
import com.nsergio.dev.myinstagramcompose.features.feed.domain.model.PostWithMedia
import com.nsergio.dev.myinstagramcompose.features.profile.domain.model.User
import com.nsergio.dev.myinstagramcompose.features.profile.domain.model.UserId
import kotlin.random.Random

val fakeUsers = mutableListOf<User>()

fun createUserWithMedia(
    position: Int,
    page: Int
): User {

    val userId = UserId("user_${position}_page_${page}")
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
    val postWithMedia = List(sizeMedia) { indexPost ->
        val postId = PostId("post_id_${user.id.value}_$indexPost")

        val imageCount = Random.nextInt(1, 6)
        val media = List(imageCount) { indexMedia ->
            Media(
                url = "https://picsum.photos/seed/${postId.value}_$indexMedia/1080/1920"
                //url = "https://picsum.photos/seed/${postId.value}_$indexMedia/600/600"
            )
        }

        val caption = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque ut dignissim est. Proin ut nisi ut lacus volutpat mollis quis a odio. Donec et tellus feugiat, efficitur urna ultrices, mattis orci. Nunc augue felis, viverra eget sagittis vel, tristique at lacus."

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
            createdAt = System.currentTimeMillis() -
                    Random.nextLong(MILLIS_IN_HOUR, 7 * MILLIS_IN_DAY + 1)
        )
    }

    return postWithMedia
}

private fun createUser(userId: UserId): User = User(
    id = userId,
    name = "Name ${userId.value}",
    avatarUrl = "https://i.pravatar.cc/150?u=${userId.value}",
    bio = "Just a mock bio for Name ${userId.value}. Lorem ipsum dolor sit amet…",
    followers = Random.nextInt(1_000, 20_000),
    following = Random.nextInt(100, 900)
)

private const val MILLIS_IN_MIN = 60_000L
private const val MILLIS_IN_HOUR = 60 * MILLIS_IN_MIN        // 3_600_000
private const val MILLIS_IN_DAY = 24 * MILLIS_IN_HOUR
