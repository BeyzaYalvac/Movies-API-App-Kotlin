package com.example.stajproje2024.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_movies")
data class FavoriteMovie(
    @PrimaryKey val  id:Int,
    val title: String,
    val posterPath: String,
    val releaseDate: String,
    val rating: Double
)