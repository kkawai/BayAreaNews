package com.kk.android.bayareanews.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.kk.android.bayareanews.R
import com.kk.android.bayareanews.common.Constants
import com.kk.android.bayareanews.presentation.ui.common.Screen
import com.kk.android.bayareanews.presentation.ui.common.WebViewScreen
import com.kk.android.bayareanews.presentation.ui.home_screen.ContactInfoScreen
import com.kk.android.bayareanews.presentation.ui.home_screen.FavoritesScreen
import com.kk.android.bayareanews.presentation.ui.home_screen.FavoritesViewModel
import com.kk.android.bayareanews.presentation.ui.home_screen.PrivacyPolicyScreen
import com.kk.android.bayareanews.presentation.ui.home_screen.RssListScreen
import com.kk.android.bayareanews.presentation.ui.home_screen.RssViewModel
import com.kk.android.bayareanews.presentation.ui.search_screen.SearchScreen
import com.kk.android.bayareanews.presentation.ui.search_screen.SearchViewModel
import com.kk.android.bayareanews.presentation.ui.search_screen.SearchWhileTypingViewModel
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun BayAreaNewsNavHost(
    navigationActions: BayAreaNewsNavigationActions,
    isExpandedScreen: Boolean,
    modifier: Modifier = Modifier,
    navController: NavHostController,
    openDrawer: () -> Unit = {},
    startDestination: String = Screen.HomeScreen.route,
    speechFlow: MutableStateFlow<String>?,
    onSpeechButtonClicked: ()->Unit
) {

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        composable(
            Screen.HomeScreen.route
        ) {
            Constants.rssUrl = Constants.HOODLINE_RSS_URL
            val viewModel = hiltViewModel<RssViewModel>()
            val searchViewModel = hiltViewModel<SearchViewModel>()
            RssListScreen(
                isExpandedScreen = isExpandedScreen,
                openDrawer = openDrawer,
                onGetRss = { viewModel.getRssList() },
                onRefresh = { viewModel.getRssList(true) },
                onSaveFav = { rss -> viewModel.saveFavorite(rss) },
                onDeleteFav = { rss -> viewModel.deleteFavorite(rss) },
                rssListState = viewModel.rssListState,
                featuredState = viewModel.featuredState,
                onPrivacyPolicyClicked = {
                    navigationActions.navigateToPrivacyPolicy()
                },
                onFavoritesClicked = { navigationActions.navigateToFavorites() },
                onArticleClicked = { link ->
                    navigationActions.navigateToWebView(link)
                },
                speechFlow = speechFlow,
                onSpeechButtonClicked = onSpeechButtonClicked,
                onPerformSearch = { searchTerm ->
                    if (searchTerm.isNotEmpty()) {
                        navigationActions.navigateToSearch(searchTerm)
                    }
                },
                onPerformSearchWhileTyping = {searchTerm ->
                    searchViewModel.searchRss(searchTerm)
                                             },
                searchResultFlowWhileTyping = searchViewModel.rssListState)
        }

        composable(
            Screen.HomeOaklandScreen.route
        ) {
            Constants.rssUrl = Constants.HOODLINE_OAKLAND_RSS_URL
            val viewModel = hiltViewModel<RssViewModel>()
            val searchViewModel = hiltViewModel<SearchViewModel>()
            RssListScreen(
                isExpandedScreen = isExpandedScreen,
                openDrawer = openDrawer,
                onGetRss = { viewModel.getRssList() },
                onRefresh = { viewModel.getRssList(true) },
                onSaveFav = { rss -> viewModel.saveFavorite(rss) },
                onDeleteFav = { rss -> viewModel.deleteFavorite(rss) },
                rssListState = viewModel.rssListState,
                featuredState = viewModel.featuredState,
                onPrivacyPolicyClicked = {
                    navigationActions.navigateToPrivacyPolicy()
                },
                onFavoritesClicked = { navigationActions.navigateToFavorites() },
                onArticleClicked = { link ->
                    navigationActions.navigateToWebView(link)
                },
                speechFlow = speechFlow,
                onSpeechButtonClicked = onSpeechButtonClicked,
                onPerformSearch = { searchTerm ->
                    if (searchTerm.isNotEmpty()) {
                        navigationActions.navigateToSearch(searchTerm)
                    }
                },
                onPerformSearchWhileTyping = {searchTerm ->
                    searchViewModel.searchRss(searchTerm)
                },
                searchResultFlowWhileTyping = searchViewModel.rssListState)
        }

        composable(
            Screen.FavoritesScreen.route
        ) {
            val viewModel = hiltViewModel<FavoritesViewModel>()
            FavoritesScreen(
                isExpandedScreen = isExpandedScreen,
                openDrawer = openDrawer,
                onGoBackClicked = { navController.popBackStack() },
                onGetFavorites = { viewModel.getFavorites() },
                onSaveFav = { rss -> viewModel.saveFavorite(rss) },
                onDeleteFav = { rss -> viewModel.deleteFavorite(rss) },
                state = viewModel.favoritesState,
                onArticleClicked = { link ->
                    navigationActions.navigateToWebView(link)
                })
        }

        composable(
            Screen.DetailsScreen.route + "/{link}",
            arguments = listOf(
                navArgument("link") {
                    type = NavType.StringType
                })
        ) { backStackEntry ->
            WebViewScreen(
                url = backStackEntry.arguments?.getString("link") ?: "",
                isExpandedScreen = isExpandedScreen,
                openDrawer = openDrawer,
                onGoBackClicked = { navController.popBackStack() }
            )
        }

        composable(
            Screen.ContactInfoScreen.route
        ) {
            ContactInfoScreen(
                isExpandedScreen = isExpandedScreen,
                openDrawer = openDrawer,
                onGoBackClicked = { navController.popBackStack() })
        }

        composable(
            Screen.PrivacyPolicyScreen.route
        ) {
            PrivacyPolicyScreen(
                isExpandedScreen = isExpandedScreen,
                openDrawer = openDrawer,
                onGoBackClicked = { navController.popBackStack() })
        }

        composable(
            Screen.SearchScreen.route + "/{${Constants.SEARCH_TERM_KEY}}",
            arguments = listOf(
                navArgument(Constants.SEARCH_TERM_KEY) {
                    type = NavType.StringType
                })
        ) { backStackEntry ->
            val searchTerm = backStackEntry.arguments?.getString(Constants.SEARCH_TERM_KEY) ?: ""
            val viewModel = hiltViewModel<SearchViewModel>()
            val searchWhileTypingViewModel = hiltViewModel<SearchWhileTypingViewModel>()
            val searchFor = stringResource(id = R.string.search_for)
            val title = remember {
                mutableStateOf(searchFor + " " + searchTerm)
            }
            SearchScreen(
                isExpandedScreen = isExpandedScreen,
                onSaveFav = { rss -> viewModel.saveFavorite(rss) },
                onDeleteFav = { rss -> viewModel.deleteFavorite(rss) },
                rssListState = viewModel.rssListState,
                onArticleClicked = { link ->
                    navigationActions.navigateToWebView(link)
                },
                speechFlow = speechFlow,
                onSpeechButtonClicked = onSpeechButtonClicked,
                onPerformSearch = {
                    if (it.isNotEmpty()) {
                        title.value = searchFor + " " + it
                        viewModel.searchRss(it)
                    }
                },
                onPerformSearchWhileTyping = {searchTerm ->
                    searchWhileTypingViewModel.searchRss(searchTerm)
                },
                searchResultFlowWhileTyping = searchWhileTypingViewModel.rssListState,
                onGoBack = { navController.popBackStack() },
                title = title)
        }

    }
}