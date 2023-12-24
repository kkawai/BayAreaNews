package com.kk.android.bayareanews.data

import com.kk.android.bayareanews.domain.model.Rss

interface RssApi {
    suspend fun getRssArticlesFromLocalDb(category: String): List<Rss>

    suspend fun getRssArticlesFromUrl(rssUrl: String): List<Rss>

    suspend fun saveFavoriteArticle(rss: Rss): Long

    suspend fun deleteFavoriteArticleByArticleId(articleId: String): Int

    suspend fun getFavoriteRssArticles(): List<Rss>

    suspend fun searchRssLocalDb(term: String): List<Rss>

    suspend fun searchFavoriteRssLocalDb(term: String): List<Rss>
}