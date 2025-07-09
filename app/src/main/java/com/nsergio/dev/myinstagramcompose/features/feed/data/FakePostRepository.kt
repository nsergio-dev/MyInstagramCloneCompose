package com.nsergio.dev.myinstagramcompose.features.feed.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.nsergio.dev.myinstagramcompose.features.feed.domain.model.Post
import kotlinx.coroutines.flow.Flow

class FakePostRepository : PostRepository {

    override fun getPosts(): Flow<PagingData<Post>> {
        val pagingConfig = PagingConfig(pageSize = 10)
        return Pager(pagingConfig) {
            PostPagingSource()
        }.flow
    }

}