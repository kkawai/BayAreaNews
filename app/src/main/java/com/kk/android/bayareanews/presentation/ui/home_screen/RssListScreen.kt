package com.kk.android.bayareanews.presentation.ui.home_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PrivacyTip
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
import com.kk.android.bayareanews.common.Constants
import com.kk.android.bayareanews.common.EncodingUtil
import com.kk.android.bayareanews.presentation.ui.common.ErrorScreen
import com.kk.android.bayareanews.presentation.ui.common.ImageCard
import com.kk.android.bayareanews.presentation.ui.common.LoadingScreen

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
private fun _RssListScreen(
    onArticleClicked: (articleLink: String) -> Unit, modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    rssViewModel: RssViewModel = hiltViewModel()
) {

    var isRefreshing by remember { mutableStateOf(false) }
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = {
            isRefreshing = true
            rssViewModel.getRssList(true)
        }
    )

    val rssListState = rssViewModel.rssListState.collectAsState()

    if (rssListState.value.isLoading) {
        LoadingScreen()
    } else if (rssListState.value.error.isNotBlank()) {
        isRefreshing = false
        ErrorScreen(
            errorText = rssListState.value.error,
            retryAction = { rssViewModel.getRssList() })
    } else if (!rssListState.value.isLoading && rssListState.value.rssList.isNotEmpty()) {
        isRefreshing = false
        Box(
            modifier = Modifier
                //.padding(padding)
                .pullRefresh(pullRefreshState)
        ) {

            LazyColumn(modifier = modifier.fillMaxSize(), contentPadding = contentPadding) {
                items(rssListState.value.rssList) { rss ->
                    ImageCard(
                        isFavorite = rssListState.value.favoritesMap.containsKey(rss.articleId),
                        onDeleteFavorite = { rss ->
                            rssViewModel.deleteFavorite(rss)
                        },
                        onSaveFavorite = { rss ->
                            rssViewModel.saveFavorite(rss)
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
fun RssListScreen(
    onPrivacyPolicyClicked: (privacyPolicyUrl: String) -> Unit,
    onFavoritesClicked: () -> Unit,
    onArticleClicked: (articleLink: String) -> Unit, modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    rssViewModel: RssViewModel = hiltViewModel()
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
                        Text(text = stringResource(id = R.string.app_name))
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        titleContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    actions = {
                        IconButton(onClick = {
                            onFavoritesClicked()
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Favorite,
                                contentDescription = stringResource(R.string.favorites)
                            )
                        }
                        IconButton(onClick = {
                            onPrivacyPolicyClicked(EncodingUtil.encodeUrlSafe(Constants.PRIVACY_POLICY_URL))
                        }) {
                            Icon(
                                imageVector = Icons.Filled.PrivacyTip,
                                contentDescription = stringResource(R.string.favorites)
                            )
                        }
                    }
                )
            }
        ) { values ->
            _RssListScreen(
                onArticleClicked = onArticleClicked,
                contentPadding = values,
                rssViewModel = rssViewModel
            )
        }

    }
}