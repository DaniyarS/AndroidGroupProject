package com.example.groupproject.database

import android.widget.ImageView
import android.widget.TextView
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "movie_detail")
data class MovieDetail (
    @PrimaryKey
    @SerializedName("id") val movieId: Int,
    @SerializedName("movieImageBackdrop") val movieImageBackdrop: String,
    @SerializedName("movieTitle") val movieTitle: String,
    @SerializedName("movieRealease") val movieRealease: String,
    @SerializedName("movieDuration") val movieDuration: String,
    @SerializedName("movieDetails") val movieDetails: String,
    @SerializedName("movieDirector") val movieDirector: String,
    @SerializedName("movieCast") val movieCast: String,
    @SerializedName("movieGenre") val movieGenre: String,
    @SerializedName("btnFavorite") val btnFavorite: String
)