package com.example.stajproje2024.data.local

import androidx.lifecycle.LiveData

class FavoriteMovieRepository(private val favoriteMovieDao: FavoriteMovieDao) {

    val allFavoriteMovies: LiveData<List<FavoriteMovie>> = favoriteMovieDao.getAllFavoriteMovies()

    suspend fun insert(movie: FavoriteMovie) {
        favoriteMovieDao.insert(movie)
    }
    fun isFavorite(movieId: Int): LiveData<Boolean> {
        return favoriteMovieDao.isFavorite(movieId)
    }

    fun delete(movieId: Int) {
        favoriteMovieDao.delete(movieId)
    }

    suspend fun getMovieById(movieId: Int): FavoriteMovie? {
        return favoriteMovieDao.getMovieById(movieId)
    }
}