package com.example.stajproje2024.data.api

import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

const val API_KEY = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJkZGRhYTY2NDQyNDJjZWNjYTFjZTM5YWFmY2IwZmFhMSIsIm5iZiI6MTcyMjIzOTk5Mi4zMDgzODEsInN1YiI6IjY2YTc0NTA1MzUxNzE0MjY1ZmFiYWE2ZiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.zohpNq7Q3RDJfkbwVfyyL0sMl3r_2ryLu0shWp6U7Ps"
const val BASE_URL = "https://api.themoviedb.org/3/"

const val POSTER_BASE_URL = "https://image.tmdb.org/t/p/w342"
//poster linki
//https://image.tmdb.org/t/p/w342/8cdWjvZQUExUUTzyp4t6EDMubfO.jpg

const val FIRST_PAGE=1
const val POST_PER_PAGE = 15
const val DATA_VIEW_TYPE = 1

object TheMovieDBClient {
    fun getClient(): TheMovieDbInterface {
        val requestInterceptor = Interceptor { chain ->

            val request: Request = chain.request()
                .newBuilder()
                .addHeader("Authorization", "Bearer $API_KEY")
                .build()
            chain.proceed(request)
        }
val logingnInterseptor=HttpLoggingInterceptor()
        logingnInterseptor.level=HttpLoggingInterceptor.Level.BODY

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(requestInterceptor)
            .addInterceptor(logingnInterseptor)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TheMovieDbInterface::class.java)
    }
}
