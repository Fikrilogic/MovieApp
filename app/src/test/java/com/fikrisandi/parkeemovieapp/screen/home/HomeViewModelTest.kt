package com.fikrisandi.parkeemovieapp.screen.home

import com.fikrisandi.parkeemovieapp.domain.model.Movie
import com.fikrisandi.parkeemovieapp.domain.usecase.GetMoviesNowPlayingUseCase
import com.fikrisandi.parkeemovieapp.domain.usecase.GetMoviesPopularUseCase
import com.fikrisandi.parkeemovieapp.domain.usecase.GetMoviesTopRatedUseCase
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
class HomeViewModelTest {

    @get:Rule
    val mockkRule = MockKRule(this)


    @MockK
    private lateinit var getMoviesPopularUseCase: GetMoviesPopularUseCase

    @MockK
    private lateinit var getMoviesTopRatedUseCase: GetMoviesTopRatedUseCase

    @MockK
    private lateinit var getMoviesNowPlayingUseCase: GetMoviesNowPlayingUseCase

    @InjectMockKs
    private lateinit var viewModel: HomeViewModel
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
    fun `loadMoviesPopular should update state with movies list and increment page`() =
        runBlocking {
            val movie = Movie(
                id = 1,
                title = "Popular Movie",
                posterPath = "",
                overview = "",
                releaseDate = "",
                rating = 8.0
            )

            coEvery { getMoviesPopularUseCase(any()) } returns Pair(listOf(movie), 1)

            viewModel.loadMoviesPopular()

            val state = viewModel.uiState.value
            assertFalse(state.loadingPopularMovie)
            assertEquals(1, state.moviesPopular.movies.size)
            assertEquals(2, state.moviesPopular.page)
            assertEquals("Popular Movie", state.moviesPopular.movies[0].title)

            coVerify(exactly = 1) { getMoviesPopularUseCase(1) }
        }

    @Test
    fun `loadMoviesTopRated should update state with top rated movies`() = runBlocking {
        val movie = Movie(
            id = 2,
            title = "Top Rated Movie",
            posterPath = "",
            overview = "",
            releaseDate = "",
            rating = 9.0
        )

        coEvery { getMoviesTopRatedUseCase(any()) } returns Pair(listOf(movie), 1)

        viewModel.loadMoviesTopRated()

        val state = viewModel.uiState.value
        assertFalse(state.loadingTopRatedMovie)
        assertEquals(1, state.moviesTopRated.movies.size)
        assertEquals(2, state.moviesTopRated.page)
        assertEquals("Top Rated Movie", state.moviesTopRated.movies[0].title)

        coVerify(exactly = 1) { getMoviesTopRatedUseCase(1) }
    }

    @Test
    fun `loadMoviesNowPlaying should update state with now playing movies`() = runBlocking {
        val movie = Movie(
            id = 3,
            title = "Now Playing Movie",
            posterPath = "",
            overview = "",
            releaseDate = "",
            rating = 7.0
        )

        coEvery { getMoviesNowPlayingUseCase(any()) } returns Pair(listOf(movie), 1)

        viewModel.loadMoviesNowPlaying()

        val state = viewModel.uiState.value
        assertFalse(state.loadingNowPlayingMovie)
        assertEquals(1, state.moviesNowPlaying.movies.size)
        assertEquals(2, state.moviesNowPlaying.page)
        assertEquals("Now Playing Movie", state.moviesNowPlaying.movies[0].title)

        coVerify(exactly = 1) { getMoviesNowPlayingUseCase(1) }
    }

    @Test
    fun `loadMoviesPopular should safely handle exception and clear loading flag`() = runBlocking {

        coEvery { getMoviesPopularUseCase(any()) } throws Exception("Network error")

        viewModel.loadMoviesPopular()

        val state = viewModel.uiState.value
        assertFalse(state.loadingPopularMovie)
        assertEquals(0, state.moviesPopular.movies.size)

        coVerify(exactly = 1) { getMoviesPopularUseCase(1) }
    }

    @Test
    fun `loadMoviesTopRated should safely handle exception and clear loading flag`() = runBlocking {

        coEvery { getMoviesTopRatedUseCase(any()) } throws Exception("Network error")

        viewModel.loadMoviesTopRated()

        val state = viewModel.uiState.value
        assertFalse(state.loadingTopRatedMovie)
        assertEquals(0, state.moviesTopRated.movies.size)

        coVerify(exactly = 1) { getMoviesTopRatedUseCase(1) }
    }

    @Test
    fun `loadMoviesNowPlaying should safely handle exception and clear loading flag`() =
        runBlocking {

            coEvery { getMoviesNowPlayingUseCase(any()) } throws Exception("Network error")

            viewModel.loadMoviesNowPlaying()

            val state = viewModel.uiState.value
            assertFalse(state.loadingNowPlayingMovie)
            assertEquals(0, state.moviesNowPlaying.movies.size)

            coVerify(exactly = 1) { getMoviesNowPlayingUseCase(1) }
        }
}
