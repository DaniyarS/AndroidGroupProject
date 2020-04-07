package com.example.groupproject.api

import com.google.gson.annotations.SerializedName

data class Validation (
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String,
    @SerializedName("request_token") val requestToken: String
)