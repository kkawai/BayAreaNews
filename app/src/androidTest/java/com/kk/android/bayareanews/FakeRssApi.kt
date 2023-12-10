package com.kk.android.bayareanews

import com.kk.android.bayareanews.data.RssApi
import com.kk.android.bayareanews.domain.model.Rss

class FakeRssApi : RssApi {
    override suspend fun getRssArticlesFromLocalDb(category: String): List<Rss> {
        return FakeRssData.fakeRssData()
    }

    override suspend fun getRssArticlesFromUrl(rssUrl: String): List<Rss> {
        return FakeRssData.fakeRssData()
    }

    override suspend fun saveFavoriteArticle(rss: Rss): Long {
        return 1001
    }

    override suspend fun deleteFavoriteArticleByArticleId(articleId: String): Int {
        return 1
    }

    override suspend fun getFavoriteRssArticles(): List<Rss> {
        return FakeRssData.fakeRssData()
    }
}