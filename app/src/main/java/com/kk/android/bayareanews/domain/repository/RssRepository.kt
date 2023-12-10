package com.kk.android.bayareanews.domain.repository

import com.kk.android.bayareanews.domain.model.Rss
import com.kk.android.bayareanews.domain.model.RssFeedHolder

interface RssRepository {

    suspend fun getFeaturedArticles(refresh: Boolean): RssFeedHolder

    suspend fun getRssArticles(refresh: Boolean, rssUrl: String, originalCategory: String): RssFeedHolder

    suspend fun saveFavoriteArticle(rss: Rss): Long

    suspend fun deleteFavoriteArticleByArticleId(rss: Rss): Int

    suspend fun getFavoriteArticles(): List<Rss>

}