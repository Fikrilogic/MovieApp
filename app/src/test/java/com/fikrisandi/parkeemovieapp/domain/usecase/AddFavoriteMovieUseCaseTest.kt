package com.fikrisandi.parkeemovieapp.domain.usecase

import com.fikrisandi.parkeemovieapp.domain.model.Movie
import com.fikrisandi.parkeemovieapp.domain.repository.MovieRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.just
import io.mockk.runs
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AddFavoriteMovieUseCaseTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    private lateinit var mockMovieRepository: MovieRepository

    @InjectMockKs
    private lateinit var mockAddFavoriteMovieUseCase: AddFavoriteMovieUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `invoke should add movie to favorite when it does not exist locally`() = runBlocking {
        val movie = Movie(
            id = 1,
            title = "Test Movie",
            posterPath = "/path.jpg",
            overview = "Overview text",
            releaseDate = "2026-09-18",
            rating = 8.5
        )

        coEvery { mockMovieRepository.getFavoriteMovie(any()) } returns null
        coEvery { mockMovieRepository.addFavoriteMovie(any()) } just runs

        mockAddFavoriteMovieUseCase(movie)

        coVerify(exactly = 1) { mockMovieRepository.getFavoriteMovie(movie.id) }
        coVerify(exactly = 1) { mockMovieRepository.addFavoriteMovie(movie) }
        coVerify(exactly = 0) { mockMovieRepository.deleteFavoriteMovie(movie) }
    }

    @Test
    fun `invoke should remove movie from favorite when it already exists locally`() = runBlocking {

        val movie = Movie(
            id = 2,
            title = "Existing Movie",
            posterPath = "/existing.jpg",
            overview = "Existing overview",
            releaseDate = "2026-01-01",
            rating = 7.0
        )

        coEvery { mockMovieRepository.getFavoriteMovie(any()) } returns movie
        coEvery { mockMovieRepository.deleteFavoriteMovie(any()) } just runs

        mockAddFavoriteMovieUseCase(movie)

        coVerify(exactly = 1) { mockMovieRepository.getFavoriteMovie(movie.id) }
        coVerify(exactly = 1) { mockMovieRepository.deleteFavoriteMovie(movie) }
        coVerify(exactly = 0) { mockMovieRepository.addFavoriteMovie(movie) }
    }

}