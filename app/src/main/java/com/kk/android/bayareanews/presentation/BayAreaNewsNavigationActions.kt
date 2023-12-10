package com.kk.android.bayareanews.presentation

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.kk.android.bayareanews.presentation.ui.common.Screen

/**
 * Models the navigation actions in the app.
 */
class BayAreaNewsNavigationActions(navController: NavHostController) {
    val navigateToHome: () -> Unit = {
        navController.navigate(Screen.HomeScreen.route) {
            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations
            // on the back stack as users select items
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            // Avoid multiple copies of the same destination when
            // re-selecting the same item
            launchSingleTop = true
            // Restore state when reselecting a previously selected item
            restoreState = true
        }
    }
    val navigateToFavorites: () -> Unit = {
        navController.navigate(Screen.FavoritesScreen.route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
    val navigateToPrivacyPolicy: () -> Unit = {
        navController.navigate(Screen.PrivacyPolicyScreen.route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
    val navigateToWebView: (url: String) -> Unit = { link ->
        navController.navigate(Screen.DetailsScreen.route + "/${link}") {
        //unlike the others, do not call 'popUpTo' since WebView is not a drawer nav item
        }
    }
}