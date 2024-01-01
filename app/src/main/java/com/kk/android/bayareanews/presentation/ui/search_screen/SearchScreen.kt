package com.kk.android.bayareanews.presentation.ui.search_screen

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kk.android.bayareanews.R
import com.kk.android.bayareanews.common.EncodingUtil
import com.kk.android.bayareanews.common.ShareUtil
import com.kk.android.bayareanews.domain.model.Rss
import com.kk.android.bayareanews.domain.use_case.get_rss.SearchState
import com.kk.android.bayareanews.presentation.MyExpandableAppBar
import com.kk.android.bayareanews.presentation.ui.common.ErrorScreen
import com.kk.android.bayareanews.presentation.ui.common.ImageCard
import com.kk.android.bayareanews.presentation.ui.common.LoadingScreen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

private fun shareArticle(context: Context, url: String) {
    ShareUtil.shareUrl(context, url)
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
private fun _SearchScreen(
    onSaveFavorite: (rss: Rss) -> Unit,
    onDeleteFavorite: (rss: Rss) -> Unit,
    rssListState: StateFlow<SearchState>,
    onArticleClicked: (articleLink: String) -> Unit,
    onGoBack: () -> Unit,
    searchTerm: String,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current
    val listState = rssListState.collectAsState()

    if (listState.value.isLoading) {
        LoadingScreen()
    } else if (listState.value.error.isNotBlank()) {
        ErrorScreen(
            errorText = listState.value.error,
            retryAction = { /*todo*/ })
    } else if (listState.value.rss.isEmpty()) {
        NoResultsScreen(searchTerm = searchTerm, onGoBack = onGoBack)
    } else {
        Box(
            modifier = Modifier
                //.padding(padding)
        ) {

            LazyColumn(modifier = modifier.fillMaxSize()) {

                items(listState.value.rss) { rss ->
                    ImageCard(
                        expandedByDefault = false,
                        rss = rss,
                        isFavorite = false/*listState.value.favoritesMap.containsKey(rss.articleId)*/,
                        onArticleShared = { shareArticle(context, rss.link) },
                        onDeleteFavorite = { onDeleteFavorite(rss) },
                        onSaveFavorite = { onSaveFavorite(rss) },
                        modifier = Modifier
                            .padding(16.dp)
                            .clickable { onArticleClicked(EncodingUtil.encodeUrlSafe(rss.link)) }
                    )
                }
            }

        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    isExpandedScreen: Boolean,
    onSaveFav: (rss: Rss) -> Unit,
    onDeleteFav: (rss: Rss) -> Unit,
    rssListState: StateFlow<SearchState>,
    onArticleClicked: (articleLink: String) -> Unit,
    modifier: Modifier = Modifier,
    speechFlow: MutableStateFlow<String>?,
    onSpeechButtonClicked: () -> Unit,
    onPerformSearch: (String) -> Unit,
    onGoBack: () -> Unit,
    searchTerm: String,
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

        val appBarTitle = remember {
            mutableStateOf("Find: " + searchTerm)
        }

        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                SearchScreenAppBar(
                    title = appBarTitle.value,
                    scrollBehavior = scrollBehavior,
                    speechFlow = speechFlow,
                    onSpeechButtonClicked = onSpeechButtonClicked,
                    isExpandedScreen = isExpandedScreen,
                    onGoBack = onGoBack,
                    onPerformSearch = onPerformSearch
                )
            }
        ) { innerPadding ->
            val screenModifier = Modifier.padding(innerPadding)
            _SearchScreen(
                onArticleClicked = onArticleClicked,
                modifier = screenModifier,
                onSaveFavorite = onSaveFav,
                onDeleteFavorite = onDeleteFav,
                rssListState = rssListState,
                onGoBack = onGoBack,
                searchTerm = searchTerm,
            )
        }

    }
}


