package com.kk.android.bayareanews.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RssList(contentPadding: PaddingValues, rssViewModel: RssViewModel = hiltViewModel()) {

    val rssListState = rssViewModel.rssListState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        if (rssListState.value.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else if (rssListState.value.error.isNotBlank()) {
            ErrorScreen(errorText = rssListState.value.error, retryAction = {rssViewModel.getRssList()})
        }
        if (!rssListState.value.isLoading && rssListState.value.rssList.isNotEmpty()) {
            LazyColumn(contentPadding = contentPadding) {
                items(rssListState.value.rssList) { rss ->
                    ImageCard(
                        imageUrl = rss.imageUrl,
                        title = rss.title,
                        description = rss.descr,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}