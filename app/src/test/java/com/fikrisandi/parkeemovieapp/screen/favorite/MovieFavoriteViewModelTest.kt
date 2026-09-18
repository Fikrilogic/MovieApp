package com.fikrisandi.parkeemovieapp.screen.favorite

import com.fikrisandi.parkeemovieapp.domain.model.Movie
import com.fikrisandi.parkeemovieapp.domain.usecase.GetMoviesFavoriteUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MovieFavoriteViewModelTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    private lateinit var getMoviesFavoriteUseCase: GetMoviesFavoriteUseCase

    @InjectMockKs
    private lateinit var viewModel: MovieFavoriteViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        MockKAnnotations.init(this)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadMoviesFavorite should update state with movies list on success`() = runBlocking {
        val movie1 = Movie(
            id = 1,
            title = "Movie 1",
            posterPath = "",
            overview = "",
            releaseDate = "",
            rating = 8.0
        )
        val movie2 = Movie(
            id = 2,
            title = "Movie 2",
            posterPath = "",
            overview = "",
            releaseDate = "",
            rating = 7.5
        )

        coEvery { getMoviesFavoriteUseCase() } returns listOf(movie1, movie2)

        viewModel.loadMoviesFavorite()

        val currentState = viewModel.uiState.value
        assertFalse(currentState.isLoading)
        assertEquals(2, currentState.moviesFavorite.size)
        assertEquals("Movie 1", currentState.moviesFavorite[0].title)

        coVerify(exactly = 1) { getMoviesFavoriteUseCase() }
    }

    @Test
    fun `loadMoviesFavorite should handle error transparently and reset loading state`() =
        runBlocking {
            coEvery { getMoviesFavoriteUseCase() } throws Exception("Test error")

            viewModel.loadMoviesFavorite()

            val currentState = viewModel.uiState.value
            assertFalse(currentState.isLoading)
            assertEquals(0, currentState.moviesFavorite.size)
        }

}
