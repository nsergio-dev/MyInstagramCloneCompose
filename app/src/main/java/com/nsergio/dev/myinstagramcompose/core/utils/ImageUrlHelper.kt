package com.nsergio.dev.myinstagramcompose.core.utils

import com.nsergio.dev.myinstagramcompose.BuildConfig
import javax.inject.Inject
import kotlin.random.Random
import kotlin.random.nextInt

class ImageUrlHelper @Inject constructor() {

    private val widths = listOf(480, 540, 600)
    private val heights = listOf(520, 680, 860, 1040)

    fun createImageUrl(): ImageUrl {

        val width = widths.random()
        val height = heights.random()
        val seed = Random.nextInt(1000, 9999)

        val baseUrl = BuildConfig.PICSUM_BASE_URL
        val url = "${baseUrl}/$seed/$width/$height"

        return ImageUrl(url, width, height, seed)
    }

    fun createRandomUserImageUrl(femaleImage: Boolean): String {
        val seed = Random.nextInt(1..100)
        val baseUrl = BuildConfig.RANDOM_USER_BASE_URL
        val imageGender = if (femaleImage) {
            "women"
        } else {
            "men"
        }

        val url = "${baseUrl}/$imageGender/${seed}.jpg"

        return url
    }
}

data class ImageUrl(
    val url: String,
    val width: Int,
    val height: Int,
    val seed: Int
)