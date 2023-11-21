package com.kk.android.bayareanews.presentation.ui.home_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kk.android.bayareanews.common.Constants
import com.kk.android.bayareanews.common.Resource
import com.kk.android.bayareanews.domain.use_case.get_rss.GetRssUseCase
import com.kk.android.bayareanews.domain.use_case.get_rss.RssListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.plus
import javax.inject.Inject

@HiltViewModel
class RssViewModel @Inject constructor(private val getRssUseCase: GetRssUseCase) : ViewModel() {

    private val _rssListState = MutableStateFlow(RssListState())
    val rssListState = _rssListState.asStateFlow()

    init {
        getRssList(false)
    }

    fun getRssList(refresh: Boolean = false) {
        getRssUseCase(refresh, Constants.HOODLINE_RSS_URL, Constants.HOODLINE_CATEGORY)
            .distinctUntilChanged()
            .onEach { result ->
                when (result) {
                    is Resource.Loading -> {
                        _rssListState.update {
                            it.copy(isLoading = true)
                        }
                    }

                    is Resource.Success -> {
                        _rssListState.update {
                            it.copy(
                                isLoading = false,
                                rssList = result.data ?: emptyList(),
                                error = ""
                            )
                        }
                    }

                    is Resource.Error -> {
                        _rssListState.update {
                            it.copy(
                                isLoading = false,
                                error = result.message
                                    ?: "Unexpected Error Occurred. Please try again."
                            )
                        }
                    }
                }
                //need 'flowOn' since RssReader does it own network I/O, but retrofit doesn't need flowOn for some reason
            }.flowOn(Dispatchers.IO).launchIn(viewModelScope + SupervisorJob())
    }
}