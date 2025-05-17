package com.example.stajproje2024.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface FavoriteMovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movie: FavoriteMovie)

    @Query("SELECT COUNT(*) > 0 FROM favorite_movies WHERE id = :movieId")
    fun isFavorite(movieId: Int): LiveData<Boolean>
    @Query("DELETE FROM favorite_movies WHERE id = :movieId")
    fun delete(movieId: Int)

    @Query("SELECT * FROM favorite_movies")
    fun getAllFavoriteMovies(): LiveData<List<FavoriteMovie>>

    @Query("SELECT * FROM favorite_movies WHERE id = :movieId")
    suspend fun getMovieById(movieId: Int): FavoriteMovie?
}