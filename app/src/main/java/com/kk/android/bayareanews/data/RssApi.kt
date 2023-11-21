package com.kk.android.bayareanews.data

import com.kk.android.bayareanews.domain.model.Rss

interface RssApi {
    suspend fun getRssArticlesFromLocalDb(category: String): List<Rss>

    suspend fun getRssArticlesFromUrl(rssUrl: String): List<Rss>
}