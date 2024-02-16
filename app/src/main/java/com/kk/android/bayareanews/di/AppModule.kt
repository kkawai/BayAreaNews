package com.kk.android.bayareanews.di

import com.kk.android.bayareanews.data.RssApiImpl
import com.kk.android.bayareanews.data.RssApi
import com.kk.android.bayareanews.data.TRApi
import com.kk.android.bayareanews.data.TRHelper
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
        return RssApiImpl()
    }

    @Provides
    @Singleton
    fun providesRssRepository(rssApi: RssApi): RssRepository {
        return RssRepositoryImpl(rssApi)
    }

    @Provides
    @Singleton
    fun providesTRApi(): TRApi {
        return TRHelper()
    }

}

