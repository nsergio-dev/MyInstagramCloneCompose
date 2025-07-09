package com.nsergio.dev.myinstagramcompose.core.di

import com.nsergio.dev.myinstagramcompose.features.profile.data.FakeUserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ProfileModule {

    @Provides
    fun provideUserRepository(): FakeUserRepository = FakeUserRepository()
}