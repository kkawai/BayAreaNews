package com.kk.android.bayareanews.presentation

sealed class Screen(val route: String) {
    object HomeScreen : Screen("list_articles")
    object DetailsScreen : Screen("full_article_screen")
}