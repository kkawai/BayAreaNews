package com.kk.android.bayareanews.data.repository

import com.kk.android.bayareanews.data.remote.RssApi
import com.kk.android.bayareanews.domain.model.Rss
import com.kk.android.bayareanews.domain.repository.RssRepository
import javax.inject.Inject

class RssRepositoryImpl @Inject constructor(private val rssApi: RssApi): RssRepository {
    override suspend fun getRssArticles(refresh: Boolean, rssUrl: String, category: String): List<Rss> {
        return rssApi.getRssArticles(refresh, rssUrl, category)
    }

}