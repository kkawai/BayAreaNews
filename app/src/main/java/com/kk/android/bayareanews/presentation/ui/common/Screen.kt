package com.kk.android.bayareanews.presentation.ui.common

sealed class Screen(val route: String) {
    object HomeScreen : Screen("list_articles")
    object DetailsScreen : Screen("full_article_screen")
    object FavoritesScreen : Screen("favorites_screen")
    object PrivacyPolicyScreen : Screen("privacy_policy_screen")
}