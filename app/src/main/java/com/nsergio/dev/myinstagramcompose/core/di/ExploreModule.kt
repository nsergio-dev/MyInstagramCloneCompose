package com.nsergio.dev.myinstagramcompose.core.di

import com.nsergio.dev.myinstagramcompose.features.explore.data.ExploreRepositoryImpl
import com.nsergio.dev.myinstagramcompose.features.explore.domain.repository.ExploreRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ExploreModule {

    @Binds
    @Singleton
    abstract fun bindExploreRepository(
        impl: ExploreRepositoryImpl
    ): ExploreRepository

}