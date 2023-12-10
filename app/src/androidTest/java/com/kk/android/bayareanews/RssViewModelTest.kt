package com.kk.android.bayareanews

import com.kk.android.bayareanews.common.MLog
import com.kk.android.bayareanews.domain.use_case.get_favorites.DeleteFavoriteUseCase
import com.kk.android.bayareanews.domain.use_case.get_favorites.SaveFavoriteUseCase
import com.kk.android.bayareanews.domain.use_case.get_rss.GetFeaturedUseCase
import com.kk.android.bayareanews.domain.use_case.get_rss.GetRssUseCase
import com.kk.android.bayareanews.presentation.ui.home_screen.RssViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
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

            var eventCounter = 0
            var event2Counter = 0
            val viewModel = RssViewModel(GetRssUseCase(FakeRssRepository()),
                GetFeaturedUseCase(FakeRssRepository()),
                SaveFavoriteUseCase(FakeRssRepository()),
                DeleteFavoriteUseCase(FakeRssRepository())
            )
            coroutineScope {
                launch {
                    //viewModel.rssListState.collect(flowCollector)
                    viewModel.rssListState.collect {
                        eventCounter++
                        if (it.isLoading == false && it.rssList.size == 1) {
                            MLog.i("nnnnn", "rssListState event count: $eventCounter cancel job")
                            cancel()
                        }
                    }
                }
                launch {
                    //viewModel.featuredState.collect(flowCollector2)
                    viewModel.featuredState.collect {
                        event2Counter++
                        if (it.isLoading == false && it.featuredRss.size == 2) {
                            MLog.i("nnnnn", "featuredState event count: $event2Counter cancel job")
                            cancel()
                        }
                    }
                }
            }
            MLog.i("nnnnn", "ViewModel test finished")
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