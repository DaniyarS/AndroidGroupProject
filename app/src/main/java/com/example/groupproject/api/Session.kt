package com.example.groupproject.api

import com.google.gson.annotations.SerializedName

data class Session(
//    @SerializedName( "success") val success: Boolean,
    @SerializedName("session_id") val session_id: String
)