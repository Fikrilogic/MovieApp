package com.fikrisandi.parkeemovieapp.domain.usecase

import com.fikrisandi.parkeemovieapp.domain.model.Movie
import com.fikrisandi.parkeemovieapp.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetMoviesPopularUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    operator fun invoke(page: Int): Flow<Pair<List<Movie>, Int>> = flow {
        movieRepository.getMoviesPopular(page).onSuccess {
            emit(it)
        }.onFailure {
            error(it.message ?: "Something Error when get popular movies")
        }
    }
}
