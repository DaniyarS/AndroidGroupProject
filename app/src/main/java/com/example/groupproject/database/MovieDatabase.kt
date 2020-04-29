package com.example.groupproject.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.groupproject.model.Movie

@Database(entities = [Movie::class], version = 2, exportSchema = false)
abstract class MovieDatabase() : RoomDatabase() {
    abstract fun movieDao(): MovieDao

    companion object {
        var INSTANCE: MovieDatabase? = null
        val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE movies_table ADD COLUMN liked int DEFAULT 0")
            }
        }

        fun getDatabase(context: Context): MovieDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        MovieDatabase::class.java,
                        "movie_database.db"
                    ).addMigrations(MIGRATION_1_2)
                    .allowMainThreadQueries()
                    .build()

            }
            return INSTANCE!!
        }
    }
}