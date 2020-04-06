package com.example.groupproject.api

import com.google.gson.annotations.SerializedName

data class FavoriteRequest (
    @SerializedName("media_type") val media_type: String = "movie",
    @SerializedName("media_id") val media_id: Int,
    @SerializedName("favorite") val favorite: Boolean
)