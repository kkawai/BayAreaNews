package com.kk.android.bayareanews.presentation.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RssList(modifier: Modifier, contentPadding: PaddingValues, rssViewModel: RssViewModel = hiltViewModel()) {

    val rssListState = rssViewModel.rssListState.collectAsState()

    if (rssListState.value.isLoading) {
        //CircularProgressIndicator(modifier = Modifier.align(Alignment))
        CircularProgressIndicator()
    } else if (rssListState.value.error.isNotBlank()) {
        ErrorScreen(
            errorText = rssListState.value.error,
            retryAction = { rssViewModel.getRssList() })
    }
    if (!rssListState.value.isLoading && rssListState.value.rssList.isNotEmpty()) {
        LazyColumn(modifier = modifier.fillMaxSize(), contentPadding = contentPadding) {
            items(rssListState.value.rssList) { rss ->
                ImageCard(
                    imageUrl = rss.imageUrl ?: "",
                    title = rss.title?:"",
                    description = rss.descr?:"",
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}