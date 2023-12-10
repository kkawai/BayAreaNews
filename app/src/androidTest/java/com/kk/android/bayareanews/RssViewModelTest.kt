package com.kk.android.bayareanews

import com.kk.android.bayareanews.common.MLog
import com.kk.android.bayareanews.domain.use_case.get_favorites.DeleteFavoriteUseCase
import com.kk.android.bayareanews.domain.use_case.get_favorites.SaveFavoriteUseCase
import com.kk.android.bayareanews.domain.use_case.get_rss.GetFeaturedUseCase
import com.kk.android.bayareanews.domain.use_case.get_rss.GetRssUseCase
import com.kk.android.bayareanews.domain.use_case.get_rss.RssFeaturedState
import com.kk.android.bayareanews.domain.use_case.get_rss.RssListState
import com.kk.android.bayareanews.presentation.ui.home_screen.RssViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import rules.TestDispatcherRule
import kotlin.time.Duration.Companion.seconds

class RssViewModelTest {

    @get:Rule
    val testDispatcher = TestDispatcherRule()

    @Test
    fun rssViewModelTest() {
        runTest {

            var job: Job? = null
            var job2: Job? = null
            var counter = 0
            var counter2 = 0
            val flowCollector = FlowCollector<RssListState> {rssListState ->
                counter++
                MLog.i("nnnnn", "RssListState $counter isLoading: ${rssListState.isLoading} topTitle ${rssListState.topRss.title} list: ${rssListState.rssList}")
                if (counter == 2) {  //loading state
                    assertEquals(true, rssListState.isLoading)
                    assertEquals(0, rssListState.rssList.size)
                    assertNull(rssListState.topRss.title )
                }
                if (counter == 3) {  //fetched state
                    assertEquals(false, rssListState.isLoading)
                    assertEquals(1, rssListState.rssList.size)
                    assertTrue(rssListState.topRss.title.isNotEmpty())
                    assertEquals(2, rssListState.favoritesMap.size)
                    job?.cancel()
                }
            }
            val flowCollector2 = FlowCollector<RssFeaturedState> { rssFeaturedState ->
                counter2++
                MLog.i("nnnnn", "RssFeaturedState $counter2 isLoading: ${rssFeaturedState.isLoading} featured ${rssFeaturedState.featuredRss} ")
                if (counter2 == 2) {  //loading state
                    assertEquals(0, rssFeaturedState.featuredRss.size)
                    assertEquals(true, rssFeaturedState.isLoading)
                }
                if (counter2 == 3) {  //fetched state
                    assertEquals(2, rssFeaturedState.featuredRss.size)
                    assertEquals(false, rssFeaturedState.isLoading)
                    job2?.cancel()
                }
            }
            val viewModel = RssViewModel(GetRssUseCase(FakeRssRepository()),
                GetFeaturedUseCase(FakeRssRepository()),
                SaveFavoriteUseCase(FakeRssRepository()),
                DeleteFavoriteUseCase(FakeRssRepository())
            )
            coroutineScope {
                job = launch {
                    viewModel.rssListState.collect(flowCollector)
                }
                job?.start()
                job2 = launch {
                    viewModel.featuredState.collect(flowCollector2)
                }
                job2?.start()
            }
        }
    }

    @Test
    fun exampleTest() = runTest {
        val deferred = async {
            delay(1.seconds)
            async {
                delay(1.seconds)
            }.await()
        }

        deferred.await() // result available immediately
    }

}