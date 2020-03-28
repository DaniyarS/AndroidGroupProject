package com.example.groupproject

import com.google.gson.annotations.SerializedName

data class Post(
    @SerializedName("id") val postId: Int,
    @SerializedName("userId") val userId: Int,
    @SerializedName("title") val title: String,
    @SerializedName("body") val body: String
)