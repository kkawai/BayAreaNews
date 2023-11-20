package com.kk.android.bayareanews.di

import com.kk.android.bayareanews.data.remote.RssApi
import com.kk.android.bayareanews.data.repository.RssRepositoryImpl
import com.kk.android.bayareanews.domain.repository.RssRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesRssApi(): RssApi {
        TODO()
    }

    @Provides
    @Singleton
    fun providesRssRepository(rssApi: RssApi): RssRepository {
        return RssRepositoryImpl(rssApi)
    }
}