package com.example.groupproject.model

import com.example.groupproject.model.Movie
import com.google.gson.annotations.SerializedName

data class GetMoviesResponse (
    @SerializedName("page") val page: Int,
    @SerializedName("total_results") val total_results: Int,
    @SerializedName("total_pages") val total_pages: Int,
    @SerializedName("results") val results: List<Movie>
)