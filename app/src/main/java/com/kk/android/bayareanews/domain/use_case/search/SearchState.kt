package com.kk.android.bayareanews.domain.use_case.get_rss

import com.kk.android.bayareanews.domain.model.Rss

data class SearchState(
    val isLoading: Boolean = false,
    val rss: List<Rss> = emptyList(),
    val error: String = ""
)