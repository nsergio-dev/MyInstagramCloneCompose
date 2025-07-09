package com.nsergio.dev.myinstagramcompose.features.profile.data

import com.nsergio.dev.myinstagramcompose.features.profile.domain.model.User
import javax.inject.Inject
import kotlin.random.Random

/**
 * Fake repository that returns a single hard-coded user.
 */
class FakeUserRepository @Inject constructor() {

    /**
     * Returns mock user data.
     *
     * @param userId Id of the user to load
     */
    fun getUser(userId: String): User = User(
        id = userId,
        name = "user_$userId",
        avatarUrl = "https://i.pravatar.cc/300?u=$userId",
        bio = "Just a mock bio for $userId. Lorem ipsum dolor sit amet…",
        posts = Random.nextInt(30, 300),
        followers = Random.nextInt(1_000, 20_000),
        following = Random.nextInt(100, 900)
    )

}