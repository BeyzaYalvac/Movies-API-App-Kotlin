package com.example.stajproje2024.data.vo


import com.google.gson.annotations.SerializedName

data class MovieResult(
    val id: Int,
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("release_date")
    val releaseDate: String,
    val title: String,
)