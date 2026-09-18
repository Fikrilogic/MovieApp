package com.fikrisandi.parkeemovieapp.data.repository

import com.fikrisandi.parkeemovieapp.BuildConfig
import com.fikrisandi.parkeemovieapp.data.remote.MovieService
import com.fikrisandi.parkeemovieapp.data.remote.model.MovieDto
import com.fikrisandi.parkeemovieapp.data.remote.model.ReviewDto
import com.fikrisandi.parkeemovieapp.data.remote.model.ReviewListResponse
import com.fikrisandi.parkeemovieapp.domain.model.Movie
import com.fikrisandi.parkeemovieapp.domain.model.Review
import com.fikrisandi.parkeemovieapp.domain.model.ReviewPagination
import com.fikrisandi.parkeemovieapp.domain.repository.MovieRepository
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val movieService: MovieService
) : MovieRepository {

    override suspend fun getMoviesPopular(page: Int): Pair<List<Movie>, Int> {
        val data = movieService.getPopularMovies(page)
        return Pair(data.results.map { it.toDomain() }, data.page)
    }

    override suspend fun getMoviesNowPlaying(page: Int): Pair<List<Movie>, Int> {
        val data = movieService.getNowPlayingMovies(page)
        return Pair(data.results.map { it.toDomain() }, data.page)
    }

    override suspend fun getMoviesTopRated(page: Int): Pair<List<Movie>, Int> {
        val data = movieService.getTopRatedMovies(page)
        return Pair(data.results.map { it.toDomain() }, data.page)
    }

    override suspend fun getMovieDetail(movieId: Int): Movie {
        return movieService.getMovieDetail(movieId).toDomain()
    }

    override suspend fun getMovieReviews(movieId: Int, page: Int): ReviewPagination {
        return movieService.getMovieReviews(movieId, page).toDomain()
    }

    private fun MovieDto.toDomain(): Movie = Movie(
        id = id,
        title = title,
        posterPath = posterPath?.let { "${BuildConfig.IMAGE_URL}$it" } ?: "",
        overview = overview,
        releaseDate = releaseDate ?: "",
        rating = voteAverage
    )

    private fun ReviewDto.toDomain(): Review = Review(
        id = id,
        author = author,
        content = content,
        createdAt = createdAt
    )


    private fun ReviewListResponse.toDomain(): ReviewPagination = ReviewPagination(
        totalPages = totalPages,
        results = results.map { it.toDomain() },
        page = page,
        totalResults = totalResults,
        id = id
    )
}
