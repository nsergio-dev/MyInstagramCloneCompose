package com.nsergio.dev.myinstagramcompose.core.di

import com.nsergio.dev.myinstagramcompose.features.chat.data.ChatRepositoryImpl
import com.nsergio.dev.myinstagramcompose.features.chat.domain.ChatRepository
import com.nsergio.dev.myinstagramcompose.features.chat.usecase.GetChatInboxUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ChatBindModule {

    @Binds
    @Singleton
    abstract fun bindChatRepository(impl: ChatRepositoryImpl): ChatRepository
}

@Module
@InstallIn(SingletonComponent::class)
object ChatProvideModule {

    @Provides
    @Singleton
    fun provideGetChatInboxUseCase(
        repository: ChatRepository
    ): GetChatInboxUseCase = GetChatInboxUseCase(repository)
}