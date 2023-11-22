package com.kk.android.bayareanews.domain.use_case.get_favorites

import com.kk.android.bayareanews.domain.model.Rss

data class RssFavoritesState(
    val isLoading: Boolean = false,
    val rssFavorites: List<Rss> = emptyList(),
    val error: String = ""
)