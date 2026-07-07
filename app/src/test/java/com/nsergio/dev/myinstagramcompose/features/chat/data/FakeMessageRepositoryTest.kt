package com.nsergio.dev.myinstagramcompose.features.chat.data

import com.nsergio.dev.myinstagramcompose.core.utils.Utils
import com.nsergio.dev.myinstagramcompose.features.BaseTest
import io.mockk.every
import io.mockk.mockkObject
import org.junit.Test
import kotlin.random.Random


class FakeMessageRepositoryTest: BaseTest() {

    lateinit var repository: FakeMessageRepository

    override fun onStart() {
        super.onStart()

        mockkObject(Random)

        mockkObject(Random.Default)

        mockkObject(Utils)

        repository = FakeMessageRepository()
    }

    @Test
    fun `Validate getMessages and getRandomMessage return not empty data`() {

        every { Random.nextInt(3, 30) } returns 5

        every { Random.Default.nextInt(any()) } returns 2

        every { Random.nextBoolean() } returns true

        every { Utils.generateRandomString() } returnsMany listOf("id1", "id2", "id3")

        val result = repository.getRandomMessages()

        val message = repository.getRandomMessage()

        assert(result.isNotEmpty())
        assert(message.isNotEmpty())
    }

}