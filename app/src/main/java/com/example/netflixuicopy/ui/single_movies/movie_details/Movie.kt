package com.example.stajproje2024.ui.single_movies.movie_details

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.stajproje2024.R
import com.example.stajproje2024.data.api.POSTER_BASE_URL
import com.example.stajproje2024.data.api.TheMovieDBClient
import com.example.stajproje2024.data.api.TheMovieDbInterface
import com.example.stajproje2024.data.local.FavoriteMovie
import com.example.stajproje2024.data.local.FavoriteMovieDatabase
import com.example.stajproje2024.data.local.FavoriteMovieRepository
import com.example.stajproje2024.data.repository.NetworkState
import com.example.stajproje2024.data.repository.Status
import com.example.stajproje2024.data.vo.MovieDetails
import com.example.stajproje2024.databinding.ActivityMovieBinding
import com.example.stajproje2024.ui.Favorite_movies.FavoriteMovies
import com.example.stajproje2024.ui.popular_movies.FavoriteMovieViewModel
import com.example.stajproje2024.ui.popular_movies.FavoriteMovieViewModelFactory
import com.oxcoding.movies.ui.SingleMovie.SingleMovieViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.NumberFormat
import java.util.*

class Movie : AppCompatActivity() {

    private lateinit var binding: ActivityMovieBinding
    private lateinit var viewModel: SingleMovieViewModel
    private lateinit var movieRepository: MovieRepo
    private lateinit var favoriteMovieViewModel: FavoriteMovieViewModel
    private var currentMovieDetails: MovieDetails? = null
    private var isFavorited = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val apiService: TheMovieDbInterface = TheMovieDBClient.getClient()
        movieRepository = MovieRepo(apiService)
        favoriteMovieViewModel = ViewModelProvider(
            this,
            FavoriteMovieViewModelFactory(
                FavoriteMovieRepository(
                    FavoriteMovieDatabase.getDatabase(this).favoriteMovieDao()
                )
            )
        ).get(FavoriteMovieViewModel::class.java)

        val movieId: Int = intent.getIntExtra("id", -1)
        viewModel = getViewModel(movieId)

        viewModel.movieDetails.observe(this, Observer { movieDetails ->
            currentMovieDetails = movieDetails
            bindUI(movieDetails)
            checkFavoriteStatus(movieDetails.id)
        })

        viewModel.networkState.observe(this, Observer { networkState ->
            binding.progressBarPopular.visibility =
                if (networkState == NetworkState.LOADING) View.VISIBLE else View.GONE
            binding.txtError.visibility =
                if (networkState.status == Status.FAILED) View.VISIBLE else View.GONE
        })

        binding.btnFavoriteTagline.setOnClickListener {
            currentMovieDetails?.let { movieDetails ->
                CoroutineScope(Dispatchers.IO).launch {
                    val existingMovie = favoriteMovieViewModel.getMovieById(movieDetails.id)
                    if (existingMovie == null) {
                        val favoriteMovie = FavoriteMovie(
                            id = movieDetails.id,
                            title = movieDetails.title,
                            posterPath = movieDetails.posterPath,
                            releaseDate = movieDetails.releaseDate,
                            rating = movieDetails.rating
                        )
                        favoriteMovieViewModel.insert(favoriteMovie)
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@Movie, "Favorilere eklendi", Toast.LENGTH_SHORT).show()
                            isFavorited = true
                            toggleFavoriteStatus(binding.btnFavoriteTagline)
                        }
                    } else {
                        favoriteMovieViewModel.delete(movieDetails.id)
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@Movie, "Favorilerden çıkarıldı", Toast.LENGTH_SHORT).show()
                            isFavorited = false
                            toggleFavoriteStatus(binding.btnFavoriteTagline)
                        }
                    }

                    val intent = Intent(this@Movie, FavoriteMovies::class.java)
                    startActivity(intent)
                }
            } ?: run {
                Toast.makeText(this, "Film bilgileri alınamadı", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun toggleFavoriteStatus(favoriteButton: ImageButton) {
        favoriteButton.setImageResource(
            if (isFavorited) R.drawable.favorited_icon else R.drawable.favorite_icon
        )
    }

    private fun bindUI(movieDetails: MovieDetails) {
        val formatCurrency = NumberFormat.getCurrencyInstance(Locale.US)

        binding.movieTitle.text = movieDetails.title
        binding.movieTagline.text = movieDetails.tagline
        binding.movieReleaseDate.text = movieDetails.releaseDate
        binding.movieRating.text = movieDetails.rating.toString()
        binding.movieRuntime.text = "${movieDetails.runtime} minutes"
        binding.movieBudget.text = formatCurrency.format(movieDetails.budget)
        binding.movieRevenue.text = formatCurrency.format(movieDetails.revenue)
        binding.movieOverview.text = movieDetails.overview

        val moviePosterURL = POSTER_BASE_URL + movieDetails.posterPath
        Glide.with(this)
            .load(moviePosterURL)
            .into(binding.ivMoviePoster)
    }

    private fun checkFavoriteStatus(movieId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val existingMovie = favoriteMovieViewModel.getMovieById(movieId)
            withContext(Dispatchers.Main) {
                isFavorited = existingMovie != null
                toggleFavoriteStatus(binding.btnFavoriteTagline)
            }
        }
    }

    private fun getViewModel(movieId: Int): SingleMovieViewModel {
        return ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return SingleMovieViewModel(movieRepository, movieId) as T
            }
        })[SingleMovieViewModel::class.java]
    }
}

