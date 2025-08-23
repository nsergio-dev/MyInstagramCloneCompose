package com.nsergio.dev.myinstagramcompose.features.explore.domain.repository

import com.nsergio.dev.myinstagramcompose.features.explore.domain.model.ExplorePhoto
import kotlinx.coroutines.flow.Flow

interface ExploreRepository {
    fun getPhotosFlow(): Flow<List<ExplorePhoto>>
}