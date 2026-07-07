package com.nsergio.dev.myinstagramcompose

import com.nsergio.dev.myinstagramcompose.core.ui.components.StoryItem
import com.nsergio.dev.myinstagramcompose.core.ui.components.StoryRing
import com.nsergio.dev.myinstagramcompose.features.chat.models.ChatItem
import com.nsergio.dev.myinstagramcompose.features.chat.models.ChatStory
import com.nsergio.dev.myinstagramcompose.features.feed.domain.model.PostId
import com.nsergio.dev.myinstagramcompose.features.feed.domain.model.PostWithMedia
import com.nsergio.dev.myinstagramcompose.features.profile.domain.model.User
import com.nsergio.dev.myinstagramcompose.features.profile.domain.model.UserId

object TestUtils {
    val user = User(
        id = UserId("test"),
        name = "nickName",
        realName = "realName",
        avatarUrl = "avatarUrl",
        bio = "Just a mock ",
        followers = 1,
        following = 1
    )
    val userMe = User(
        id = UserId("me"),
        name = "nickName",
        realName = "realName",
        avatarUrl = "avatarUrl",
        bio = "Just a mock ",
        followers = 1,
        following = 1
    )
    val chatStory = ChatStory(
        userId = "userId",
        username = "username",
        photoUrl = "photoUrl",
        caption = "caption",
        isOnline = true
    )
    val chatItem = ChatItem(
        userId = "userId",
        username = "username",
        photoUrl = "photoUrl",
        isOnline = true
    )

    val postWithMedia = PostWithMedia(
        id = PostId("test"),
        authorId = user.id,
        authorName = user.name,
        authorAvatarUrl = user.avatarUrl,
        media = listOf(),
        caption = "caption",
        likeCount = 1,
        commentCount = 2,
        shareCount = 1,
        createdAt = 11L
    )
    val storyItem = StoryItem(
        idHistory = PostId("d"),
        postId = PostId("post1"),
        username = "user1",
        avatarUrl = "avatar1",
        ring = StoryRing.NONE
    )
}