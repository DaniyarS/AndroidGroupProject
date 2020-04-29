package com.example.groupproject.api

import com.google.gson.annotations.SerializedName

data class Session(
    @SerializedName("session_id") val sessionId: String
)