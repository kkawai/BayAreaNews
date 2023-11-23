package com.kk.android.bayareanews.domain.use_case.get_rss

import com.kk.android.bayareanews.domain.model.Rss
import java.util.LinkedList

data class RssListState(
    val isLoading: Boolean = false,
    val rssList: List<Rss> = emptyList(),
    val favorites: MutableList<Rss> = LinkedList(),
    val favoritesMap: MutableMap<String, Rss> = HashMap(),
    val error: String = ""
)