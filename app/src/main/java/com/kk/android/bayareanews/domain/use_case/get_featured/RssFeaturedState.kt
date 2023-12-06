package com.kk.android.bayareanews.domain.use_case.get_rss

import com.kk.android.bayareanews.domain.model.Rss

data class RssFeaturedState(
    val isLoading: Boolean = false,
    var featuredRss: List<Rss> = emptyList(),
    val error: String = ""
)