package com.kk.android.bayareanews

import com.kk.android.bayareanews.common.Constants
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
            val rssFeedHolder2 = fakeRssRepository.getRssArticles(false, Constants.HOODLINE_RSS_URL, Constants.HOODLINE_CATEGORY)
            Assert.assertEquals(2, rssFeedHolder2.rss.size)
            Assert.assertEquals(2, rssFeedHolder2.favorites.size)
        }
    }
}