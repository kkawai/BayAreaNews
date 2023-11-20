package com.kk.android.bayareanews.domain.use_case.get_rss

import com.kk.android.bayareanews.common.Resource
import com.kk.android.bayareanews.domain.model.Rss
import com.kk.android.bayareanews.domain.repository.RssRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class GetRssUseCase @Inject constructor(private val rssRepository: RssRepository) {

    operator fun invoke(
        refresh: Boolean,
        rssUrl: String,
        category: String
    ): Flow<Resource<List<Rss>>> = flow {

        /* perform network IO here */          // Dispatchers.IO (main-safety block)
        try {
            emit(Resource.Loading())
            val list = rssRepository.getRssArticles(refresh, rssUrl, category)
            emit(Resource.Success(list))
        } catch (e: IOException) {
            emit(Resource.Error("Internet connection error.  Please try again: " + e))
        } catch (e: Exception) {
            emit(Resource.Error("Unexpected error occurred.  Pls try again: " + e))
        }

    }
}