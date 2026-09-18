package com.fikrisandi.parkeemovieapp.data.local.database

import androidx.room3.Database
import androidx.room3.RoomDatabase
import com.fikrisandi.parkeemovieapp.data.local.dao.MovieDao
import com.fikrisandi.parkeemovieapp.data.local.entity.MovieEntity


@Database(entities = [MovieEntity::class], version = 1)
abstract class ParkeeRoomDatabase : RoomDatabase() {
    abstract fun getMovieDao(): MovieDao
}