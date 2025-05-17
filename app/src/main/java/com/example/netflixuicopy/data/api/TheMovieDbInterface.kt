package com.example.stajproje2024.data.api

import com.example.stajproje2024.data.vo.MovieDetails
import com.example.stajproje2024.data.vo.MovieResponse
import io.reactivex.Single

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TheMovieDbInterface {
    //https://api.themoviedb.org/3/movie/573435
    // https://api.themoviedb.org/3/movie/popular
    //https://api.themoviedb.org/3/movie/popular
    @GET("movie/popular")
    fun getPopularMovie(@Query("page") page:Int):Single<MovieResponse>
@GET("movie/{movie_id}")
fun getMovieDetails(@Path(value = "movie_id")id:Int): Single<MovieDetails>

@GET("search/movie")
fun searchMovies(
    @Query("query") query: String?, @Query("page") page: Int): Single<MovieResponse>

}

