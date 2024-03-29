package com.kk.android.bayareanews.presentation.ui.search_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kk.android.bayareanews.common.Resource
import com.kk.android.bayareanews.domain.use_case.get_rss.SearchState
import com.kk.android.bayareanews.domain.use_case.get_rss.SearchUseCase
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
class SearchWhileTypingViewModel @Inject constructor(
    private val searchUseCase: SearchUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _rssListState = MutableStateFlow(SearchState())
    val rssListState = _rssListState.asStateFlow()

    fun searchRss(searchTerm: String) {
        searchUseCase(searchTerm)
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
                                rss =  result.data ?: emptyList(),
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

