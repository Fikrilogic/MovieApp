package com.fikrisandi.parkeemovieapp.data.local.entity

import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.PrimaryKey


@Entity(
    tableName = "movie"
)
data class MovieEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo val title: String,
    @ColumnInfo val posterPath: String,
    @ColumnInfo val overview: String,
    @ColumnInfo val releaseDate: String,
    @ColumnInfo val rating: Double,
)
