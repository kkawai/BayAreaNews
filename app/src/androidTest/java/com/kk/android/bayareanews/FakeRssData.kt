package com.kk.android.bayareanews

import com.kk.android.bayareanews.domain.model.Rss

object FakeRssData {
    fun fakeRssData(): List<Rss>{
        val rss = Rss()
        rss.author = "test author"
        rss.title = "test title"
        rss.imageUrl = "https://fastly.picsum.photos/id/237/200/300.jpg"
        rss.articleId = "1"
        rss.publisher = "cnn"
        rss.descr = "my description"
        rss.link = "www.google.com"

        val rss2 = Rss()
        rss2.author = "test author2"
        rss2.title = "test title2"
        rss2.imageUrl = "https://fastly.picsum.photos/id/237/200/300.jpg"
        rss2.articleId = "2"
        rss2.publisher = "cnn2"
        rss2.descr = "my description2"
        rss2.link = "www.google2.com"
        return listOf(rss,rss2)
    }
}