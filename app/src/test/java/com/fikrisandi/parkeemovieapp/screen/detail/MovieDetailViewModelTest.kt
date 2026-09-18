package com.fikrisandi.parkeemovieapp.screen.detail

import com.fikrisandi.parkeemovieapp.domain.model.Movie
import com.fikrisandi.parkeemovieapp.domain.model.Review
import com.fikrisandi.parkeemovieapp.domain.model.ReviewPagination
import com.fikrisandi.parkeemovieapp.domain.repository.MovieRepository
import com.fikrisandi.parkeemovieapp.domain.usecase.AddFavoriteMovieUseCase
import com.fikrisandi.parkeemovieapp.domain.usecase.GetMovieDetailUseCase
import com.fikrisandi.parkeemovieapp.domain.usecase.GetMovieFavoriteUseCase
import com.fikrisandi.parkeemovieapp.domain.usecase.GetMovieReviewsUseCase
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
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MovieDetailViewModelTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    private lateinit var mockGetMovieDetailUseCase: GetMovieDetailUseCase
    @MockK
    private lateinit var mockGetMovieReviewUseCase: GetMovieReviewsUseCase
    @MockK
    private lateinit var mockAddFavoriteMovieUseCase: AddFavoriteMovieUseCase
    @MockK
    private lateinit var mockGetMovieFavoriteUseCase: GetMovieFavoriteUseCase
    @InjectMockKs
    private lateinit var viewModel: MovieDetailViewModel
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
    fun `setMovieId should load movie detail and favorite status`() = runBlocking {
        val movieId = 1
        val movie = Movie(id = movieId, title = "Detail Movie", posterPath = "", overview = "", releaseDate = "", rating = 9.0)

        coEvery { mockGetMovieDetailUseCase(any()) } returns movie
        coEvery { mockGetMovieFavoriteUseCase(any()) } returns movie

        viewModel.setMovieId(movieId)

        assertEquals(movie, viewModel.uiState.value.movie)
        assertTrue(viewModel.uiState.value.isFavorite)
        coVerify(exactly = 1){ mockGetMovieDetailUseCase(movieId) }
        coVerify(exactly = 1){ mockGetMovieFavoriteUseCase(movieId) }
    }

    @Test
    fun `actionToFavorite should toggle favorite state and set success message when add favorite movie`() = runBlocking {
        val movie = Movie(id = 1, title = "Favorite Movie", posterPath = "", overview = "", releaseDate = "", rating = 9.0)

        coEvery { mockGetMovieDetailUseCase(movie.id) } returns movie
        coEvery { mockGetMovieFavoriteUseCase(movie.id) } returns null
        coEvery { mockAddFavoriteMovieUseCase(any()) } returns Unit

        viewModel.setMovieId(movie.id)

        viewModel.actionToFavorite()

        assertTrue(viewModel.uiState.value.isFavorite)
        assertEquals("Added to favorites", viewModel.uiState.value.toastMessage)

        coVerify(exactly = 1){ mockAddFavoriteMovieUseCase(movie) }
        coVerify(exactly = 1){ mockGetMovieFavoriteUseCase(movie.id) }
    }

    @Test
    fun `actionToFavorite should toggle favorite state and set success message for unfavorite a movie`() = runBlocking {
        val movie = Movie(id = 1, title = "Favorite Movie", posterPath = "", overview = "", releaseDate = "", rating = 9.0)

        coEvery { mockGetMovieDetailUseCase(any()) } returns movie
        coEvery { mockAddFavoriteMovieUseCase(any()) } returns Unit
        coEvery { mockGetMovieFavoriteUseCase(any()) } returns movie

        viewModel.setMovieId(movie.id)

        viewModel.actionToFavorite()

        assertFalse(viewModel.uiState.value.isFavorite)
        assertEquals("Removed from favorites", viewModel.uiState.value.toastMessage)
        coVerify(exactly = 1){ mockAddFavoriteMovieUseCase(movie) }
        coVerify(exactly = 1){ mockGetMovieFavoriteUseCase(movie.id) }
    }


    @Test
    fun `setMovieId should load data review from movie`() = runBlocking {
        val movieId = 1
        
        val dummyReviews = listOf(
            Review(id = "1", author = "Author 1", content = "Content 1", createdAt = "2026-01-01"),
            Review(id = "2", author = "Author 2", content = "Content 2", createdAt = "2026-01-02")
        )
        val listReview = ReviewPagination(
            id = movieId,
            page = 1,
            results = dummyReviews,
            totalPages = 1,
            totalResults = 2
        )

        coEvery { mockGetMovieReviewUseCase(any(), any()) } returns listReview

        viewModel.setMovieId(movieId)

        assertEquals(2, viewModel.uiState.value.reviews.size)
        assertEquals("Author 1", viewModel.uiState.value.reviews[0].author)
        coVerify(exactly = 1) { mockGetMovieReviewUseCase(movieId, 1) }
    }
}
