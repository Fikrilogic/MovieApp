package com.fikrisandi.parkeemovieapp.screen.home

import com.fikrisandi.parkeemovieapp.domain.model.Movie


data class HomeUiState(
    val moviesPopular: PaginationMovie = PaginationMovie(),
    val moviesTopRated: PaginationMovie = PaginationMovie(),
    val moviesNowPlaying: PaginationMovie = PaginationMovie(),
    val loadingPopularMovie: Boolean = false,
    val loadingTopRatedMovie: Boolean = false,
    val loadingNowPlayingMovie: Boolean = false,
    val errorPopularMovie: Throwable? = null,
    val errorTopRatedMovie: Throwable? = null,
    val errorNowPlayingMovie: Throwable? = null,
)

data class PaginationMovie(
    val page: Int = 1,
    val movies: List<Movie> = emptyList()
)