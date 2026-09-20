package com.fikrisandi.parkeemovieapp.domain.repository

import com.fikrisandi.parkeemovieapp.domain.model.Movie
import com.fikrisandi.parkeemovieapp.domain.model.ReviewPagination


interface MovieRepository {
    suspend fun getMoviesPopular(page: Int): Result<Pair<List<Movie>, Int>>
    suspend fun getMoviesTopRated(page: Int): Result<Pair<List<Movie>, Int>>
    suspend fun getMoviesNowPlaying(page: Int): Result<Pair<List<Movie>, Int>>
    suspend fun getMovieDetail(movieId: Int): Result<Movie>
    suspend fun getMovieReviews(movieId: Int, page: Int): Result<ReviewPagination>

    suspend fun getFavoriteMovies(): Result<List<Movie>>
    suspend fun getFavoriteMovie(id: Int): Result<Movie?>
    suspend fun addFavoriteMovie(movie: Movie): Result<Unit>
    suspend fun deleteFavoriteMovie(movie: Movie): Result<Unit>
}
