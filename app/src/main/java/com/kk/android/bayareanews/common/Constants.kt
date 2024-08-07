package com.kk.android.bayareanews.common

object Constants {

    const val HOODLINE_DEFAULT_RSS_URL = "https://hoodline.com/news/san-francisco/rss/"
    const val HOODLINE_OAKLAND_RSS_URL = "https://hoodline.com/news/oakland/rss/"
    const val HOODLINE_SAN_JOSE_RSS_URL = "https://hoodline.com/news/san-jose/rss/"
    const val HOODLINE_NORTH_BAY_RSS_URL = "https://hoodline.com/news/north-bay/rss/"

    const val HOODLINE_DEFAULT_TITLE = "San Francisco Stories"
    const val HOODLINE_OAKLAND_TITLE = "Oakland Top Stories"
    const val HOODLINE_SAN_JOSE_TITLE = "San Jose Top Stories"
    const val HOODLINE_NORTH_BAY_TITLE = "North Bay Top Stories"

    var currentAreaTitle = HOODLINE_DEFAULT_TITLE
    var rssUrl = HOODLINE_DEFAULT_RSS_URL

    const val HOODLINE_CATEGORY = "Bay Area Breaking News"
    const val SHARED_PREFS_HOODLINE_KEY = "hoodline_breaking_news"
    const val SHARED_PREFS_NAME = "com.kk.android.bayareanews.SHARED_PREFS"
    const val CONTENT_EXPIRE_TIME = 1000*60*60 * 6L  //6 hours
    const val PRIVACY_POLICY_URL =
        "https://raw.githack.com/kkawai/BayAreaNews/main/external/privacy_policy.html"
    const val FEATURED_CATEGORIES = "featured_categories"
    const val MAIN_CARDS_EXPANDED = "main_cards_expanded"
    const val FEATURED_CARDS_EXPANDED = "featured_cards_expanded"
    const val PRIVACY_POLICY_V2 = "privacy_policy"
    const val PRIVACY_POLICY_DEFAULT = "The app does not require, collect, transmit, store or use any personally identifiable information from its users in any way, shape, or form. Any \"Favorited\" content is not saved against any personally identifiable information and not associated to any form of social media whatsoever."
    const val CONTACT_INFO = "contact_info"
    const val CONTACT_INFO_DEFAULT = "This app contact:\nbayareanews933@gmail.com \n\n" +
            "hoodline contact:\ncontact@hoodline.com \n\n" +
            "wired contact:\nmail@wired.com"
    const val SEARCH_TERM_KEY = "searchTerm"
    const val PLAYSTORE_URL = "https://play.google.com/store/apps/details?id=com.kk.android.bayareanews"
}