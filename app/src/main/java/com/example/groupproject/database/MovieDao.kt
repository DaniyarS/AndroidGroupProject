package com.example.groupproject.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.groupproject.model.Movie

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: List<Movie>)

    @Query("SELECT * FROM movie_table LIMIT 20")
    fun getPopular(): List<Movie>

    @Query("SELECT * FROM movie_table LIMIT 20 OFFSET 20")
    fun getTopRated(): List<Movie>

    @Query("SELECT * FROM movie_table LIMIT 20 OFFSET 40")
    fun getUpcoming(): List<Movie>

    @Query("SELECT * FROM movie_table WHERE id=:movieId")
    fun getBriefMovie(movieId: Int): Movie

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insertMovie(movie: Movie)
}