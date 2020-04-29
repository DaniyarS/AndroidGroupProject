package com.example.groupproject.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "movies_table")
data class Movie(
    @PrimaryKey
    val idMovie: Int?=null,
    @SerializedName("id") val id: Int? = null,
    @SerializedName("popularity") val popularity: Double? = null,
    @SerializedName("vote_count") val vote_count: Int? = null,
    @SerializedName("video") val video: Boolean? = null,
    @SerializedName("poster_path") val poster_path: String? = null,
    @SerializedName("adult") val adult: Boolean? = null,
    @SerializedName("backdrop_path") val backdrop_path: String? = null,
    @SerializedName("original_language") val original_language: String? = null,
    @SerializedName("original_title") val original_title: String? = null,
    @SerializedName("selected") var selected: Int? = 0,
    @SerializedName("title") val title: String? = null,
    @SerializedName("vote_average") val vote_average: Double? = null,
    @SerializedName("overview") val overview: String? = null,
    @SerializedName("release_date") val release_date: String? = null,
    @SerializedName("runtime") val runtime: Int? = null

) : Serializable
{
    fun getPosterPathImage(): String {
        return "https://image.tmdb.org/t/p/w342"+poster_path }

    fun getBackDropPathImage(): String{
        return "https://image.tmdb.org/t/p/original" + backdrop_path }
}