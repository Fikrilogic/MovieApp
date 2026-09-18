package com.fikrisandi.parkeemovieapp.screen.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fikrisandi.parkeemovieapp.domain.usecase.GetMovieDetailUseCase
import com.fikrisandi.parkeemovieapp.domain.usecase.GetMovieReviewsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val getMovieDetailUseCase: GetMovieDetailUseCase,
    private val getMovieReviewsUseCase: GetMovieReviewsUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(MovieDetailUiState())
    val uiState: StateFlow<MovieDetailUiState> = _uiState.asStateFlow()

    private var currentPage = 1
    private var isCanScroll = true
    private var currentMovieId: Int? = null

    fun setMovieId(movieId: Int) {
        if (currentMovieId == movieId) return
        currentMovieId = movieId
        loadMovieDetail(movieId)
        loadReviews(movieId)
    }

    private fun loadMovieDetail(movieId: Int) {
        viewModelScope.launch {
            try {
                val movie = getMovieDetailUseCase(movieId)
                _uiState.update { it.copy(movie = movie) }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun loadReviews(movieId: Int? = currentMovieId) {
        val id = movieId ?: return
        if (_uiState.value.isLoadingReviews || !isCanScroll) return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingReviews = true) }
            try {
                val newReviews = getMovieReviewsUseCase(id, currentPage)

                _uiState.update {
                    it.copy(
                        reviews = it.reviews + newReviews.results,
                        isLoadingReviews = false
                    )
                }
                if (currentPage >= newReviews.totalPages) {
                    isCanScroll = false
                }
                currentPage++
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoadingReviews = false) }
            }
        }
    }
}