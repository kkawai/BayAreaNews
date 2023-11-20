package com.kk.android.bayareanews.domain.use_case.get_rss

import com.kk.android.bayareanews.domain.model.Rss

data class RssListState(
    val isLoading: Boolean = false,
    val rssList: List<Rss> = emptyList(),
    val error: String = ""
)