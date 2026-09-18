package com.fikrisandi.parkeemovieapp.domain.model

data class Movie(
    val id: Int,
    val title: String,
    val posterPath: String,
    val overview: String,
    val releaseDate: String,
    val rating: Double,
    val trailerUrl: String = "https://www.youtube.com/watch?v=dQw4w9WgXcQ"
)
