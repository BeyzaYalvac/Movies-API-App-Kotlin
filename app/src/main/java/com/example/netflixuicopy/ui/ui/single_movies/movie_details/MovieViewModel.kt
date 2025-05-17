package com.oxcoding.movies.ui.SingleMovie

import androidx.lifecycle.ViewModel
import com.example.stajproje2024.ui.single_movies.movie_details.MovieRepo
import io.reactivex.disposables.CompositeDisposable

class SingleMovieViewModel(private val movieRepository : MovieRepo, movieId: Int) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val  movieDetails by lazy {
        movieRepository.fetchSingleMovieDetails(compositeDisposable,movieId)
    }

    val networkState by lazy {
        movieRepository.getMovieDetailsNetworkState()
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose() //gereksiz işlemleri durdurdu ve Disposable nesnelerini temizler
    }

}