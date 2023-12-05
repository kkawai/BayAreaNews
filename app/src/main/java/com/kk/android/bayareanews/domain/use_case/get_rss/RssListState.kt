package com.kk.android.bayareanews.domain.use_case.get_rss

import com.kk.android.bayareanews.domain.model.Rss

data class RssListState(
    val isLoading: Boolean = false,
    val rssList: List<Rss> = emptyList(),
    var topRss: Rss = Rss(),
    var featuredRss: List<Rss> = emptyList(),
    val favoritesMap: MutableMap<String, Rss> = HashMap(),
    val error: String = ""
)