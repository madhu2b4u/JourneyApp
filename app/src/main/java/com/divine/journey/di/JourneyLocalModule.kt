package com.divine.journey.di

import android.app.Application
import com.divine.journey.database.dao.JourneyDatabase
import com.divine.journey.database.source.LocalDataSource
import com.divine.journey.database.source.LocalDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module(includes = [JourneyLocalModule.Binders::class])
@InstallIn(SingletonComponent::class)
class JourneyLocalModule {
    @Module
    @InstallIn(SingletonComponent::class)
    interface Binders {
        @Binds
        fun bindsLocalDataSource(
            localDataSourceImpl: LocalDataSourceImpl
        ): LocalDataSource
    }

    @Provides
    @Singleton
    fun providesDatabase(
        application: Application
    ) = JourneyDatabase.getInstance(application.applicationContext)

    @Provides
    @Singleton
    fun providesDao(
        data: JourneyDatabase
    ) = data.getJourneyDao()
}