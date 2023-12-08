package com.kk.android.bayareanews.presentation.ui.home_screen

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.kk.android.bayareanews.MainApp
import com.kk.android.bayareanews.R
import com.kk.android.bayareanews.common.Constants
import com.kk.android.bayareanews.common.EncodingUtil
import com.kk.android.bayareanews.common.ShareUtil
import com.kk.android.bayareanews.domain.model.Rss
import com.kk.android.bayareanews.domain.use_case.get_rss.RssFeaturedState
import com.kk.android.bayareanews.domain.use_case.get_rss.RssListState
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

private fun shareArticle(context: Context, url: String) {
    ShareUtil.shareUrl(context, url)
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
private fun _RssListScreen(
    onGetRss: () -> Unit,
    onRefresh: () -> Unit,
    onSaveFavorite: (rss: Rss) -> Unit,
    onDeleteFavorite: (rss: Rss) -> Unit,
    rssListState: StateFlow<RssListState>,
    featuredState: StateFlow<RssFeaturedState>,
    onArticleClicked: (articleLink: String) -> Unit,
    modifier: Modifier = Modifier
) {

    var isRefreshing by remember { mutableStateOf(false) }
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = {
            isRefreshing = true
            onRefresh()
        }
    )

    val context = LocalContext.current

    val listState = rssListState.collectAsState()
    val featuredListState = featuredState.collectAsState()

    if (listState.value.isLoading) {
        LoadingScreen()
    } else if (listState.value.error.isNotBlank()) {
        isRefreshing = false
        ErrorScreen(
            errorText = listState.value.error,
            retryAction = { onGetRss() })
    } else {
        isRefreshing = false
        Box(
            modifier = Modifier
                //.padding(padding)
                .pullRefresh(pullRefreshState)
        ) {

            LazyColumn(modifier = modifier.fillMaxSize()) {

                if (listState.value.topRss.title?.isNotBlank() ?: false) {
                    item {
                        TopStorySection(
                            listState.value.topRss,
                            isFavorited = listState.value.favoritesMap.containsKey(listState.value.topRss.articleId),
                            onArticleShared = {
                                shareArticle(
                                    context,
                                    listState.value.topRss.link
                                )
                            },
                            onSaveFavorite = { onSaveFavorite(listState.value.topRss) },
                            onDeleteFavorite = { onDeleteFavorite(listState.value.topRss) },
                            modifier = Modifier
                                .clickable {
                                    onArticleClicked(
                                        EncodingUtil.encodeUrlSafe(
                                            listState.value.topRss.link
                                        )
                                    )
                                })
                    }
                }

                item {
                    Text(
                        modifier = Modifier.padding(16.dp),
                        text = stringResource(id = R.string.home_popular_section_title),
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                if (featuredState.value.featuredRss.isNotEmpty()) {
                    item {
                        FeaturedRssSection(
                            featuredListState.value.featuredRss,
                            onArticleClicked,
                            listState.value.favoritesMap,
                            onSaveFavorite,
                            onDeleteFavorite
                        )
                    }
                }
                item {
                    Column {
                        Spacer(Modifier.height(16.dp))
                        PostListDivider()
                    }
                }

                if (listState.value.rssList.isNotEmpty()) {
                    item {
                        Text(
                            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
                            text = stringResource(id = R.string.top_stories_continued),
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                }

                items(listState.value.rssList) { rss ->
                    ImageCard(
                        expandedByDefault = MainApp.app.remoteConfigMap.get(Constants.MAIN_CARDS_EXPANDED)?.asBoolean()?:true,
                        rss = rss,
                        isFavorite = listState.value.favoritesMap.containsKey(rss.articleId),
                        onArticleShared = { shareArticle(context, rss.link) },
                        onDeleteFavorite = { onDeleteFavorite(rss) },
                        onSaveFavorite = { onSaveFavorite(rss) },
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

@Preview
@Composable
fun TopStorySectionPreview() {
    val rss = Rss()
    rss.author = "author name"
    rss.title = "my title"
    rss.imageUrl = "https://sdasdfasdfasdfas.com"
    rss.articleId = "asdfasdfasdf"
    rss.link = "asdfasdfasdfsadf"
    BayAreaNewsTheme {
        Surface {
            Column {
                TopStorySection(rss, true, {}, {}, {})
            }
        }
    }

}

@Composable
private fun TopStorySection(
    rss: Rss,
    isFavorited: Boolean,
    onArticleShared: () -> Unit,
    onSaveFavorite: () -> Unit,
    onDeleteFavorite: () -> Unit,
    modifier: Modifier = Modifier
) {

    var toggleFavorite by rememberSaveable {
        mutableStateOf(isFavorited)
    }

    Text(
        modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
        text = stringResource(id = R.string.top_story_for_you),
        //style = MaterialTheme.typography.titleMedium
        style = MaterialTheme.typography.headlineSmall
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
            Column(modifier = Modifier.weight(.75f)) {
                Text(
                    text = rss.author,
                    style = typography.labelLarge,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = "${rss.publisher} · ${rss.timeAgo}",
                    style = typography.bodySmall
                )
            }
            IconButton(onClick =
            {
                if (toggleFavorite)
                    onDeleteFavorite()
                else
                    onSaveFavorite()
                toggleFavorite = !toggleFavorite
            }
            ) {
                Icon(
                    imageVector = if (toggleFavorite) Icons.Outlined.Favorite
                    else Icons.Outlined.FavoriteBorder,
                    contentDescription = null,
                )
            }
            IconButton(onClick = { onArticleShared() }) {
                Icon(
                    imageVector = Icons.Outlined.Share,
                    contentDescription = null,
                )
            }
        }
    }

    PostListDivider()
}

@Composable
private fun FeaturedRssSection(
    rssList: List<Rss>,
    onArticleClicked: (articleLink: String) -> Unit,
    favoritesMap: Map<String, Rss>,
    onSaveFavorite: (rss: Rss) -> Unit,
    onDeleteFavorite: (rss: Rss) -> Unit,
) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .horizontalScroll(rememberScrollState())
            .height(IntrinsicSize.Max)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        for (rss in rssList) {
            FeaturedRssCard(
                rss,
                { shareArticle(context, rss.link) },
                favoritesMap.containsKey(rss.articleId),
                { onSaveFavorite(rss) },
                { onDeleteFavorite(rss) },
                modifier = Modifier.clickable { onArticleClicked(EncodingUtil.encodeUrlSafe(rss.link)) },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RssListScreen(
    isExpandedScreen: Boolean,
    openDrawer: () -> Unit,
    onGetRss: () -> Unit,
    onRefresh: () -> Unit,
    onSaveFav: (rss: Rss) -> Unit,
    onDeleteFav: (rss: Rss) -> Unit,
    rssListState: StateFlow<RssListState>,
    featuredState: StateFlow<RssFeaturedState>,
    onPrivacyPolicyClicked: () -> Unit,
    onFavoritesClicked: () -> Unit,
    onArticleClicked: (articleLink: String) -> Unit,
    modifier: Modifier = Modifier
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
        ) { innerPadding ->
            val screenModifier = Modifier.padding(innerPadding)
            _RssListScreen(
                onArticleClicked = onArticleClicked,
                modifier = screenModifier,
                onGetRss = onGetRss,
                onRefresh = onRefresh,
                onSaveFavorite = onSaveFav,
                onDeleteFavorite = onDeleteFav,
                rssListState = rssListState,
                featuredState = featuredState
            )
        }

    }
}

@Composable
fun FeaturedRssCard(
    rss: Rss,
    onArticleShared: () -> Unit,
    isFavorite: Boolean,
    onSaveFavorite: () -> Unit,
    onDeleteFavorite: () -> Unit,
    modifier: Modifier = Modifier
) {

    var isFavorited by rememberSaveable {
        mutableStateOf(isFavorite)
    }

    Card(
        shape = MaterialTheme.shapes.medium,
        modifier = modifier
            .width(280.dp)
    ) {
        Column {
            Image(
                painter = rememberAsyncImagePainter(
                    model = rss.imageUrl
                ),
                contentDescription = null, // decorative
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(100.dp)
                    .fillMaxWidth()
            )

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = rss.title,
                    style = MaterialTheme.typography.headlineSmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.weight(1f))
                Row {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "${rss.publisher} · ${rss.author}",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = stringResource(
                                id = R.string.home_post_min_read,
                                formatArgs = arrayOf(
                                    rss.monthDayString,
                                    rss.minRead
                                )
                            ),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    IconButton(onClick =
                    {
                        if (isFavorited)
                            onDeleteFavorite()
                        else
                            onSaveFavorite()
                        isFavorited = !isFavorited
                    }
                    ) {
                        Icon(
                            imageVector = if (isFavorited) Icons.Outlined.Favorite
                            else Icons.Outlined.FavoriteBorder,
                            contentDescription = null,
                        )
                    }
                    IconButton(onClick = { onArticleShared() }) {
                        Icon(
                            imageVector = Icons.Outlined.Share,
                            contentDescription = null,
                        )
                    }
                }

            }
        }
    }
}
