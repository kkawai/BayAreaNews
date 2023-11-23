package com.kk.android.bayareanews.domain.repository

import com.kk.android.bayareanews.domain.model.Rss
import com.kk.android.bayareanews.domain.model.RssAndFavorites

interface RssRepository {
    suspend fun getRssArticles(refresh: Boolean, rssUrl: String, originalCategory: String): RssAndFavorites

    suspend fun saveFavoriteArticle(rss: Rss): Long

    suspend fun deleteFavoriteArticleByArticleId(rss: Rss): Int

}