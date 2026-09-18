package com.fikrisandi.parkeemovieapp.data.repository

import com.fikrisandi.parkeemovieapp.data.local.dao.MovieDao
import com.fikrisandi.parkeemovieapp.data.local.entity.MovieEntity
import com.fikrisandi.parkeemovieapp.data.remote.MovieService
import com.fikrisandi.parkeemovieapp.data.remote.model.MovieDto
import com.fikrisandi.parkeemovieapp.data.remote.model.MovieListResponse
import com.fikrisandi.parkeemovieapp.domain.model.Movie
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.just
import io.mockk.runs
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MovieRepositoryImplTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    private lateinit var mockMovieService: MovieService

    @MockK
    private lateinit var mockMovieDao: MovieDao

    @InjectMockKs
    private lateinit var repository: MovieRepositoryImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `getMoviesPopular should request remote server and map DTO data correctly`() = runBlocking {
        val dto = MovieDto(
            id = 1,
            title = "Popular DTO",
            posterPath = "/path.jpg",
            overview = "Overview",
            releaseDate = "2026",
            voteAverage = 8.0
        )
        val response = MovieListResponse(results = listOf(dto), page = 1, totalPages = 5)

        coEvery { mockMovieService.getPopularMovies(1) } returns response
        val result = repository.getMoviesPopular(page = 1)

        assertEquals(1, result.second) // page
        assertEquals(1, result.first.size)
        assertEquals("Popular DTO", result.first[0].title)
        assertEquals(8.0, result.first[0].rating, 0.0)
        coVerify(exactly = 1) { mockMovieService.getPopularMovies(1) }
    }

    @Test
    fun `getMovieDetail should contact service and map parameters safely`() = runBlocking {
        val movieId = 12
        val posterPath = "/test_path.jpg"
        val dto = MovieDto(
            id = 12,
            title = "Detail DTO",
            posterPath = posterPath,
            overview = "Summary",
            releaseDate = "2026-01-01",
            voteAverage = 7.5
        )
        coEvery { mockMovieService.getMovieDetail(movieId) } returns dto

        val result = repository.getMovieDetail(movieId = movieId)

        assertEquals(12, result.id)
        assertEquals("Detail DTO", result.title)
        assertTrue(posterPath, result.posterPath.contains(posterPath))
        assertEquals("2026-01-01", result.releaseDate)
        coVerify(exactly = 1) { mockMovieService.getMovieDetail(movieId) }
    }

    @Test
    fun `getFavoriteMovies should load everything from local Room DAO`() = runBlocking {

        val entity1 = MovieEntity(
            id = 101,
            title = "Local 1",
            posterPath = "",
            overview = "",
            releaseDate = "",
            rating = 6.0
        )
        val entity2 = MovieEntity(
            id = 102,
            title = "Local 2",
            posterPath = "",
            overview = "",
            releaseDate = "",
            rating = 7.0
        )

        coEvery { mockMovieDao.getAll() } returns listOf(entity1, entity2)

        val favorites = repository.getFavoriteMovies()

        assertEquals(2, favorites.size)
        assertEquals("Local 1", favorites[0].title)
        coVerify(exactly = 1) { mockMovieDao.getAll() }
    }

    @Test
    fun `addFavoriteMovie should convert domain model down to local Room entity and insert`() =
        runBlocking {

            val domainMovie = Movie(
                id = 55,
                title = "To Store",
                posterPath = "pic.png",
                overview = "txt",
                releaseDate = "2026",
                rating = 9.9
            )
            val expectedEntity = MovieEntity(
                id = 55,
                title = "To Store",
                posterPath = "pic.png",
                overview = "txt",
                releaseDate = "2026",
                rating = 9.9
            )

            coEvery { mockMovieDao.insert(any()) } just runs

            repository.addFavoriteMovie(domainMovie)

            coVerify(exactly = 1) { mockMovieDao.insert(expectedEntity) }
        }

    @Test
    fun `deleteFavoriteMovie should trigger specific deleteById instruction on DAO`() =
        runBlocking {

            val domainMovie = Movie(
                id = 55,
                title = "To Store",
                posterPath = "pic.png",
                overview = "txt",
                releaseDate = "2026",
                rating = 9.9
            )
            val expectedEntity = MovieEntity(
                id = 55,
                title = "To Store",
                posterPath = "pic.png",
                overview = "txt",
                releaseDate = "2026",
                rating = 9.9
            )


            coEvery { mockMovieDao.deleteById(any()) } just runs

            repository.deleteFavoriteMovie(domainMovie)

            coVerify(exactly = 1) { mockMovieDao.deleteById(55) }
        }

//    private class MockMovieService : MovieService {
//        var popularResponse = MovieListResponse(emptyList(), 1, 1)
//        var detailResponse = MovieDto(0, "", null, "", null, 0.0)
//
//        override suspend fun getPopularMovies(page: Int): MovieListResponse = popularResponse
//        override suspend fun getNowPlayingMovies(page: Int): MovieListResponse = MovieListResponse(emptyList(), 1, 1)
//        override suspend fun getTopRatedMovies(page: Int): MovieListResponse = MovieListResponse(emptyList(), 1, 1)
//        override suspend fun getMovieDetail(movieId: Int): MovieDto = detailResponse
//
//        override suspend fun getMovieReviews(movieId: Int, page: Int): ReviewListResponse {
//            return ReviewListResponse(emptyList(), 1, 1, 1, 0)
//        }
//    }
//
//    private class MockMovieDao : MovieDao {
//        private val dbMap = mutableMapOf<Int, MovieEntity>()
//
//        override suspend fun getAll(): List<MovieEntity> = dbMap.values.toList()
//        override suspend fun getById(movieId: Int): MovieEntity? = dbMap[movieId]
//
//        override suspend fun insert(movie: MovieEntity) {
//            dbMap[movie.id] = movie
//        }
//
//        override suspend fun delete(movie: MovieEntity) {
//            dbMap.remove(movie.id)
//        }
//
//        override suspend fun deleteById(movieId: Int) {
//            dbMap.remove(movieId)
//        }
//    }
}
