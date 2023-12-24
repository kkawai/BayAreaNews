package com.kk.android.bayareanews.data

import com.kk.android.bayareanews.MainApp
import com.kk.android.bayareanews.data.local.RssLocalDbHelper
import com.kk.android.bayareanews.data.remote.RssRemoteHelper
import com.kk.android.bayareanews.domain.model.Rss

class RssApiImpl: RssApi {
    override suspend fun getRssArticlesFromLocalDb(category: String): List<Rss>{
        return RssLocalDbHelper.getInstance(MainApp.app).getRss(category)
    }

    override suspend fun getRssArticlesFromUrl(rssUrl: String): List<Rss>{
        return RssRemoteHelper().getRss(rssUrl)
    }

    override suspend fun saveFavoriteArticle(rss: Rss): Long {
        return RssLocalDbHelper.getInstance(MainApp.app).insertRssFavorite(rss)
    }

    override suspend fun deleteFavoriteArticleByArticleId(articleId: String): Int {
        return RssLocalDbHelper.getInstance(MainApp.app).deleteRssFavorite(articleId)
    }

    override suspend fun getFavoriteRssArticles(): List<Rss> {
        return RssLocalDbHelper.getInstance(MainApp.app).rssFavorites
    }

    override suspend fun searchRssLocalDb(term: String): List<Rss> {
        return RssLocalDbHelper.getInstance(MainApp.app).searchRss(term)
    }

    override suspend fun searchFavoriteRssLocalDb(term: String): List<Rss> {
        return RssLocalDbHelper.getInstance(MainApp.app).searchFavoriteRss(term)
    }
}