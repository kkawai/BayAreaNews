package com.kk.android.bayareanews.data

import com.kk.android.bayareanews.NewsReaderApp
import com.kk.android.bayareanews.data.local.RssLocalDbHelper
import com.kk.android.bayareanews.data.remote.RssRemoteHelper
import com.kk.android.bayareanews.domain.model.Rss

class RssApiImpl: RssApi {
    override suspend fun getRssArticlesFromLocalDb(category: String): List<Rss>{
        return RssLocalDbHelper.getInstance(NewsReaderApp.app).getRss(category)
    }

    override suspend fun getRssArticlesFromUrl(rssUrl: String): List<Rss>{
        return RssRemoteHelper().getRss(rssUrl)
    }

    override suspend fun saveFavoriteArticle(rss: Rss): Long {
        return RssLocalDbHelper.getInstance(NewsReaderApp.app).insertRssFavorite(rss)
    }

    override suspend fun deleteFavoriteArticleByArticleId(articleId: String): Int {
        return RssLocalDbHelper.getInstance(NewsReaderApp.app).deleteRssFavorite(articleId)
    }

    override suspend fun getFavoriteRssArticles(): List<Rss> {
        return RssLocalDbHelper.getInstance(NewsReaderApp.app).rssFavorites
    }

}