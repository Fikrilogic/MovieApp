package com.fikrisandi.parkeemovieapp.domain.usecase

import com.fikrisandi.parkeemovieapp.domain.model.Movie
import com.fikrisandi.parkeemovieapp.domain.repository.MovieRepository
import javax.inject.Inject

class AddFavoriteMovieUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(movie: Movie) {
        val checkFavorite = movieRepository.getFavoriteMovie(movie.id)

        if(checkFavorite != null) {
            return movieRepository.deleteFavoriteMovie(movie)
        }


        return movieRepository.addFavoriteMovie(movie)
    }
}