package com.nsergio.dev.myinstagramcompose.features.feed.presentation

import androidx.lifecycle.ViewModel
import com.nsergio.dev.myinstagramcompose.features.feed.domain.usecase.GetPostsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    getPosts: GetPostsUseCase
) : ViewModel() {

    /**
     * Emits PagingData where each Post knows whether it's liked.
     * The underlying paging stream is still collected only once.
     */
    val posts = getPosts.invoke()

}