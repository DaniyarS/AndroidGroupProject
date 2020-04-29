package com.example.groupproject.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [MovieDetail::class], version=1)
abstract class MovieDetailDatabase : RoomDatabase() {

    abstract fun movieDetailDao() : MovieDetailDao

    companion object {
        var INSTANCE: MovieDetailDatabase? = null

        fun getDatabase(context: Context): MovieDetailDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    MovieDetailDatabase::class.java,
                    "movie_detail_database.db"
                ).build()
            }
            return INSTANCE!!
        }
    }
}