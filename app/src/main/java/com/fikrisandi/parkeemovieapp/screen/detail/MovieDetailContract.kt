package com.fikrisandi.parkeemovieapp.screen.detail

import com.fikrisandi.parkeemovieapp.domain.model.Movie
import com.fikrisandi.parkeemovieapp.domain.model.Review


data class MovieDetailUiState(
    val movie: Movie? = null,
    val reviews: List<Review> = emptyList(),
    val isLoadingReviews: Boolean = false,
    val isFavorite: Boolean = false,
    val toastMessage: String? = null
)
