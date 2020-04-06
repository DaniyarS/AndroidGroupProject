package com.example.groupproject.api

import com.google.gson.annotations.SerializedName

data class FavoriteResponse (
    @SerializedName("status_code") val status_code: Int,
    @SerializedName("status_message") val status_message: String
)