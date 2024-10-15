package com.divine.journey.posts.di

import com.divine.journey.posts.data.repository.PostsRepository
import com.divine.journey.posts.data.repository.PostsRepositoryImpl
import com.divine.journey.posts.domain.PostsUseCase
import com.divine.journey.posts.domain.PostsUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class PostsDomainModule {

    /**
     * Binds the implementation of PostRepository to its interface.
     *
     * @param repoImpl The concrete implementation of PostRepository.
     * @return An instance of PostRepository.
     */

    @Binds
    internal abstract fun bindRepository(
        repoImpl: PostsRepositoryImpl
    ): PostsRepository

    /**
     * Binds the implementation of PostUseCase to its interface.
     *
     * @param useCaseImpl The concrete implementation of PostUseCase.
     * @return An instance of PostUseCase.
     */


    @Binds
    internal abstract fun bindsUseCase(
        useCaseImpl: PostsUseCaseImpl
    ): PostsUseCase

}