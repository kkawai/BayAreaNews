package com.kk.android.bayareanews.domain.use_case.get_rss

import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.get
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.kk.android.bayareanews.NewsReaderApp
import com.kk.android.bayareanews.common.Constants
import com.kk.android.bayareanews.common.MLog
import com.kk.android.bayareanews.common.Resource
import com.kk.android.bayareanews.data.local.RssLocalDbHelper
import com.kk.android.bayareanews.domain.model.Rss
import com.kk.android.bayareanews.domain.model.RssFeedHolder
import com.kk.android.bayareanews.domain.repository.RssRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import java.util.Date
import javax.inject.Inject

class GetFeaturedUseCase @Inject constructor(private val rssRepository: RssRepository) {

    operator fun invoke( refresh: Boolean): Flow<Resource<RssFeedHolder>> = flow {

        MLog.i("nnnnn","GetFeaturedUseCase waiting for remote config")
        val remoteConfigResponse = NewsReaderApp.app.remoteConfigResponse.await()
        MLog.i("nnnnn","GetFeaturedUseCase received remote config")
        if (!remoteConfigResponse) {
            emit(Resource.Error("Error: Failed to get remote config "))
            return@flow
        }
        val originalCategory = Firebase.remoteConfig[Constants.FEATURED_CATEGORIES].asString()
        if (originalCategory.isBlank()) {
            emit(Resource.Error("Error: Failed to get featured category from remote config "))
            return@flow
        }
        val rssUrlFromConfig = Firebase.remoteConfig[originalCategory.trim()].asString()
        if (rssUrlFromConfig.isBlank()) {
            emit(Resource.Error("Error: Failed to get featured category url from remote config "))
            return@flow
        }

        try {
            emit(Resource.Loading())
            val rssFeedHolder = rssRepository.getFeaturedArticles(refresh, rssUrlFromConfig, originalCategory)
            emit(Resource.Success(rssFeedHolder))
        } catch (e: IOException) {
            emit(Resource.Error("Internet connection error.  Please try again: " + e))
        } catch (e: Exception) {
            emit(Resource.Error("Unexpected error occurred.  Pls try again: " + e))
        }

    }
}