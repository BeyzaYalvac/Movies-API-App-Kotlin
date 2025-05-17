package com.example.stajproje2024.ui.Favorite_movies

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.stajproje2024.data.local.FavoriteMovieDatabase
import com.example.stajproje2024.data.local.FavoriteMovieRepository
import com.example.stajproje2024.databinding.ActivityFavoriteMoviesBinding
import com.example.stajproje2024.ui.popular_movies.FavoriteMovieViewModel
import com.example.stajproje2024.ui.popular_movies.FavoriteMovieViewModelFactory

class FavoriteMovies : AppCompatActivity() {

    private lateinit var favoriteMoviesViewModel: FavoriteMovieViewModel
    private lateinit var binding: ActivityFavoriteMoviesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteMoviesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = FavoriteMovieDatabase.getDatabase(this)
        val favoriteMovieDao = database.favoriteMovieDao()
        val repository = FavoriteMovieRepository(favoriteMovieDao)
        val factory = FavoriteMovieViewModelFactory(repository)

        favoriteMoviesViewModel = ViewModelProvider(this, factory).get(FavoriteMovieViewModel::class.java)

        favoriteMoviesViewModel.allFavoriteMovies.observe(this, { favoriteMovies ->
            favoriteMovies?.let {
                val adapter = FavoriteMoviesAdapter(this, it)
                binding.favoritelistView.adapter = adapter
            }
        })
    }
}
