package com.example.stajproje2024.ui.popular_movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stajproje2024.data.local.FavoriteMovie
import com.example.stajproje2024.data.local.FavoriteMovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoriteMovieViewModel(private val repository: FavoriteMovieRepository) : ViewModel() {

    val allFavoriteMovies: LiveData<List<FavoriteMovie>> = repository.allFavoriteMovies

    fun insert(favoriteMovie: FavoriteMovie) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(favoriteMovie)
        }
    }

    fun delete(movieId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(movieId)
        }
    }

    suspend fun getMovieById(movieId: Int): FavoriteMovie? {
        return withContext(Dispatchers.IO) {
            repository.getMovieById(movieId)
        }
    }

    fun isFavorite(movieId: Int): LiveData<Boolean> {
        return repository.isFavorite(movieId)
    }


}
