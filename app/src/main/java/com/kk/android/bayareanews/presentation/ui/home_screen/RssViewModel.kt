package com.kk.android.bayareanews.presentation.ui.home_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kk.android.bayareanews.common.Constants
import com.kk.android.bayareanews.common.Resource
import com.kk.android.bayareanews.domain.model.Rss
import com.kk.android.bayareanews.domain.use_case.get_favorites.DeleteFavoriteUseCase
import com.kk.android.bayareanews.domain.use_case.get_favorites.SaveFavoriteUseCase
import com.kk.android.bayareanews.domain.use_case.get_rss.GetFeaturedUseCase
import com.kk.android.bayareanews.domain.use_case.get_rss.GetRssUseCase
import com.kk.android.bayareanews.domain.use_case.get_rss.RssFeaturedState
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
class RssViewModel @Inject constructor(
    private val getRssUseCase: GetRssUseCase,
    private val getFeaturedUseCase: GetFeaturedUseCase,
    private val saveFavoriteUseCase: SaveFavoriteUseCase,
    private val deleteFavoriteUseCase: DeleteFavoriteUseCase
) : ViewModel() {

    private var rssUrl: String = ""

    private val _rssListState = MutableStateFlow(RssListState())
    val rssListState = _rssListState.asStateFlow()

    private val _featuredState = MutableStateFlow(RssFeaturedState())
    val featuredState = _featuredState.asStateFlow()

    init {
        if (rssUrl.equals(Constants.rssUrl))
            getRssList(false)
        else {
            getRssList(true)
            rssUrl = Constants.rssUrl
        }
    }

    private fun getTopStory(rss: List<Rss>): Rss {
        return if (rss.isNotEmpty()) rss.get(0) else Rss()
    }

    private fun getRemainingStories(rss: List<Rss>): List<Rss> {
        return if (rss.size > 1) rss.subList(1,rss.size) else emptyList()
    }

    fun getRssList(refresh: Boolean = false) {
        getMainStories(refresh)
        getFeaturedStories(refresh)
    }

    private fun getMainStories(refresh: Boolean = false) {
        getRssUseCase(refresh, Constants.rssUrl, Constants.HOODLINE_CATEGORY)
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
                                rssList =  getRemainingStories(result.data?.rss ?: emptyList()),
                                topRss = getTopStory(result.data?.rss ?: emptyList()),
                                favoritesMap = result.data?.favorites?.associateBy({it.articleId},{it})?.toMutableMap()?:HashMap(),
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

    private fun getFeaturedStories(refresh: Boolean = false) {
        getFeaturedUseCase(refresh)
            .distinctUntilChanged()
            .onEach { result ->
                when (result) {
                    is Resource.Loading -> {
                        _featuredState.update {
                            it.copy(isLoading = true)
                        }
                    }

                    is Resource.Success -> {
                        _featuredState.update {
                            it.copy(
                                isLoading = false,
                                featuredRss = result.data?.rss ?: emptyList(),
                                error = ""
                            )
                        }
                    }

                    is Resource.Error -> {
                        _featuredState.update {
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
                        //not implemented; not much to do
                    }

                    is Resource.Success -> {

                        if (!_rssListState.value.favoritesMap.containsKey(rss.articleId)) {
                            _rssListState.value.favoritesMap.put(rss.articleId, rss)
                        }
                    }

                    is Resource.Error -> {
                        //not implemented; not much to do
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
                        //not implemented; not much to do
                    }

                    is Resource.Success -> {
                        if (_rssListState.value.favoritesMap.containsKey(rss.articleId)) {
                            _rssListState.value.favoritesMap.remove(rss.articleId)
                        }
                    }

                    is Resource.Error -> {
                        //not implemented; not much to do
                    }
                }
            }.flowOn(Dispatchers.IO).launchIn(viewModelScope + SupervisorJob())
    }
}

