package com.nsergio.dev.myinstagramcompose.features.feed.data

import androidx.paging.PagingData
import androidx.paging.testing.asSnapshot
import com.nsergio.dev.myinstagramcompose.features.feed.domain.model.PostWithMedia
import com.nsergio.dev.myinstagramcompose.features.profile.domain.model.UserId
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FakePostRepositoryTest {

    private val repo = FakePostRepository()

    @Test
    fun `getPosts devuelve datos (snapshot inicial)`() = runTest {
        val flow: Flow<PagingData<PostWithMedia>> = repo.getPosts()

        // Toma un snapshot de los ítems cargados (refresh inicial + prefetch).
        val items: List<PostWithMedia> = flow.asSnapshot()

        assertTrue(items.isNotEmpty())
        assertNotNull(items.first().id)
    }

    @Test
    fun `findById encuentra el post obtenido del snapshot`() = runTest {
        val items = repo.getPosts().asSnapshot()
        assertTrue(items.isNotEmpty())

        val target = items.first()
        val found = repo.findById(target.id)

        assertNotNull(found)
        assertEquals(target.id, found!!.id)
    }

    @Test
    fun `postsOf devuelve vacio para autor desconocido`() = runTest {
        val result = repo.postsOf(UserId("USER_UNKNOWN_999"))
        assertTrue(result.isEmpty())
    }
}