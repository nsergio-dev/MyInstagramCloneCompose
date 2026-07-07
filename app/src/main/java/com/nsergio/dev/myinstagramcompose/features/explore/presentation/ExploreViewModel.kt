package com.nsergio.dev.myinstagramcompose.features.explore.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nsergio.dev.myinstagramcompose.features.explore.domain.model.ExplorePhoto
import com.nsergio.dev.myinstagramcompose.features.explore.domain.repository.ExploreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(
    exploreRepository: ExploreRepository
) : ViewModel() {

    val photos: StateFlow<List<ExplorePhoto>> = exploreRepository.getPhotosFlow()
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            emptyList()
        )
}