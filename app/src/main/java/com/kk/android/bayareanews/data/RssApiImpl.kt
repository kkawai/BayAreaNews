package com.kk.android.bayareanews.data

import com.kk.android.bayareanews.MainApp
import com.kk.android.bayareanews.data.local.RssLocalDbHelper2
import com.kk.android.bayareanews.data.remote.RssRemoteHelper
import com.kk.android.bayareanews.domain.model.Rss

class RssApiImpl: RssApi {
    override suspend fun getRssArticlesFromLocalDb(category: String): List<Rss>{
        return RssLocalDbHelper2.getRss(MainApp.app, category)
    }

    override suspend fun getRssArticlesFromUrl(rssUrl: String): List<Rss>{
        return RssRemoteHelper().getRss(rssUrl)
    }

    override suspend fun saveFavoriteArticle(rss: Rss): Long {
        RssLocalDbHelper2.insertRssFavorite(MainApp.app, rss)
        return 0L // probably irrelevant
    }

    override suspend fun deleteFavoriteArticleByArticleId(articleId: String): Int {
        RssLocalDbHelper2.deleteRssFavorite(MainApp.app, articleId)
        return 0 // probably irrelevant
    }

    override suspend fun getFavoriteRssArticles(): List<Rss> {
        return RssLocalDbHelper2.getRssFavorites(MainApp.app)
    }

    override suspend fun searchRssLocalDb(term: String): List<Rss> {
        return RssLocalDbHelper2.searchRss(MainApp.app, term)
    }

    override suspend fun searchFavoriteRssLocalDb(term: String): List<Rss> {
        return RssLocalDbHelper2.searchRssFavorites(MainApp.app, term)
    }
}