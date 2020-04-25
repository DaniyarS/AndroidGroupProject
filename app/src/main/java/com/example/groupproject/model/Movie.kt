package com.example.groupproject.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "movie_table")
data class Movie(
    @PrimaryKey
    @SerializedName("id") val id: Int?=null,
    @SerializedName("poster_path") val poster_path: String?=null,
    @SerializedName("backdrop_path") val backdrop_path: String?=null,
    @SerializedName("title") val title: String?=null,
    @SerializedName("overview") val overview: String?=null,
    @SerializedName("release_date") val release_date: String?=null,
    @SerializedName("runtime") val runtime: Int = 0

//    @SerializedName("genres")
//    @Ignore
//    val genres: List<MovieGenres>?=null,
//    @SerializedName("credits")
//    @Ignore
//    val credits : Credits?=null
//    @SerializedName("popularity") val populatiry: Double,
//    @SerializedName("vote_count") val vote_count: Int,
//    @SerializedName("video") val video: Boolean,
//    @SerializedName("adult") val adult: Boolean,
//    @SerializedName("original_language") val original_language: String,
//    @SerializedName("original_title") val original_title: String,
//    @SerializedName("genre_ids") val genre_ids: List<Int>,
//    @SerializedName("genres") val genres: List<MovieGenres>,
//    @SerializedName("vote_average") val vote_average: Double,
//    @SerializedName("results") val results: List<Movie>
)



// Backdrop pages format size:
//w300
//w780
//w1280
//original

//poster_path images format size:
//w92
//w154
//w185
//w342
//w500
//w780
//original
