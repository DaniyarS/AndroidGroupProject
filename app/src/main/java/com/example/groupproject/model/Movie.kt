package com.example.groupproject.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "movie_table")
data class Movie(
    @PrimaryKey
    @SerializedName("id") val id: Int,
    @SerializedName("poster_path") val poster_path: String,
    @SerializedName("backdrop_path") val backdrop_path: String,
    @SerializedName("title") val title: String,
    @SerializedName("overview") val overview: String,
    @SerializedName("release_date") val release_date: String,
    @SerializedName("runtime") val runtime: Int,
    @SerializedName("genres") val genres: List<MovieGenres>,
    @SerializedName("credits") val credits : Credits
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
//{
////    var baseImageUrl: String = "https://image.tmdb.org/t/p/w300"
////    var backdropImageUrl: String= "https://image.tmdb.org/t/p/w780"
////    fun getPosterPathImage(): String {
////        return "https://image.tmdb.org/t/p/w342"+poster_path }
////
////    fun getBackDropPathImage(): String{
////        return "https://image.tmdb.org/t/p/original" + backdrop_path }
//
//}


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
