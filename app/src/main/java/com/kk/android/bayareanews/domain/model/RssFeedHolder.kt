package com.kk.android.bayareanews.domain.model

data class RssFeedHolder(
    var rss: List<Rss> = emptyList(),
    var favorites: List<Rss> = emptyList()
)
