package com.kk.android.bayareanews.data.remote

import com.kk.android.bayareanews.domain.model.Rss

interface RssApi {
    suspend fun getRssArticles(refresh: Boolean, rssUrl: String, category: String): List<Rss>
}