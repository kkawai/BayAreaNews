package com.kk.android.bayareanews.presentation.ui.home_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Menu
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kk.android.bayareanews.MainApp
import com.kk.android.bayareanews.R
import com.kk.android.bayareanews.common.Constants
import com.kk.android.bayareanews.common.ShareUtil
import com.kk.android.bayareanews.domain.model.Rss
import com.kk.android.bayareanews.domain.use_case.get_rss.RssFavoritesState
import com.kk.android.bayareanews.presentation.ui.common.ErrorScreen
import com.kk.android.bayareanews.presentation.ui.common.ImageCard
import com.kk.android.bayareanews.presentation.ui.common.LoadingScreen
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
private fun _FavoritesScreen(
    onGetFavorites: ()->Unit,
    onSaveFavorite: (rss: Rss) -> Unit,
    onDeleteFavorite: (rss: Rss) -> Unit,
    state: StateFlow<RssFavoritesState>,
    onArticleClicked: (articleLink: String) -> Unit,
    modifier: Modifier = Modifier
) {

    var isRefreshing by remember { mutableStateOf(false) }
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = {
            isRefreshing = true
            onGetFavorites()
        }
    )

    val favoritesState = state.collectAsState()

    if (favoritesState.value.isLoading) {
        LoadingScreen()
    } else if (favoritesState.value.error.isNotBlank()) {
        isRefreshing = false
        ErrorScreen(
            errorText = favoritesState.value.error,
            retryAction = { onGetFavorites() })
    } else if (!favoritesState.value.isLoading && favoritesState.value.favorites.isEmpty()) {
        isRefreshing = false
        ErrorScreen(errorText = stringResource(id = R.string.no_favorites),
            retryAction = {})
    } else {
        isRefreshing = false
        Box(
            modifier = Modifier
                //.padding(padding)
                .pullRefresh(pullRefreshState)
        ) {
            val context = LocalContext.current
            LazyColumn(modifier = modifier.fillMaxSize()) {
                itemsIndexed(favoritesState.value.favorites) { index, rss ->
                    ImageCard(
                        expandedByDefault = MainApp.app.remoteConfigMap.get(Constants.FEATURED_CARDS_EXPANDED)?.asBoolean()?:false,
                        isFavorite = true, //initially, everything in this screen is a favorite
                        onDeleteFavorite = {onDeleteFavorite(rss)},
                        onSaveFavorite = {onSaveFavorite(rss)},
                        onArticleShared = {ShareUtil.shareUrl(context, rss.link)},
                        rss = rss,
                        modifier = Modifier
                            .padding(16.dp)
                            .clickable {onArticleClicked(rss.link)}
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
    isExpandedScreen: Boolean,
    openDrawer: () -> Unit,
    onGoBackClicked: () -> Unit,
    onArticleClicked: (articleLink: String) -> Unit,
    modifier: Modifier = Modifier,
    onGetFavorites: ()->Unit,
    onSaveFav: (rss: Rss) -> Unit,
    onDeleteFav: (rss: Rss) -> Unit,
    state: StateFlow<RssFavoritesState>,
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
                    navigationIcon = {
                        if (!isExpandedScreen) {
                            IconButton(onClick = openDrawer) {
                                Icon(
                                    imageVector = Icons.Outlined.Menu,
                                    contentDescription = stringResource(
                                        R.string.cd_open_navigation_drawer
                                    ),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
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
        ) { innerPadding ->
            val screenModifier = Modifier.padding(innerPadding)
            _FavoritesScreen(
                onArticleClicked = onArticleClicked,
                onGetFavorites = onGetFavorites,
                onSaveFavorite = onSaveFav,
                onDeleteFavorite = onDeleteFav,
                state = state,
                modifier = screenModifier
            )
        }

    }
}