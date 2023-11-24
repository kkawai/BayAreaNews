package com.kk.android.bayareanews.presentation.ui.home_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kk.android.bayareanews.common.Resource
import com.kk.android.bayareanews.domain.model.Rss
import com.kk.android.bayareanews.domain.use_case.get_favorites.DeleteFavoriteUseCase
import com.kk.android.bayareanews.domain.use_case.get_favorites.SaveFavoriteUseCase
import com.kk.android.bayareanews.domain.use_case.get_rss.GetFavoritesUseCase
import com.kk.android.bayareanews.domain.use_case.get_rss.RssFavoritesState
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
class FavoritesViewModel @Inject constructor(
    private val getFavoritesUseCase: GetFavoritesUseCase,
    private val saveFavoriteUseCase: SaveFavoriteUseCase,
    private val deleteFavoriteUseCase: DeleteFavoriteUseCase
) : ViewModel() {

    private val _favoritesState = MutableStateFlow(RssFavoritesState())
    val favoritesState = _favoritesState.asStateFlow()

    init {
        getFavorites()
    }

    fun getFavorites() {
        getFavoritesUseCase()
            .distinctUntilChanged()
            .onEach { result ->
                when (result) {
                    is Resource.Loading -> {
                        _favoritesState.update {
                            it.copy(isLoading = true)
                        }
                    }

                    is Resource.Success -> {
                        _favoritesState.update {
                            it.copy(
                                isLoading = false,
                                favorites =  result.data?.toMutableList()?: LinkedList(),
                                error = ""
                            )
                        }
                    }

                    is Resource.Error -> {
                        _favoritesState.update {
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

                    }

                    is Resource.Error -> {
                        //not implemented; not much to do
                    }
                }
            }.flowOn(Dispatchers.IO).launchIn(viewModelScope + SupervisorJob())
    }
}

