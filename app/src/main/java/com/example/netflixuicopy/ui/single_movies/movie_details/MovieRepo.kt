package com.example.stajproje2024.ui.single_movies.movie_details

import androidx.lifecycle.LiveData
import com.example.stajproje2024.data.api.TheMovieDbInterface
import com.example.stajproje2024.data.repository.NetworkState
import com.example.stajproje2024.data.vo.MovieDetails

import com.oxcoding.movies.data.repository.MovieDetailsNetworkDataSource

import io.reactivex.disposables.CompositeDisposable

class MovieRepo  (private val apiService : TheMovieDbInterface) {

    lateinit var movieDetailsNetworkDataSource: MovieDetailsNetworkDataSource

    fun fetchSingleMovieDetails (compositeDisposable: CompositeDisposable, movieId: Int) : LiveData<MovieDetails> {

        movieDetailsNetworkDataSource = MovieDetailsNetworkDataSource(apiService,compositeDisposable)
        movieDetailsNetworkDataSource.fetchMovieDetails(movieId)

        return movieDetailsNetworkDataSource.downloadedMovieResponse

    }

    fun getMovieDetailsNetworkState(): LiveData<NetworkState> {
        return movieDetailsNetworkDataSource.networkState
    }
}