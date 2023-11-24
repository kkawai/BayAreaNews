package com.kk.android.bayareanews.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.kk.android.bayareanews.presentation.ui.common.Screen
import com.kk.android.bayareanews.presentation.ui.home_screen.RssListScreen
import com.kk.android.bayareanews.presentation.ui.common.WebViewScreen
import com.kk.android.bayareanews.presentation.ui.home_screen.FavoritesScreen

@Composable
fun NewsNavHost(contentPadding: PaddingValues = PaddingValues(0.dp)) {

    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.HomeScreen.route
    ) {

        composable(
            Screen.HomeScreen.route
        ) {
            RssListScreen(
                onPrivacyPolicyClicked = { link ->
                    navController.navigate(Screen.PrivacyPolicyScreen.route + "/${link}")
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
            FavoritesScreen(
                onGoBackClicked = { navController.popBackStack() },
                contentPadding = contentPadding,
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
            Screen.PrivacyPolicyScreen.route + "/{link}",
            arguments = listOf(
                navArgument("link") {
                    type = NavType.StringType
                })
        ) { backStackEntry ->
            WebViewScreen(backStackEntry.arguments?.getString("link") ?: "")
        }

    }
}