package com.kk.android.bayareanews.domain.use_case.get_rss

import com.kk.android.bayareanews.domain.model.Rss

data class RssFavoritesState(
    val isLoading: Boolean = false,
    val favorites: List<Rss> = emptyList(),
    val error: String = ""
)