package com.example.groupproject.model

import com.google.gson.annotations.SerializedName

data class Movie(

    @SerializedName("popularity") val populatiry: Double,
    @SerializedName("vote_count") val vote_count: Int,
    @SerializedName("video") val video: Boolean,
    @SerializedName("poster_path") val poster_path: String,
    @SerializedName("id") val id: Int,
    @SerializedName("adult") val adult: Boolean,
    @SerializedName("backdrop_path") val backdrop_path: String,
    @SerializedName("original_language") val original_language: String,
    @SerializedName("original_title") val original_title: String,

    @SerializedName("genre_ids") val genre_ids: List<Int>,
    @SerializedName("genres") val genres: List<MovieGenres>,

    @SerializedName("title") val title: String,
    @SerializedName("vote_average") val vote_average: Double,
    @SerializedName("overview") val overview: String,
    @SerializedName("release_date") val release_date: String,
    @SerializedName("runtime") val runtime: Int,

    @SerializedName("credits") val credits : Credits
)
{
    val baseImageUrl: String = "https://image.tmdb.org/t/p/w300"
    val backdropImageUrl: String= "https://image.tmdb.org/t/p/w780"
    fun getPosterPathImage(): String {
        return "https://image.tmdb.org/t/p/w342"+poster_path }

    fun getBackDropPathImage(): String{
        return "https://image.tmdb.org/t/p/w780" + backdrop_path }

}


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
