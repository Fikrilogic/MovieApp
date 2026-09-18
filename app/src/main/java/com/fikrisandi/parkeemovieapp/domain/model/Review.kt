package com.fikrisandi.parkeemovieapp.domain.model

data class Review(
    val id: String,
    val author: String,
    val content: String,
    val createdAt: String
)

data class ReviewPagination(
    val id: Int,
    val page: Int,
    val results: List<Review>,
    val totalPages: Int,
    val totalResults: Int,
)
