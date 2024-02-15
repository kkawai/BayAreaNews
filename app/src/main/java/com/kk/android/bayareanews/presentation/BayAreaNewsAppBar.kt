package com.kk.android.bayareanews.presentation

import android.util.Log
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import com.kk.android.bayareanews.R
import com.kk.android.bayareanews.domain.use_case.get_rss.SearchState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyExpandableAppBar(scrollBehavior: TopAppBarScrollBehavior,
                       speechFlow: MutableStateFlow<String>?,
                       onSpeechButtonClicked: ()->Unit,
                       isExpandedScreen: Boolean,
                       onFavoritesClicked: ()->Unit,
                       onRewardsClicked: ()->Unit,
                       openDrawer: () -> Unit,
                       onPerformSearch: (String) -> Unit,
                       onPerformSearchWhileTyping: (String)->Unit,
                       searchResultFlowWhileTyping: StateFlow<SearchState>,
                       onArticleClicked: (articleLink: String) -> Unit,
                       title: String
                       ) {
    val expandedInitially = false
    val (expanded, onExpandedChanged) = remember {
        mutableStateOf(expandedInitially)
    }

    Crossfade(targetState = expanded) { isSearchFieldVisible ->
        when (isSearchFieldVisible) {
            true -> MySearchBar(onExpandedChanged, speechFlow, onSpeechButtonClicked,
                onPerformSearch, onPerformSearchWhileTyping, searchResultFlowWhileTyping,
                onArticleClicked)

            false -> MyTopAppBar(
                title = title,
                isExpandedScreen = isExpandedScreen,
                scrollBehavior,
                onSearchButtonClicked = {
                    onExpandedChanged(true)
                },
                onFavoritesClicked = onFavoritesClicked,
                openDrawer = openDrawer)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MyTopAppBar(
                title: String,
                isExpandedScreen: Boolean,
                scrollBehavior: TopAppBarScrollBehavior,
                onSearchButtonClicked: ()->Unit,
                onFavoritesClicked: ()->Unit,
                openDrawer: () -> Unit) {

    TopAppBar(
        scrollBehavior = scrollBehavior,
        title = {
            Text(text = title)
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
                onSearchButtonClicked()
            }) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = stringResource(R.string.search)
                )
            }
            IconButton(onClick = {
                onFavoritesClicked()
            }) {
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = stringResource(R.string.favorites)
                )
            }
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MySearchBar(
                onExpandedChanged: (Boolean) -> Unit,
                speechFlow: MutableStateFlow<String>?,
                onSpeechButtonClicked: ()->Unit,
                onPerformSearch: (String)->Unit,
                onPerformSearchWhileTyping: (String)->Unit,
                searchResultFlowWhileTyping: StateFlow<SearchState>,
                onArticleClicked: (articleLink: String) -> Unit
) {

    var query by remember {
        mutableStateOf("")
    }
    val keyboard = LocalSoftwareKeyboardController.current
    val speechText = speechFlow?.collectAsState()
    var active by remember {
        mutableStateOf(true)
    }
    fun doSearch(query: String) {
        keyboard?.hide()
        active = false
        onExpandedChanged(false)
        onPerformSearch(query)
        Log.i("vvvvv","perform search on: $query")
    }

    speechText?.let {
        if (it.value.isNotEmpty()) {
            query = it.value
            speechFlow.update { "" }
            doSearch(query)
        }
    }

    //val searchHistory = listOf("bitcoin", "cookies", "memes")
    val searchResultsWhileTyping = searchResultFlowWhileTyping.collectAsState()
    val textFieldFocusRequester = remember { FocusRequester() }

    SideEffect {
        textFieldFocusRequester.requestFocus()
    }

    DockedSearchBar(
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(textFieldFocusRequester),
        query = query,
        onQueryChange = { query = it
                            onPerformSearchWhileTyping(query)
                        },
        onSearch = {
            doSearch(query)
        },
        active = active,
        onActiveChange = {
            active = it
            if (!active) {
                onExpandedChanged(false)
            }
            Log.i("vvvvv", "DockedSearchBar active: $active")
        },
        placeholder = {
            Text( text = stringResource(id = R.string.search))
        },
        leadingIcon = {
            Icon(imageVector = Icons.Outlined.Search, contentDescription = stringResource(id = R.string.search))
        },
        trailingIcon = {
            Row {

                IconButton(onClick = {onSpeechButtonClicked()}) {
                    Icon(
                        imageVector = Icons.Filled.Mic,
                        contentDescription = stringResource(id = R.string.mic)
                    )
                }

                if (active) {
                    IconButton(onClick = {
                        if (query.isNotEmpty())
                            query = ""
                        else {
                            active = false
                            onExpandedChanged(false)
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.Close,
                            contentDescription = stringResource(id = R.string.close)
                        )
                    }
                }
            }
        }

    ) {

        if (searchResultsWhileTyping.value.rss.isNotEmpty()) {
            searchResultsWhileTyping.value.rss.takeLast(4).forEach { item ->

                ListItem(
                    modifier = Modifier.clickable { onArticleClicked(item.link) },
                    headlineContent = { Text( text = item.title) },
                    leadingContent = {
                        Icon(
                            imageVector = Icons.Outlined.History,
                            contentDescription = stringResource(id = R.string.search)
                        )
                    })
            }
        }

    }

}