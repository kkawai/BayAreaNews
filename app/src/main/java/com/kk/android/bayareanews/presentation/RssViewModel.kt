package com.kk.android.bayareanews.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kk.android.bayareanews.common.Constants
import com.kk.android.bayareanews.common.Resource
import com.kk.android.bayareanews.domain.use_case.get_rss.GetRssUseCase
import com.kk.android.bayareanews.domain.use_case.get_rss.RssListState
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.plus
import javax.inject.Inject

class RssViewModel @Inject constructor(private val getRssUseCase: GetRssUseCase) : ViewModel() {

    private val _rssListState = MutableStateFlow(RssListState())
    val rssListState = _rssListState.asStateFlow()

    init {
        getRssList()
    }

    fun getRssList() {
        getRssUseCase(false, Constants.HOODLINE_RSS_URL, Constants.HOODLINE_CATEGORY)
            .distinctUntilChanged()
            .onEach {result ->
                when (result) {
                    is Resource.Loading -> {
                        _rssListState.value = RssListState(
                            isLoading = true
                        )
                    }
                    is Resource.Success -> {
                        _rssListState.value = RssListState(
                            isLoading = false,
                            rssList = result.data?: emptyList(),
                            error = ""
                        )
                    }
                    is Resource.Error -> {
                        _rssListState.value = RssListState(
                            isLoading = false,
                            error = result.message?:"Unexpected Error Occurred. Please try again."
                        )
                    }
                }
            }.launchIn(viewModelScope + SupervisorJob())
    }
}