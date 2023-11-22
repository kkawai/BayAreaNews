package com.kk.android.bayareanews.domain.repository

import com.kk.android.bayareanews.domain.model.Rss

interface RssRepository {
    suspend fun getRssArticles(refresh: Boolean, rssUrl: String, originalCategory: String): List<Rss>

    suspend fun saveFavoriteArticle(rss: Rss): Long

    suspend fun deleteFavoriteArticleByArticleId(rss: Rss): Int

    suspend fun getFavoriteRssArticles(): List<Rss>
}