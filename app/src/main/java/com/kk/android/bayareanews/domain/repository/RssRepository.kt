package com.kk.android.bayareanews.domain.repository

import com.kk.android.bayareanews.domain.model.Rss

interface RssRepository {
    suspend fun getRssArticles(refresh: Boolean, rssUrl: String, originalCategory: String): List<Rss>
}