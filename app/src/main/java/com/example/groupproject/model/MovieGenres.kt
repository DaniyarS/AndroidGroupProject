package com.example.groupproject.model

import com.google.gson.annotations.SerializedName

data class MovieGenres (
    @SerializedName("id") var id: Int = 0,
    @SerializedName("name") var name: String = ""
)
//{

//    fun  getGenreName(): String{
//        return this.name
//    }
//}
