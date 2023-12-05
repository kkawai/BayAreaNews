package com.kk.android.bayareanews.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.get
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.kk.android.bayareanews.NewsReaderApp
import com.kk.android.bayareanews.common.Constants
import com.kk.android.bayareanews.common.MLog
import com.kk.android.bayareanews.data.RssApi
import com.kk.android.bayareanews.data.local.RssLocalDbHelper
import com.kk.android.bayareanews.domain.model.Rss
import com.kk.android.bayareanews.domain.model.RssFeedHolder
import com.kk.android.bayareanews.domain.repository.RssRepository
import java.util.Date
import javax.inject.Inject

class RssRepositoryImpl @Inject constructor(private val rssApi: RssApi) : RssRepository {

    private suspend fun getFeaturedRssArticles(): List<Rss> {

        MLog.i("nnnnn","RssRepositoryImpl waiting for remote config")
        val remoteConfigResponse = NewsReaderApp.app.remoteConfigResponse.await()
        MLog.i("nnnnn","RssRepositoryImpl received remote config")
        if (!remoteConfigResponse) {
            return emptyList()
        }
        val originalCategory = Firebase.remoteConfig[Constants.FEATURED_CATEGORIES].asString()
        if (originalCategory.isBlank()) {
            return emptyList()
        }
        val rssUrl = Firebase.remoteConfig[originalCategory.trim()].asString()
        if (rssUrl.isBlank()) {
            return emptyList()
        }
        if (featuredNeedsRefresh(originalCategory)) {
            val localList = rssApi.getRssArticlesFromLocalDb(originalCategory)
            if (localList.isNotEmpty()) {
                return localList
            }
        }
        val remoteList = rssApi.getRssArticlesFromUrl(rssUrl)
        if (remoteList.isNotEmpty()) {
            RssLocalDbHelper.getInstance(NewsReaderApp.app).deleteRss(originalCategory)
            RssLocalDbHelper.getInstance(NewsReaderApp.app).insertRss(originalCategory, remoteList)
            sharedPrefs().edit().putLong(originalCategory, Date().time).apply()
        }
        return remoteList
    }

    override suspend fun getRssArticles(
        refresh: Boolean,
        rssUrl: String,
        originalCategory: String
    ): RssFeedHolder {

        val rssFeedHolder = RssFeedHolder()
        //val featuredRss = getFeaturedRssArticles()
        val favorites = rssApi.getFavoriteRssArticles()
        rssFeedHolder.favorites = favorites
        rssFeedHolder.featuredRss = getFeaturedRssArticles()

        if (!refresh && !needsRefresh()) {
            val localList = rssApi.getRssArticlesFromLocalDb(originalCategory)
            if (localList.isNotEmpty()) {
                rssFeedHolder.rss = localList
                return rssFeedHolder
            }
        }

        val remoteList = rssApi.getRssArticlesFromUrl(rssUrl)
        if (remoteList.isNotEmpty()) {
            RssLocalDbHelper.getInstance(NewsReaderApp.app).deleteRss(originalCategory)
            RssLocalDbHelper.getInstance(NewsReaderApp.app).insertRss(originalCategory, remoteList)
            sharedPrefs().edit().putLong(Constants.SHARED_PREFS_HOODLINE_KEY, Date().time).apply()
        }
        rssFeedHolder.rss = remoteList
        return rssFeedHolder
    }

    private fun featuredNeedsRefresh(category: String): Boolean {
        val lastFetch = sharedPrefs().getLong(category, 0L)
        return (Date().time - lastFetch > Constants.CONTENT_EXPIRE_TIME)
    }

    private fun needsRefresh(): Boolean {
        val lastFetch = sharedPrefs().getLong(Constants.SHARED_PREFS_HOODLINE_KEY, 0L)
        return (Date().time - lastFetch > Constants.CONTENT_EXPIRE_TIME)
    }

    private fun sharedPrefs(): SharedPreferences {
        return NewsReaderApp.app.getSharedPreferences(
            Constants.SHARED_PREFS_NAME,
            Context.MODE_PRIVATE
        )
    }

    override suspend fun saveFavoriteArticle(rss: Rss): Long {
        return rssApi.saveFavoriteArticle(rss)
    }

    override suspend fun deleteFavoriteArticleByArticleId(rss: Rss): Int {
        return rssApi.deleteFavoriteArticleByArticleId(rss.articleId)
    }

    override suspend fun getFavoriteArticles(): List<Rss> {
        return rssApi.getFavoriteRssArticles()
    }

}