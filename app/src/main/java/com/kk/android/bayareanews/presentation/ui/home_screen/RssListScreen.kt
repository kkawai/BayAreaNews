package com.kk.android.bayareanews.presentation.ui.home_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kk.android.bayareanews.common.EncodingUtil
import com.kk.android.bayareanews.presentation.ui.common.ErrorScreen
import com.kk.android.bayareanews.presentation.ui.common.LoadingScreen

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun RssListScreen(
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
                        imageUrl = rss.imageUrl ?: "",
                        title = rss.title ?: "",
                        description = rss.descr ?: "",
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