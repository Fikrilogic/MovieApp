package com.fikrisandi.parkeemovieapp.data.repository

import com.fikrisandi.parkeemovieapp.BuildConfig
import com.fikrisandi.parkeemovieapp.data.local.dao.MovieDao
import com.fikrisandi.parkeemovieapp.data.local.entity.MovieEntity
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
    private val movieService: MovieService,
    private val movieDao: MovieDao
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

    override suspend fun getFavoriteMovies(): List<Movie> {
        return movieDao.getAll().map { it.toDomain() }
    }

    override suspend fun getFavoriteMovie(id: Int): Movie? {
        return movieDao.getById(id)?.toDomain()
    }

    override suspend fun addFavoriteMovie(movie: Movie) {
        movieDao.insert(movie.toEntity())
    }

    override suspend fun deleteFavoriteMovie(movie: Movie) {
        movieDao.deleteById(movie.id)
    }

    private fun MovieDto.toDomain(): Movie = Movie(
        id = id,
        title = title,
        posterPath = posterPath?.let { "${BuildConfig.IMAGE_URL}$it" } ?: "",
        overview = overview,
        releaseDate = releaseDate ?: "",
        rating = voteAverage
    )

    private fun MovieEntity.toDomain(): Movie = Movie(
        id = id,
        title = title,
        posterPath = posterPath,
        overview = overview,
        releaseDate = releaseDate,
        rating = rating
    )

    private fun Movie.toEntity(): MovieEntity = MovieEntity(
        id = id,
        title = title,
        posterPath = posterPath,
        overview = overview,
        releaseDate = releaseDate,
        rating = rating
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
