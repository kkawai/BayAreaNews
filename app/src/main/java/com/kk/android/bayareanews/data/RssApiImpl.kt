package com.kk.android.bayareanews.data

import com.kk.android.bayareanews.NewsReaderApp
import com.kk.android.bayareanews.data.local.RssDb
import com.kk.android.bayareanews.data.remote.RssApi
import com.kk.android.bayareanews.data.remote.RssReader
import com.kk.android.bayareanews.domain.model.Rss

class RssApiImpl: RssApi {
    override suspend fun getRssArticles(refresh: Boolean, rssUrl: String, category: String): List<Rss>{

        if (!refresh) {
            val list = getRssFromLocalDb(category)
            if (list.isNotEmpty()) {
                return list
            }
        }

        val remoteList = getRssFromInternet(category, rssUrl)
        return remoteList
    }

    private suspend fun getRssFromLocalDb(category: String): List<Rss> {
        return RssDb.getInstance(NewsReaderApp.app).getRss(category)
    }

    private suspend fun getRssFromInternet(category: String, rssUrl: String): List<Rss> {
        val remoteList = RssReader().getRss(rssUrl)
        if (remoteList.isNotEmpty()) {
            RssDb.getInstance(NewsReaderApp.app).deleteRss(category)
            RssDb.getInstance(NewsReaderApp.app).insertRss(category, remoteList)
        }
        return remoteList
    }

}