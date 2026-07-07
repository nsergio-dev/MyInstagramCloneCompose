package com.nsergio.dev.myinstagramcompose.features.chat.data

import com.nsergio.dev.myinstagramcompose.core.utils.Utils
import com.nsergio.dev.myinstagramcompose.features.chat.models.Message
import javax.inject.Inject
import kotlin.random.Random

class FakeMessageRepository @Inject constructor() {

    private val loremWords = listOf(
        "lorem", "ipsum", "dolor", "sit", "amet", "consectetur",
        "adipiscing", "elit", "sed", "do", "eiusmod", "tempor",
        "incididunt", "ut", "labore", "et", "dolore", "magna", "aliqua",
        "lorem", "ipsum", "dolor", "sit", "amet", "consectetur",
        "adipiscing", "elit", "sed", "do", "eiusmod", "tempor",
        "incididunt", "ut", "labore", "et", "dolore", "magna", "aliqua",
        "enim", "ad", "minim", "veniam", "quis", "nostrud", "exercitation",
        "ullamco", "laboris", "nisi", "aliquip", "ex", "ea", "commodo", "consequat"
    )

    fun getRandomMessages(count: Int = 20): List<Message> {
        return (1..count).map {
            val wordCount = Random.nextInt(3, 30)
            val text = (1..wordCount).joinToString(" ") {
                loremWords.random()
            }
            Message(
                id = Utils.generateRandomString(),
                text = text,
                fromMe = Random.nextBoolean()
            )
        }
    }
    fun getRandomMessage(): String {
        val words = Random.nextInt(3, 40) // 3..39 palabras
        val base = (1..words).joinToString(" ") { loremWords.random() }
        val sentence = base.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
        return if (Random.nextBoolean()) "$sentence." else "$sentence!"
    }
}