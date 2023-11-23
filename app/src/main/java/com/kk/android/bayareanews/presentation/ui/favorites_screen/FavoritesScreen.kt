package com.kk.android.bayareanews.presentation.ui.home_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kk.android.bayareanews.R
import com.kk.android.bayareanews.common.EncodingUtil
import com.kk.android.bayareanews.presentation.ui.common.ErrorScreen
import com.kk.android.bayareanews.presentation.ui.common.ImageCard
import com.kk.android.bayareanews.presentation.ui.common.LoadingScreen

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
private fun _FavoritesScreen(
    onArticleClicked: (articleLink: String) -> Unit, modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    viewModel: FavoritesViewModel
) {

    var isRefreshing by remember { mutableStateOf(false) }
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = {
            isRefreshing = true
            viewModel.getFavorites()
        }
    )

    val favoritesState = viewModel.favoritesState.collectAsState()

    if (favoritesState.value.isLoading) {
        LoadingScreen()
    } else if (favoritesState.value.error.isNotBlank()) {
        isRefreshing = false
        ErrorScreen(
            errorText = favoritesState.value.error,
            retryAction = { viewModel.getFavorites() })
    } else if (!favoritesState.value.isLoading && favoritesState.value.favorites.isNotEmpty()) {
        isRefreshing = false
        Box(
            modifier = Modifier
                //.padding(padding)
                .pullRefresh(pullRefreshState)
        ) {

            LazyColumn(modifier = modifier.fillMaxSize(), contentPadding = contentPadding) {
                itemsIndexed(favoritesState.value.favorites) { index, rss ->
                    ImageCard(
                        isFavorite = true, //initially, everything in this screen is a favorite
                        onDeleteFavorite = {rss ->
                            viewModel.deleteFavorite(rss)
                        },
                        onSaveFavorite = { rss ->
                            viewModel.saveFavorite(rss)
                        },
                        rss = rss,
                        modifier = Modifier
                            .padding(16.dp)
                            .clickable { onArticleClicked(EncodingUtil.encodeUrlSafe(rss.link)) }
                    )
                }
            }

            PullRefreshIndicator(
                refreshing = isRefreshing,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    onGoBackClicked: () -> Unit,
    onArticleClicked: (articleLink: String) -> Unit, modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                TopAppBar(
                    scrollBehavior = scrollBehavior,
                    title = {
                        Text(text = stringResource(id = R.string.favorites))
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        titleContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    actions = {
                        IconButton(onClick = {
                                onGoBackClicked()
                            }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = stringResource(id = R.string.go_back)
                            )
                        }
                    }
                )
            }
        ) { values ->
            _FavoritesScreen(
                onArticleClicked = onArticleClicked,
                contentPadding = values,
                viewModel = viewModel
            )
        }

    }
}