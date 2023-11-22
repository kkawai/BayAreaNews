package com.kk.android.bayareanews.domain.use_case.get_favorites

import com.kk.android.bayareanews.domain.model.Rss
import java.util.LinkedList

data class RssFavoritesState(
    val isLoading: Boolean = false,
    val rssFavorites: MutableList<Rss> = LinkedList<Rss>(),
    val rssFavoritesMap: MutableMap<String, Rss> = HashMap<String,Rss>(),
    val isLocallyFetched: Boolean = false,
    val error: String = ""
)