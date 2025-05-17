package com.oxcoding.movies.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.stajproje2024.data.api.TheMovieDbInterface
import com.example.stajproje2024.data.repository.NetworkState
import com.example.stajproje2024.data.repository.Status
import com.example.stajproje2024.data.vo.MovieDetails
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MovieDetailsNetworkDataSource(
    private val apiService: TheMovieDbInterface,
    private val compositeDisposable: CompositeDisposable
) {

    private val _networkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState>
        get() = _networkState

    private val _downloadedMovieDetailsResponse = MutableLiveData<MovieDetails>()
    val downloadedMovieResponse: LiveData<MovieDetails>
        get() = _downloadedMovieDetailsResponse

    fun fetchMovieDetails(movieId: Int) {

        _networkState.postValue(NetworkState.LOADING)

        try {
            compositeDisposable.add(
                apiService.getMovieDetails(movieId)
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                        { movieDetails ->
                            _downloadedMovieDetailsResponse.postValue(movieDetails)
                            _networkState.postValue(NetworkState.LOADED)
                        },
                        { error ->
                            _networkState.postValue(NetworkState(Status.FAILED, "Bir şeyler yanlış gitti"))
                            Log.e("MovieDetailsDataSource", error.message ?: "Bilinemedik bir hata")
                        }
                    )
            )
        } catch (e: Exception) {
            e.message?.let { Log.e("MovieDetailsDataSource", it) }
        }
    }
}
