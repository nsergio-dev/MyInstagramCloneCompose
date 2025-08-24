package com.nsergio.dev.myinstagramcompose.features.explore.data

import com.nsergio.dev.myinstagramcompose.core.utils.ImageUrlHelper
import com.nsergio.dev.myinstagramcompose.features.explore.domain.model.ExplorePhoto
import com.nsergio.dev.myinstagramcompose.features.explore.domain.repository.ExploreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ExploreRepositoryImpl @Inject constructor(
    private val imageUrlHelper: ImageUrlHelper
) : ExploreRepository {

    override fun getPhotosFlow(): Flow<List<ExplorePhoto>> = flow {

        val photos = List(60) { index ->

            val url = imageUrlHelper.createImageUrl()

            ExplorePhoto(
                id = "${url.seed}-$index",
                url = url.url,
                width = url.width,
                height = url.height
            )
        }
        emit(photos)
    }
}