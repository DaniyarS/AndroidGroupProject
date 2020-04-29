package com.example.groupproject.database

import androidx.room.*
import com.example.groupproject.model.Movie

@Dao
interface MovieDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: List<Movie>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movie: Movie?)

    @Query("SELECT*FROM movies_table WHERE id=:id")
    fun getMovie(id: Int?): Movie

    @Query("SELECT*FROM movies_table")
    fun getAll(): List<Movie>

    @Query("SELECT * FROM movies_table WHERE idMovie>=1 AND idMovie<=20")
    fun getPopular(): List<Movie>

    @Query("SELECT * FROM movies_table WHERE idMovie>=21 AND idMovie<=40")
    fun getTopRated(): List<Movie>

    @Query("SELECT * FROM movies_table WHERE idMovie>=41 AND idMovie<=60")
    fun getUpcoming(): List<Movie>

    @Query("SELECT*FROM movies_table where selected=10")
    fun getUnLikedOffline(): List<Movie>

    @Query("SELECT*FROM movies_table where selected=1 or selected=11")
    fun getAllLiked(): List<Movie>

    @Query("SELECT selected FROM movies_table where id=:id")
    fun getLiked(id: Int?): Int

    @Query("SELECT id FROM movies_table where selected=11")
    fun getLikedOffline(): List<Int>

    @Query("UPDATE movies_table SET runtime = :runtime WHERE id = :id")
    fun updateMovieRuntime(runtime: Int, id: Int)
}