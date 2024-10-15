package com.divine.journey.posts.di

import com.divine.journey.posts.data.service.PostService
import com.divine.journey.posts.data.source.PostsRemoteDataSource
import com.divine.journey.posts.data.source.PostsRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

// This module is installed in the SingletonComponent, ensuring application-wide singletons.
@Module(includes = [PostRemoteModule.Binders::class])
@InstallIn(SingletonComponent::class)
class PostRemoteModule {
    @Module
    @InstallIn(SingletonComponent::class)
    interface Binders {

        /**
         * Binds the implementation of PostRemoteDataSource to its interface.
         *
         * @param remoteDataSourceImpl The concrete implementation of PostRemoteDataSource.
         * @return An instance of PostRemoteDataSource.
         */

        @Binds
        fun bindsRemoteSource(
            remoteDataSourceImpl: PostsRemoteDataSourceImpl
        ): PostsRemoteDataSource
    }

    /**
     * Provides an instance of PostsService.
     *
     * @param retrofit The Retrofit instance used to create the service.
     * @return An implementation of PostsService.
     */

    @Provides
    fun providePostService(retrofit: Retrofit): PostService =
        retrofit.create(PostService::class.java)
}