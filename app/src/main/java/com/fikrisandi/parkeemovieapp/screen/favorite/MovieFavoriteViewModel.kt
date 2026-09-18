package com.fikrisandi.parkeemovieapp.screen.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fikrisandi.parkeemovieapp.domain.usecase.GetMoviesFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieFavoriteViewModel @Inject constructor(
    private val getMoviesFavoriteUseCase: GetMoviesFavoriteUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(MovieFavoriteUiState())
    val uiState: StateFlow<MovieFavoriteUiState> = _uiState.asStateFlow()


    fun loadMoviesFavorite() {
        if (_uiState.value.isLoading) return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val newMovies = getMoviesFavoriteUseCase()

                _uiState.update {
                    it.copy(
                        moviesFavorite = newMovies,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false) }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }


}