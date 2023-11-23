package com.kk.android.bayareanews.domain.use_case.get_rss

import com.kk.android.bayareanews.common.Resource
import com.kk.android.bayareanews.domain.model.Rss
import com.kk.android.bayareanews.domain.repository.RssRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class GetFavoritesUseCase @Inject constructor(private val rssRepository: RssRepository) {

    operator fun invoke(): Flow<Resource<List<Rss>>> = flow {

        try {
            emit(Resource.Loading())
            val favorites = rssRepository.getFavoriteArticles()
            emit(Resource.Success(favorites))
        } catch (e: IOException) {
            emit(Resource.Error("Internet connection error.  Please try again: " + e))
        } catch (e: Exception) {
            emit(Resource.Error("Unexpected error occurred.  Pls try again: " + e))
        }

    }
}