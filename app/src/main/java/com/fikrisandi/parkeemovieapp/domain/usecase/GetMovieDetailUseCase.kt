package com.fikrisandi.parkeemovieapp.domain.usecase

import com.fikrisandi.parkeemovieapp.domain.model.Movie
import com.fikrisandi.parkeemovieapp.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetMovieDetailUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    operator fun invoke(movieId: Int): Flow<Movie> = flow {
        movieRepository.getMovieDetail(movieId).onSuccess {
            emit(it)
        }.onFailure {
            error(it.message ?: "Something Error when get movie detail")
        }
    }
}
