package com.kk.android.bayareanews

import androidx.compose.runtime.collectAsState
import com.kk.android.bayareanews.domain.use_case.get_favorites.DeleteFavoriteUseCase
import com.kk.android.bayareanews.domain.use_case.get_favorites.SaveFavoriteUseCase
import com.kk.android.bayareanews.domain.use_case.get_rss.GetFeaturedUseCase
import com.kk.android.bayareanews.domain.use_case.get_rss.GetRssUseCase
import com.kk.android.bayareanews.domain.use_case.get_rss.RssListState
import com.kk.android.bayareanews.presentation.ui.home_screen.RssViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
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
            val viewModel = RssViewModel(GetRssUseCase(FakeRssRepository()),
                GetFeaturedUseCase(FakeRssRepository()),
                SaveFavoriteUseCase(FakeRssRepository()),
                DeleteFavoriteUseCase(FakeRssRepository())
            )
            //todo this test isn't working yet
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