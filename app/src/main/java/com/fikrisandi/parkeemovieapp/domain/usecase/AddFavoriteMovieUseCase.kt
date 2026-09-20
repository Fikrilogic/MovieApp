package com.fikrisandi.parkeemovieapp.domain.usecase

import android.util.Log
import com.fikrisandi.parkeemovieapp.domain.model.Movie
import com.fikrisandi.parkeemovieapp.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AddFavoriteMovieUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    operator fun invoke(movie: Movie): Flow<Unit> = flow {
        movieRepository.getFavoriteMovie(movie.id).onSuccess { favorite ->
            Log.d("tagg", "invoke: favorite $favorite")
            val result = if (favorite != null) {
                movieRepository.deleteFavoriteMovie(movie)
            } else {
            Log.d("tagg", "call add favorite")
                movieRepository.addFavoriteMovie(movie)
            }

            result.onSuccess {
                emit(Unit)
            }.onFailure {
                error(it.message ?: "Something Error when update favorite")
            }
        }.onFailure {
            error(it.message ?: "Something Error when add favorite")
        }
    }
}