package com.kk.android.bayareanews.domain.model

data class RssAndFavorites(
    var rss: List<Rss> = emptyList(),
    var favorites: List<Rss> = emptyList()
)
