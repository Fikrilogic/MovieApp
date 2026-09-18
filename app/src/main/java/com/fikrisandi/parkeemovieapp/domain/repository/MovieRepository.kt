package com.fikrisandi.parkeemovieapp.domain.repository

import com.fikrisandi.parkeemovieapp.domain.model.Movie
import com.fikrisandi.parkeemovieapp.domain.model.ReviewPagination


interface MovieRepository {
    suspend fun getMoviesPopular(page: Int): Pair<List<Movie>, Int>
    suspend fun getMoviesTopRated(page: Int): Pair<List<Movie>, Int>
    suspend fun getMoviesNowPlaying(page: Int): Pair<List<Movie>, Int>
    suspend fun getMovieDetail(movieId: Int): Movie
    suspend fun getMovieReviews(movieId: Int, page: Int): ReviewPagination

    suspend fun getFavoriteMovies(): List<Movie>
    suspend fun getFavoriteMovie(id: Int): Movie?
    suspend fun addFavoriteMovie(movie: Movie)
    suspend fun deleteFavoriteMovie(movie: Movie)
}
