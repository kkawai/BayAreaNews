package com.kk.android.bayareanews.domain.use_case.get_rss

import com.kk.android.bayareanews.common.Resource
import com.kk.android.bayareanews.domain.model.RssFeedHolder
import com.kk.android.bayareanews.domain.repository.RssRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class GetFeaturedUseCase @Inject constructor(private val rssRepository: RssRepository) {

    operator fun invoke( refresh: Boolean): Flow<Resource<RssFeedHolder>> = flow {
        try {
            emit(Resource.Loading())
            val rssFeedHolder = rssRepository.getFeaturedArticles(refresh)
            emit(Resource.Success(rssFeedHolder))
        } catch (e: IOException) {
            emit(Resource.Error("Internet connection error.  Please try again: " + e))
        } catch (e: Exception) {
            emit(Resource.Error("Unexpected error occurred.  Pls try again: " + e))
        }

    }
}