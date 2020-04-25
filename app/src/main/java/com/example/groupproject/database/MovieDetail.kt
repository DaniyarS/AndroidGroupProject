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
    @SerializedName("movieImageBackdrop") val movieImageBackdrop: ImageView,
    @SerializedName("movieTitle") val movieTitle: TextView,
    @SerializedName("movieRealease") val movieRealease: TextView,
    @SerializedName("movieDuration") val movieDuration: TextView,
    @SerializedName("movieDetails") val movieDetails: TextView,
    @SerializedName("movieDirector") val movieDirector: TextView,
    @SerializedName("movieCast") val movieCast: TextView,
    @SerializedName("movieGenre") val movieGenre: TextView,
    @SerializedName("btnFavorite") val btnFavorite: ImageView
)