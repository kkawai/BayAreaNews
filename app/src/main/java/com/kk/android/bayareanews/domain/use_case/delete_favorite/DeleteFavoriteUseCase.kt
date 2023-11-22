package com.kk.android.bayareanews.domain.use_case.get_favorites

import com.kk.android.bayareanews.common.Resource
import com.kk.android.bayareanews.domain.repository.RssRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class DeleteFavoriteUseCase @Inject constructor(private val rssRepository: RssRepository) {

    operator fun invoke(articleId: String): Flow<Resource<Int>> = flow {

        try {
            emit(Resource.Loading())
            val result = rssRepository.deleteFavoriteArticleByArticleId(articleId)
            emit(Resource.Success(result))
        } catch (e: IOException) {
            emit(Resource.Error("Internet connection error.  Please try again: " + e))
        } catch (e: Exception) {
            emit(Resource.Error("Unexpected error occurred.  Pls try again: " + e))
        }

    }
}