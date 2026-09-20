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

    override suspend fun getMoviesPopular(page: Int): Result<Pair<List<Movie>, Int>> {
        try {
            val data = movieService.getPopularMovies(page)
            return Result.success(Pair(data.results.map { it.toDomain() }, data.page))
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override suspend fun getMoviesNowPlaying(page: Int): Result<Pair<List<Movie>, Int>> {
        try {
            val data = movieService.getNowPlayingMovies(page)
            return Result.success(Pair(data.results.map { it.toDomain() }, data.page))
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override suspend fun getMoviesTopRated(page: Int): Result<Pair<List<Movie>, Int>> {
        try {
            val data = movieService.getTopRatedMovies(page)
            return Result.success(Pair(data.results.map { it.toDomain() }, data.page))
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override suspend fun getMovieDetail(movieId: Int): Result<Movie> {
        try {
            return Result.success(movieService.getMovieDetail(movieId).toDomain())
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override suspend fun getMovieReviews(movieId: Int, page: Int): Result<ReviewPagination> {
        try {
            return Result.success(movieService.getMovieReviews(movieId, page).toDomain())
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override suspend fun getFavoriteMovies(): Result<List<Movie>> {
        try {
            return Result.success(movieDao.getAll().map { it.toDomain() })
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override suspend fun getFavoriteMovie(id: Int): Result<Movie?> {
        try {
            val data = movieDao.getById(id)?.toDomain()
            return Result.success(data)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override suspend fun addFavoriteMovie(movie: Movie): Result<Unit> {
        try {
            movieDao.insert(movie.toEntity())
            return Result.success(Unit)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override suspend fun deleteFavoriteMovie(movie: Movie): Result<Unit> {
        try {
            movieDao.deleteById(movie.id)
            return Result.success(Unit)
        } catch (e: Exception) {
            return Result.failure(e)
        }
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
