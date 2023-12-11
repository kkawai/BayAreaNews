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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.takeWhile
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

            var event2Counter = 0
            val viewModel = RssViewModel(GetRssUseCase(FakeRssRepository()),
                GetFeaturedUseCase(FakeRssRepository()),
                SaveFavoriteUseCase(FakeRssRepository()),
                DeleteFavoriteUseCase(FakeRssRepository())
            )
            /*
             * Demonstrates 2 different ways to collect/take changes in the state of a StateFlow
             * (rssListState AND featureRss)
             */
            coroutineScope {
                launch {
                    viewModel.rssListState.takeWhile {
                        it.isLoading != false && it.rssList.size != 1 && it.topRss.title == null
                    }
                }
                launch {
                    viewModel.featuredState.collect {
                        event2Counter++
                        if (it.isLoading == false && it.featuredRss.size == 2) {
                            MLog.i("nnnnn", "featuredState event count: $event2Counter cancel job")
                            cancel() //cancels the coroutine scope which in turn kills all jobs under it
                                     //in this case the collecting of featureRss events
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