package com.example.groupproject.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
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

    @Query("SELECT * FROM movies_table WHERE idiwka>=1 AND idiwka<=20")
    fun getPopular(): List<Movie>

    @Query("SELECT * FROM movies_table WHERE idiwka>=21 AND idiwka<=40")
    fun getTopRated(): List<Movie>

    @Query("SELECT * FROM movies_table WHERE idiwka>=41 AND idiwka<=60")
    fun getUpcoming(): List<Movie>

    @Query("SELECT*FROM movies_table where selected=10")
    fun getUnLikedOffline(): List<Movie>

    @Query("SELECT*FROM movies_table where selected=1 or selected=11")
    fun getAllLiked(): List<Movie>

    @Query("SELECT selected FROM movies_table where id=:id")
    fun getLiked(id: Int?): Int

    @Query("SELECT id FROM movies_table where selected=:selected")
    fun getLikedOffline(selected: Int?): List<Int>
}