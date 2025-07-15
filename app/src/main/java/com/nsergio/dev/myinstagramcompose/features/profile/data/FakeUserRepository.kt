package com.nsergio.dev.myinstagramcompose.features.profile.data

import com.nsergio.dev.myinstagramcompose.features.common.fakeUsers
import com.nsergio.dev.myinstagramcompose.features.profile.domain.model.User
import com.nsergio.dev.myinstagramcompose.features.profile.domain.model.UserId
import javax.inject.Inject

/**
 * Fake repository that returns a single hard-coded user.
 */
class FakeUserRepository @Inject constructor() {

    /**
     * Returns mock user data.
     *
     * @param userId Id of the user to load
     */
    fun getUser(userId: UserId): User? {
        val user = fakeUsers.find { it.id == userId }
        return user
    }

}