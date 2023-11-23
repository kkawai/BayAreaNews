package com.kk.android.bayareanews.domain.use_case.get_rss

import com.kk.android.bayareanews.domain.model.Rss
import java.util.LinkedList

data class RssFavoritesState(
    val isLoading: Boolean = false,
    val favorites: MutableList<Rss> = LinkedList(),
    val error: String = ""
)