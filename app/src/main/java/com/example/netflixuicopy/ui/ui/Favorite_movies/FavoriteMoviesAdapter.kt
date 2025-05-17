package com.example.stajproje2024.ui.Favorite_movies

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.bumptech.glide.Glide
import com.example.stajproje2024.data.local.FavoriteMovie
import com.example.stajproje2024.data.api.POSTER_BASE_URL
import com.example.stajproje2024.databinding.FavoritemovieListItemBinding

class FavoriteMoviesAdapter(private val context: Context, private val favoriteMovies: List<FavoriteMovie>) : BaseAdapter() {

    override fun getCount(): Int {
        return favoriteMovies.size
    }

    override fun getItem(position: Int): Any {
        return favoriteMovies[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding: FavoritemovieListItemBinding
        if (convertView == null) {
            binding = FavoritemovieListItemBinding.inflate(LayoutInflater.from(context), parent, false)
        } else {
            binding = FavoritemovieListItemBinding.bind(convertView)
        }

        val movie = getItem(position) as FavoriteMovie

        binding.tvMovieTitle.text = movie.title
        binding.tvMovieReleaseDate.text = movie.releaseDate

        val moviePosterURL = POSTER_BASE_URL + movie.posterPath
        Glide.with(context)
            .load(moviePosterURL)
            .into(binding.ivMoviePoster)

        return binding.root
    }
}
