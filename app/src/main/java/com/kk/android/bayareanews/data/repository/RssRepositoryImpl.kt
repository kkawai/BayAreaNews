package com.kk.android.bayareanews.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.kk.android.bayareanews.MainApp
import com.kk.android.bayareanews.common.Constants
import com.kk.android.bayareanews.common.MLog
import com.kk.android.bayareanews.data.RssApi
import com.kk.android.bayareanews.data.local.RssLocalDbHelper2
import com.kk.android.bayareanews.domain.model.Rss
import com.kk.android.bayareanews.domain.model.RssFeedHolder
import com.kk.android.bayareanews.domain.repository.RssRepository
import java.util.Date
import javax.inject.Inject

class RssRepositoryImpl @Inject constructor(private val rssApi: RssApi) : RssRepository {

    override suspend fun getFeaturedArticles( refresh: Boolean): RssFeedHolder {

        val rssFeedHolder = RssFeedHolder()
        MLog.i("nnnnn","getFeaturedArticles waiting for remote config..")
        val remoteConfigResponse = MainApp.app.remoteConfigResponse.await()
        MLog.i("nnnnn","getFeaturedArticles after waiting for remote config. remoteConfigResponse=$remoteConfigResponse")
        if (!remoteConfigResponse) {
            MLog.w("nnnnn","getFeaturedArticles Error: Failed to get remote config ")
            return rssFeedHolder
        }
        val originalCategory = MainApp.app.remoteConfigMap.get(Constants.FEATURED_CATEGORIES)?.asString()?:""
        if (originalCategory.isBlank()) {
            MLog.w("nnnnn","getFeaturedArticles Error: Failed to get featured category from remote config ")
            return rssFeedHolder
        }
        val rssUrlFromConfig = MainApp.app.remoteConfigMap.get(originalCategory.trim())?.asString()?:""
        if (rssUrlFromConfig.isBlank()) {
            MLog.w("nnnnn","getFeaturedArticles Error: Failed to get featured category url from remote config ")
            return rssFeedHolder
        }

        if (!refresh && !featuredNeedsRefresh(originalCategory)) {
            val localList = rssApi.getRssArticlesFromLocalDb(originalCategory)
            if (localList.isNotEmpty()) {
                rssFeedHolder.rss = localList
                return rssFeedHolder
            }
        }

        val remoteList = rssApi.getRssArticlesFromUrl(rssUrlFromConfig)
        if (remoteList.isNotEmpty()) {
            RssLocalDbHelper2.deleteRss(MainApp.app, originalCategory)
            RssLocalDbHelper2.insertRss(MainApp.app, originalCategory, remoteList)
            sharedPrefs().edit().putLong(originalCategory, Date().time).apply()
        }
        rssFeedHolder.rss = remoteList
        return rssFeedHolder
    }

    override suspend fun getRssArticles(
        refresh: Boolean,
        rssUrl: String,
        originalCategory: String
    ): RssFeedHolder {

        val rssFeedHolder = RssFeedHolder()
        val favorites = rssApi.getFavoriteRssArticles()
        rssFeedHolder.favorites = favorites

        if (!refresh && !needsRefresh()) {
            val localList = rssApi.getRssArticlesFromLocalDb(originalCategory)
            if (localList.isNotEmpty()) {
                rssFeedHolder.rss = localList
                return rssFeedHolder
            }
        }

        val remoteList = rssApi.getRssArticlesFromUrl(rssUrl)
        if (remoteList.isNotEmpty()) {
            RssLocalDbHelper2.deleteRss(MainApp.app, originalCategory)
            RssLocalDbHelper2.insertRss(MainApp.app, originalCategory, remoteList)
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
        return MainApp.app.getSharedPreferences(
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

    override suspend fun searchArticles(term: String): List<Rss> {
        return rssApi.searchRssLocalDb(term) + rssApi.searchFavoriteRssLocalDb(term)
    }
}