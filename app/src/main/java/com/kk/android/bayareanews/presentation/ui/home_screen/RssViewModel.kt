package com.kk.android.bayareanews.presentation.ui.home_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kk.android.bayareanews.common.Constants
import com.kk.android.bayareanews.common.Resource
import com.kk.android.bayareanews.domain.model.Rss
import com.kk.android.bayareanews.domain.use_case.get_favorites.DeleteFavoriteUseCase
import com.kk.android.bayareanews.domain.use_case.get_favorites.GetFavoritesUseCase
import com.kk.android.bayareanews.domain.use_case.get_favorites.RssFavoritesState
import com.kk.android.bayareanews.domain.use_case.get_favorites.SaveFavoriteUseCase
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
import java.util.LinkedList
import javax.inject.Inject

@HiltViewModel
class RssViewModel @Inject constructor(private val getRssUseCase: GetRssUseCase,
                                       private val getFavoritesUseCase: GetFavoritesUseCase,
                                       private val saveFavoriteUseCase: SaveFavoriteUseCase,
                                       private val deleteFavoriteUseCase: DeleteFavoriteUseCase) : ViewModel() {

    private val _rssListState = MutableStateFlow(RssListState())
    val rssListState = _rssListState.asStateFlow()

    private val _rssFavoritesState = MutableStateFlow(RssFavoritesState())
    val rssFavoritesState = _rssFavoritesState.asStateFlow()

    init {
        getRssList(false)
        getRssFavorites()
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

    fun getRssFavorites() {
        getFavoritesUseCase()
            .distinctUntilChanged()
            .onEach { result ->
                when (result) {
                    is Resource.Loading -> {
                        _rssFavoritesState.update {
                            it.copy(isLoading = true)
                        }
                    }

                    is Resource.Success -> {
                        _rssFavoritesState.update {
                            it.copy(
                                isLoading = false,
                                rssFavorites = result.data?.toMutableList() ?: LinkedList<Rss>(),
                                rssFavoritesMap = if (result.data != null) result.data.associateBy({it.articleId},{it}).toMutableMap() else HashMap<String,Rss>(),
                                error = "",
                                isLocallyFetched = true
                            )
                        }
                    }

                    is Resource.Error -> {
                        _rssFavoritesState.update {
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

    fun saveFavorite(rss: Rss) {
        saveFavoriteUseCase(rss)
            .distinctUntilChanged()
            .onEach { result ->
                when (result) {
                    is Resource.Loading -> {
                        //todo
                    }
                    is Resource.Success -> {
                        if (_rssFavoritesState.value.isLocallyFetched
                            && !_rssFavoritesState.value.rssFavoritesMap.containsKey(rss.articleId)) {

                            _rssFavoritesState.value.rssFavoritesMap.put(rss.articleId, rss)
                            _rssFavoritesState.value.rssFavorites.add(0, rss)
                        }
                    }
                    is Resource.Error -> {
                        //todo
                    }
                }
            }.flowOn(Dispatchers.IO).launchIn(viewModelScope + SupervisorJob())
    }

    fun deleteFavorite(rss: Rss) {
        deleteFavoriteUseCase(rss)
            .distinctUntilChanged()
            .onEach { result ->
                when (result) {
                    is Resource.Loading -> {
                        //todo
                    }
                    is Resource.Success -> {
                        if (_rssFavoritesState.value.isLocallyFetched
                            && _rssFavoritesState.value.rssFavoritesMap.containsKey(rss.articleId)) {

                            _rssFavoritesState.value.rssFavoritesMap.remove(rss.articleId)
                            _rssFavoritesState.value.rssFavorites.remove(rss)
                        }
                    }
                    is Resource.Error -> {
                        //todo
                    }
                }
            }.flowOn(Dispatchers.IO).launchIn(viewModelScope + SupervisorJob())
    }
}

