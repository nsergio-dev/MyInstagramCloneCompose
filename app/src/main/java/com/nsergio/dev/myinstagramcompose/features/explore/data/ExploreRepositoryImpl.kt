package com.nsergio.dev.myinstagramcompose.features.explore.data

import com.nsergio.dev.myinstagramcompose.features.explore.domain.model.ExplorePhoto
import com.nsergio.dev.myinstagramcompose.features.explore.domain.repository.ExploreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import kotlin.random.Random

class ExploreRepositoryImpl @Inject constructor() : ExploreRepository {

    override fun getPhotosFlow(): Flow<List<ExplorePhoto>> = flow {
        val widths = listOf(480, 540, 600)
        val heights = listOf(520, 680, 860, 1040)

        val photos = List(60) { index ->
            val w = widths.random()
            val h = heights.random()
            val seed = Random.nextInt(1000, 9999)

            val url = "https://picsum.photos/seed/$seed/$w/$h"

            ExplorePhoto(
                id = "$seed-$index",
                url = url,
                width = w,
                height = h
            )
        }
        emit(photos)
    }
}