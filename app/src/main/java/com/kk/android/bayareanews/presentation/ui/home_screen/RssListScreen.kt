package com.kk.android.bayareanews.presentation.ui.home_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.kk.android.bayareanews.R
import com.kk.android.bayareanews.common.Constants
import com.kk.android.bayareanews.common.EncodingUtil
import com.kk.android.bayareanews.domain.model.Rss
import com.kk.android.bayareanews.domain.use_case.get_rss.RssListState
import com.kk.android.bayareanews.presentation.NewsNavHost
import com.kk.android.bayareanews.presentation.ui.common.ErrorScreen
import com.kk.android.bayareanews.presentation.ui.common.ImageCard
import com.kk.android.bayareanews.presentation.ui.common.LoadingScreen
import com.kk.android.bayareanews.ui.theme.BayAreaNewsTheme
import kotlinx.coroutines.flow.StateFlow

@Composable
private fun PostListDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(horizontal = 14.dp),
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
private fun _RssListScreen(
    onGetRss: () -> Unit,
    onRefresh: () -> Unit,
    onSaveFav: (rss: Rss) -> Unit,
    onDeleteFav: (rss: Rss) -> Unit,
    stateFlow: StateFlow<RssListState>,
    onArticleClicked: (articleLink: String) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {

    var isRefreshing by remember { mutableStateOf(false) }
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = {
            isRefreshing = true
            onRefresh()
        }
    )

    val rssListState = stateFlow.collectAsState()

    if (rssListState.value.isLoading) {
        LoadingScreen()
    } else if (rssListState.value.error.isNotBlank()) {
        isRefreshing = false
        ErrorScreen(
            errorText = rssListState.value.error,
            retryAction = { onGetRss() })
    } else if (!rssListState.value.isLoading && rssListState.value.topRss.title != null && rssListState.value.rssList.isNotEmpty()) {
        isRefreshing = false
        Box(
            modifier = Modifier
                //.padding(padding)
                .pullRefresh(pullRefreshState)
        ) {

            LazyColumn(modifier = modifier.fillMaxSize(), contentPadding = contentPadding) {

                item {
                    PostCardTop(rssListState.value.topRss,
                        modifier = Modifier
                            .clickable { onArticleClicked(EncodingUtil.encodeUrlSafe(rssListState.value.topRss.link))})
                }

                items(rssListState.value.rssList) { rss ->
                    ImageCard(
                        isFavorite = rssListState.value.favoritesMap.containsKey(rss.articleId),
                        onDeleteFavorite = { rss ->
                            onDeleteFav(rss)
                        },
                        onSaveFavorite = { rss ->
                            onSaveFav(rss)
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

@Composable
@Preview
fun PostCardTopPreview() {
    val rss = Rss()
    rss.title = "My preview title. My preview title. My preview title. My preview title. "
    rss.author = "Marie Lamb"
    rss.imageUrl = "https://www.test.com/fasdfasdf.png"
    val typography = MaterialTheme.typography

    BayAreaNewsTheme {
        Surface {

            Column() {

                Text(
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
                    text = stringResource(id = R.string.top_story_for_you),
                    style = MaterialTheme.typography.titleMedium
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    val imageModifier = Modifier
                        .heightIn(min = 180.dp)
                        .fillMaxWidth()
                        .clip(shape = MaterialTheme.shapes.medium)
                    Image(
                        painter = painterResource(R.drawable.post_2),
                        contentDescription = null, // decorative
                        modifier = imageModifier,
                        contentScale = ContentScale.Crop
                    )
                    Spacer(Modifier.height(16.dp))

                    Text(
                        text = rss.title,
                        style = typography.titleLarge,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Row() {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = rss.author,
                                style = typography.labelLarge,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                            Text(
                                text = "${Constants.HOODLINE_CARD_MARKER} · ${rss.timeAgo}",
                                style = typography.bodySmall
                            )
                        }
                        Icon(
                            imageVector = Icons.Outlined.FavoriteBorder,
                            contentDescription = null
                        )
                        Icon(
                            imageVector = Icons.Outlined.Share,
                            contentDescription = null,
                            modifier = Modifier.padding(horizontal = 12.dp)
                        )

                    }

                }

                PostListDivider()

            }
        }
    }
}

@Composable
fun PostCardTop(rss: Rss, modifier: Modifier = Modifier) {

    Text(
        modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
        text = stringResource(id = R.string.top_story_for_you),
        style = MaterialTheme.typography.titleMedium
    )

    val typography = MaterialTheme.typography
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        val imageModifier = Modifier
            .heightIn(min = 180.dp)
            .fillMaxWidth()
            .clip(shape = MaterialTheme.shapes.medium)
        Image(
            painter = rememberAsyncImagePainter(
                //model = "https://picsum.photos/seed/${Random.nextInt()}/300/200"
                model = rss.imageUrl
            ),
            contentDescription = null, // decorative
            modifier = imageModifier,
            contentScale = ContentScale.Crop
        )
        Spacer(Modifier.height(16.dp))

        Text(
            text = rss.title,
            style = typography.titleLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Row() {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = rss.author,
                    style = typography.labelLarge,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = "${Constants.HOODLINE_CARD_MARKER} · ${rss.timeAgo}",
                    style = typography.bodySmall
                )
            }
            Icon(
                imageVector = Icons.Outlined.FavoriteBorder,
                contentDescription = null,
            )
            Icon(
                imageVector = Icons.Outlined.Share,
                contentDescription = null,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
        }
    }

    PostListDivider()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RssListScreen(
    onGetRss: () -> Unit,
    onRefresh: () -> Unit,
    onSaveFav: (rss: Rss) -> Unit,
    onDeleteFav: (rss: Rss) -> Unit,
    stateFlow: StateFlow<RssListState>,
    onPrivacyPolicyClicked: () -> Unit,
    onFavoritesClicked: () -> Unit,
    onArticleClicked: (articleLink: String) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
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
                            onPrivacyPolicyClicked()
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
                onGetRss = onGetRss,
                onRefresh = onRefresh,
                onSaveFav = onSaveFav,
                onDeleteFav = onDeleteFav,
                stateFlow = stateFlow
            )
        }

    }
}