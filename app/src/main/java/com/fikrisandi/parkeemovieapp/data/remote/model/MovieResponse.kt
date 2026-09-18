package com.fikrisandi.parkeemovieapp.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieListResponse(
    val results: List<MovieDto>,
    val page: Int,
    @SerialName("total_pages") val totalPages: Int
)

@Serializable
data class MovieDto(
    val id: Int,
    val title: String,
    @SerialName("poster_path") val posterPath: String?,
    val overview: String,
    @SerialName("release_date") val releaseDate: String?,
    @SerialName("vote_average") val voteAverage: Double
)

@Serializable
data class GenreListResponse(
    val genres: List<GenreDto>
)

@Serializable
data class GenreDto(
    val id: Int,
    val name: String
)

@Serializable
data class ReviewListResponse(
    val results: List<ReviewDto>,
    val page: Int,
    val id: Int,
    @SerialName("total_pages") val totalPages: Int,
    @SerialName("total_results") val totalResults: Int
)

@Serializable
data class ReviewDto(
    val id: String,
    val author: String,
    val content: String,
    @SerialName("created_at") val createdAt: String
)

@Serializable
data class VideoListResponse(
    val id: Int,
    val results: List<VideoDto>
)

@Serializable
data class VideoDto(
    val id: String,
    val key: String,
    val name: String,
    val site: String,
    val type: String,
    val official: Boolean
)
