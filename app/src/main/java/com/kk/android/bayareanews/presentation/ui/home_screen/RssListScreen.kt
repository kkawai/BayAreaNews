package com.kk.android.bayareanews.presentation.ui.home_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kk.android.bayareanews.common.Util
import com.kk.android.bayareanews.presentation.ui.common.ErrorScreen
import com.kk.android.bayareanews.presentation.ui.common.LoadingScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RssListScreen(onArticleClicked: (articleLink: String)->Unit, modifier: Modifier = Modifier,
                  contentPadding: PaddingValues = PaddingValues(0.dp),
                  rssViewModel: RssViewModel = hiltViewModel()) {

    val rssListState = rssViewModel.rssListState.collectAsState()

    if (rssListState.value.isLoading) {
        LoadingScreen()
    } else if (rssListState.value.error.isNotBlank()) {
        ErrorScreen(
            errorText = rssListState.value.error,
            retryAction = { rssViewModel.getRssList() })
    } else if (!rssListState.value.isLoading && rssListState.value.rssList.isNotEmpty()) {
        LazyColumn(modifier = modifier.fillMaxSize(), contentPadding = contentPadding) {
            items(rssListState.value.rssList) { rss ->
                ImageCard(
                    imageUrl = rss.imageUrl ?: "",
                    title = rss.title?:"",
                    description = rss.descr?:"",
                    modifier = Modifier.padding(16.dp).clickable { onArticleClicked(Util.encodeUrlSafe(rss.link)) }
                )
            }
        }
    }
}