package com.fikrisandi.parkeemovieapp.data.local.dao

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query
import com.fikrisandi.parkeemovieapp.data.local.entity.MovieEntity

@Dao
interface MovieDao {

    @Query("SELECT * FROM movie")
    suspend fun getAll(): List<MovieEntity>

    @Query("SELECT * FROM movie WHERE id = :movieId")
    suspend fun getById(movieId: Int): MovieEntity?


    @Insert
    suspend fun insert(movie: MovieEntity)

    @Query("DELETE FROM movie WHERE id = :movieId")
    suspend fun deleteById(movieId: Int)
}