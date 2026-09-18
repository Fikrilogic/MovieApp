package com.fikrisandi.parkeemovieapp.screen.favorite

import com.fikrisandi.parkeemovieapp.domain.model.Movie


data class MovieFavoriteUiState(
    val moviesFavorite: List<Movie> = emptyList(),
    val isLoading: Boolean = false
)
