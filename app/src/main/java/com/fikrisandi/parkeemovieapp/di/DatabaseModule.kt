package com.fikrisandi.parkeemovieapp.di

import android.content.Context
import androidx.room3.Room
import com.fikrisandi.parkeemovieapp.data.local.database.ParkeeRoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ParkeeRoomDatabase {
        return Room.databaseBuilder(
            context,
            ParkeeRoomDatabase::class.java,
            "parkee_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideMovieDao(database: ParkeeRoomDatabase) = database.getMovieDao()

}