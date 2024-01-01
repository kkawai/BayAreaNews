package com.kk.android.bayareanews.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
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
            val viewModel = hiltViewModel<RssViewModel>()
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
                    if (searchTerm.isNotBlank()) {
                        navigationActions.navigateToSearch(searchTerm)
                    }
                })
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
            Screen.SearchScreen.route + "/{searchTerm}",
            arguments = listOf(
                navArgument("searchTerm") {
                    type = NavType.StringType
                })
        ) { backStackEntry ->
            val searchTerm = backStackEntry.arguments?.getString("searchTerm") ?: ""
            val viewModel = hiltViewModel<SearchViewModel>()
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
                    if (it.isNotBlank()) {
                        viewModel.searchRss(it)
                    }
                },
                onGoBack = { navController.popBackStack() },
                searchTerm = searchTerm)
        }

    }
}