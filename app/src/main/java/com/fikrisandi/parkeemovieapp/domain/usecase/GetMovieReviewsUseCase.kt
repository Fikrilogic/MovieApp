package com.fikrisandi.parkeemovieapp.domain.usecase

import com.fikrisandi.parkeemovieapp.domain.model.ReviewPagination
import com.fikrisandi.parkeemovieapp.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetMovieReviewsUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    operator fun invoke(movieId: Int, page: Int): Flow<ReviewPagination> = flow {
        movieRepository.getMovieReviews(movieId, page).onSuccess {
            emit(it)
        }.onFailure {
            error(it.message ?: "Something Error when get movie reviews")
        }
    }
}
