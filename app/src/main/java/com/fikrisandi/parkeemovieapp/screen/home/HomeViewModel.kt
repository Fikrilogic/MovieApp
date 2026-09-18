package com.fikrisandi.parkeemovieapp.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fikrisandi.parkeemovieapp.domain.usecase.GetMoviesNowPlayingUseCase
import com.fikrisandi.parkeemovieapp.domain.usecase.GetMoviesPopularUseCase
import com.fikrisandi.parkeemovieapp.domain.usecase.GetMoviesTopRatedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getMoviesPopularUseCase: GetMoviesPopularUseCase,
    private val getMoviesTopRatedUseCase: GetMoviesTopRatedUseCase,
    private val getMoviesNowPlayingUseCase: GetMoviesNowPlayingUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadMoviesPopular()
        loadMoviesTopRated()
        loadMoviesNowPlaying()
    }


    fun loadMoviesPopular() {
        if (_uiState.value.loadingPopularMovie) return
        viewModelScope.launch {
            _uiState.update { it.copy(loadingPopularMovie = true) }
            try {
                val newMovies = getMoviesPopularUseCase(_uiState.value.moviesPopular.page)

                _uiState.update {
                    it.copy(
                        moviesPopular = it.moviesPopular.copy(movies = it.moviesPopular.movies + newMovies.first, page = newMovies.second + 1),
                        loadingPopularMovie = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(loadingPopularMovie = false) }
            }
        }
    }

    fun loadMoviesTopRated() {
        if (_uiState.value.loadingTopRatedMovie) return
        viewModelScope.launch {
            _uiState.update { it.copy(loadingTopRatedMovie = true) }
            try {
                val newMovies = getMoviesTopRatedUseCase(_uiState.value.moviesTopRated.page)

                _uiState.update {
                    it.copy(
                        moviesTopRated = it.moviesTopRated.copy(movies = it.moviesTopRated.movies + newMovies.first, page = newMovies.second + 1),
                        loadingTopRatedMovie = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(loadingTopRatedMovie = false) }
            }
        }
    }

    fun loadMoviesNowPlaying() {
        if (_uiState.value.loadingNowPlayingMovie) return
        viewModelScope.launch {
            _uiState.update { it.copy(loadingNowPlayingMovie = true) }
            try {
                val newMovies = getMoviesNowPlayingUseCase(_uiState.value.moviesNowPlaying.page)

                _uiState.update {
                    it.copy(
                        moviesNowPlaying = it.moviesNowPlaying.copy(movies = it.moviesNowPlaying.movies + newMovies.first, page = newMovies.second + 1),
                        loadingNowPlayingMovie = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(loadingNowPlayingMovie = false) }
            }
        }
    }
}