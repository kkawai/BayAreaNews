package com.kk.android.bayareanews.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.kk.android.bayareanews.presentation.ui.common.Screen
import com.kk.android.bayareanews.presentation.ui.common.WebViewScreen
import com.kk.android.bayareanews.presentation.ui.home_screen.FavoritesScreen
import com.kk.android.bayareanews.presentation.ui.home_screen.FavoritesViewModel
import com.kk.android.bayareanews.presentation.ui.home_screen.PrivacyPolicyScreen
import com.kk.android.bayareanews.presentation.ui.home_screen.RssListScreen
import com.kk.android.bayareanews.presentation.ui.home_screen.RssViewModel

@Composable
fun NewsNavHost(
    isExpandedScreen: Boolean,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    openDrawer: () -> Unit = {},
    startDestination: String = Screen.HomeScreen.route,
    contentPadding: PaddingValues = PaddingValues(0.dp)) {

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
                    navController.navigate(Screen.PrivacyPolicyScreen.route)
                },
                onFavoritesClicked = { navController.navigate(Screen.FavoritesScreen.route) },
                contentPadding = contentPadding,
                onArticleClicked = { link ->
                    navController.navigate(Screen.DetailsScreen.route + "/${link}")
                })
        }

        composable(
            Screen.FavoritesScreen.route
        ) {
            val viewModel = hiltViewModel<FavoritesViewModel>()
            FavoritesScreen(
                onGoBackClicked = { navController.popBackStack() },
                contentPadding = contentPadding,
                onGetFavorites = { viewModel.getFavorites() },
                onSaveFav = { rss -> viewModel.saveFavorite(rss) },
                onDeleteFav = { rss -> viewModel.deleteFavorite(rss) },
                state = viewModel.favoritesState,
                onArticleClicked = { link ->
                    navController.navigate(Screen.DetailsScreen.route + "/${link}")
                })
        }

        composable(
            Screen.DetailsScreen.route + "/{link}",
            arguments = listOf(
                navArgument("link") {
                    type = NavType.StringType
                })
        ) { backStackEntry ->
            WebViewScreen(backStackEntry.arguments?.getString("link") ?: "")
        }

        composable(
            Screen.PrivacyPolicyScreen.route
        ) {
            PrivacyPolicyScreen(onGoBackClicked = { navController.popBackStack() })
        }

    }
}