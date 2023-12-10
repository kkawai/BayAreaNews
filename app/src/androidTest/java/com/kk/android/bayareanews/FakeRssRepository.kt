package com.kk.android.bayareanews

import com.kk.android.bayareanews.domain.model.Rss
import com.kk.android.bayareanews.domain.model.RssFeedHolder
import com.kk.android.bayareanews.domain.repository.RssRepository

class FakeRssRepository: RssRepository {
    override suspend fun getFeaturedArticles(refresh: Boolean): RssFeedHolder {
        val rssFeedHolder = RssFeedHolder()
        rssFeedHolder.rss = FakeRssData.fakeRssData()
        return rssFeedHolder
    }

    override suspend fun getRssArticles(
        refresh: Boolean,
        rssUrl: String,
        originalCategory: String
    ): RssFeedHolder {
        val rssFeedHolder = RssFeedHolder()
        rssFeedHolder.rss = FakeRssData.fakeRssData()
        rssFeedHolder.favorites = FakeRssData.fakeRssData()
        return rssFeedHolder
    }

    override suspend fun saveFavoriteArticle(rss: Rss): Long {
        return 1001
    }

    override suspend fun deleteFavoriteArticleByArticleId(rss: Rss): Int {
        return 1
    }

    override suspend fun getFavoriteArticles(): List<Rss> {
        return FakeRssData.fakeRssData()
    }
}