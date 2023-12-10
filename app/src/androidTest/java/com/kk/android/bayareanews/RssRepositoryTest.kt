package com.kk.android.bayareanews

import com.kk.android.bayareanews.domain.use_case.get_favorites.DeleteFavoriteUseCase
import com.kk.android.bayareanews.domain.use_case.get_favorites.SaveFavoriteUseCase
import com.kk.android.bayareanews.domain.use_case.get_rss.GetFeaturedUseCase
import com.kk.android.bayareanews.domain.use_case.get_rss.GetRssUseCase
import com.kk.android.bayareanews.presentation.ui.home_screen.RssViewModel
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import rules.TestDispatcherRule

class RssRepositoryTest {

    @get:Rule
    val testDispatcher = TestDispatcherRule()

    @Test
    fun rssRssRepositoryTest() {
        runTest {
            val fakeRssRepository = FakeRssRepository()
            val rssFeedHolder = fakeRssRepository.getFeaturedArticles(false)
            Assert.assertEquals(2, rssFeedHolder.rss.size)
            Assert.assertEquals(0, rssFeedHolder.favorites.size)
        }
    }
}