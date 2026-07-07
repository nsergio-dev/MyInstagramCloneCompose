package com.nsergio.dev.myinstagramcompose.features.feed.data

import com.nsergio.dev.myinstagramcompose.features.feed.domain.model.PostId
import com.nsergio.dev.myinstagramcompose.features.feed.domain.model.PostWithMedia
import com.nsergio.dev.myinstagramcompose.features.profile.domain.model.UserId
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LocalFeedOverlayTest {

    private lateinit var overlay: LocalFeedOverlay

    val post1 = PostWithMedia(
        id = PostId("1"),
        authorId = UserId("1"),
        authorName = "test",
        authorAvatarUrl = "",
        media = emptyList(),
        createdAt = 1,
    )

    @Before
    fun setUp() {
        overlay = LocalFeedOverlay()
    }

    @Test
    fun `headPosts inicia vacio`() = runTest {
        val value = overlay.headPosts.first()
        assertTrue(value.isEmpty())
    }

    @Test
    fun `addAtTop agrega post al inicio`() = runTest {

        val post2 = post1.copy(id = PostId("2"), authorId = UserId("2"))

        overlay.addAtTop(post1)
        overlay.addAtTop(post2)

        val list = overlay.headPosts.first()

        // post2 debería estar en la posición 0
        assertEquals(post2.id, list[0].id)
        assertEquals(post1.id, list[1].id)
    }

    @Test
    fun `clear vacia la lista`() = runTest {
        val post = post1
        overlay.addAtTop(post)

        overlay.clear()

        val list = overlay.headPosts.first()
        assertTrue(list.isEmpty())
    }
    
}