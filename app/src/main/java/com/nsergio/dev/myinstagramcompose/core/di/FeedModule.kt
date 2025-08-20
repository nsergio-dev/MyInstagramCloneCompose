package com.nsergio.dev.myinstagramcompose.core.di

import com.nsergio.dev.myinstagramcompose.features.feed.data.FakePostRepository
import com.nsergio.dev.myinstagramcompose.features.feed.data.StoriesRepository
import com.nsergio.dev.myinstagramcompose.features.feed.data.StoriesRepositoryImpl
import com.nsergio.dev.myinstagramcompose.features.feed.data.PostRepository
import com.nsergio.dev.myinstagramcompose.features.feed.domain.usecase.GetPostsUseCase
import com.nsergio.dev.myinstagramcompose.features.profile.data.FakeUserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FeedModule {

    @Provides
    @Singleton
    fun providePostRepository(): PostRepository = FakePostRepository()

    @Provides
    fun provideGetPostsUseCase(repo: PostRepository): GetPostsUseCase {
        return GetPostsUseCase { repo.getPosts() }
    }

    @Provides
    @Singleton
    fun provideHistoriesRepository(
        userRepository: FakeUserRepository
    ): StoriesRepository  {
        return StoriesRepositoryImpl(userRepository)
    }

}