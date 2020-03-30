package com.example.groupproject.model

import com.google.gson.annotations.SerializedName

data class MovieGenres (
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String
) {

    fun  getGenreName(): String{
        return this.name
    }
}
