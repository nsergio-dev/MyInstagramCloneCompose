package com.nsergio.dev.myinstagramcompose.features.profile.data

import com.nsergio.dev.myinstagramcompose.TestUtils
import com.nsergio.dev.myinstagramcompose.features.BaseTest
import com.nsergio.dev.myinstagramcompose.features.common.fakeUsers
import com.nsergio.dev.myinstagramcompose.features.profile.domain.model.UserId
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test


class FakeUserRepositoryTest: BaseTest() {

    lateinit var repository: FakeUserRepository

    override fun onStart() {
        super.onStart()
        repository = FakeUserRepository()
    }

    @Test
    fun `Validate getUser function`() {
        fakeUsers.clear()
        val value = repository.getUser(UserId("123"))
        assertNull(value)
    }

    @Test
    fun `Validate getUser null with fakeUsers not empty`() {
        val userId = UserId("3")
        fakeUsers.add(TestUtils.user.copy(id = userId))
        val value = repository.getUser(UserId("123"))
        assertNull(value)
        fakeUsers.clear()
    }

    @Test
    fun `Validate getUser function not null`() {
        val userId = UserId("123")
        fakeUsers.add(TestUtils.user.copy(id = userId))
        val value = repository.getUser(userId)
        assertNotNull(value)
        fakeUsers.clear()
    }

    @Test
    fun `Validate getRandomUser`() {
        val userId = UserId("123")
        fakeUsers.add(TestUtils.user.copy(id = userId))
        val user = repository.getRandomUser()
        assertNotNull(user)
    }

    @Test
    fun `Validate getRandomUser number`() {
        val userId = UserId("123")
        fakeUsers.add(TestUtils.user.copy(id = userId))
        fakeUsers.add(TestUtils.user.copy(id = UserId("1232")))
        val user = repository.getRandomUser(1)
        assertNotNull(user)
        assert(user.count() == 1)
        fakeUsers.clear()

    }

    @Test
    fun `Validate getRandomUser number bigger than fakeUsers`() {
        fakeUsers.clear()
        val userId = UserId("123")
        fakeUsers.add(TestUtils.user.copy(id = userId))
        fakeUsers.add(TestUtils.user.copy(id = UserId("1232")))
        val user = repository.getRandomUser(6)
        assertNotNull(user)
        assert(user.count() == 2)
        fakeUsers.clear()

    }

}