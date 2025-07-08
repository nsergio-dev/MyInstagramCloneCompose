package com.nsergio.dev.myinstagramcompose.features.feed.presentation


import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.nsergio.dev.myinstagramcompose.features.feed.data.PostPagingSource
import com.nsergio.dev.myinstagramcompose.features.feed.domain.model.Post
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor() : ViewModel() {

    /**
     * Emits PagingData where each Post knows whether it's liked.
     * The underlying paging stream is still collected only once.
     */
    val posts: Flow<PagingData<Post>> = getMockPost()

    private fun getMockPost(): Flow<PagingData<Post>> {

        val pagingConfig = PagingConfig(pageSize = 10)
        return Pager(pagingConfig) {
            PostPagingSource()
        }.flow
    }

}