package com.nsergio.dev.myinstagramcompose.features.explore.data

import com.nsergio.dev.myinstagramcompose.core.utils.ImageUrl
import com.nsergio.dev.myinstagramcompose.core.utils.ImageUrlHelper
import com.nsergio.dev.myinstagramcompose.features.BaseTest
import com.nsergio.dev.myinstagramcompose.features.explore.domain.model.ExplorePhoto
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test


class ExploreRepositoryImplTest : BaseTest() {

    @RelaxedMockK
    lateinit var imageUrlHelper: ImageUrlHelper

    private lateinit var exploreRepository: ExploreRepositoryImpl

    override fun onStart() {
        super.onStart()
        exploreRepository = ExploreRepositoryImpl(imageUrlHelper)
    }

    @Test
    fun `Validate calling getPhotosFlow`() = runTest {
        every {
            imageUrlHelper.createImageUrl()
        } returns ImageUrl(url = "url", width = 1, height = 1, seed = 1)

        val photos = exploreRepository.getPhotosFlow().first()

        assert(photos.isNotEmpty())

    }
}