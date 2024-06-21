package com.kk.android.bayareanews.presentation.ui.common

sealed class Screen(val route: String) {
    object HomeScreen : Screen("list_articles")
    object DetailsScreen : Screen("full_article_screen")
    object FavoritesScreen : Screen("favorites_screen")
    object PrivacyPolicyScreen : Screen("privacy_policy_screen")
    object ContactInfoScreen : Screen("contact_info_screen")

    object SearchScreen : Screen("search_articles")

    object HomeOaklandScreen : Screen("list_oakland_articles")
    object HomeNorthBayScreen : Screen("list_north_bay_articles")
    object HomeSanJoseScreen : Screen("list_san_jose_articles")
}