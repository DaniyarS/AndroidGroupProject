package com.example.groupproject.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.groupproject.database.MovieDetail
import com.example.groupproject.model.Movie

@Dao
interface MovieDetailDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: Movie?)

    @Query("SELECT * FROM movie_detail")
    fun getAll(): List<MovieDetail>
}