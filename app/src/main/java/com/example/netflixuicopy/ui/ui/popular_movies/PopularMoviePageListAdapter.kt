package com.example.stajproje2024.ui.popular_movies

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.stajproje2024.R
import com.example.stajproje2024.data.repository.NetworkState
import com.example.stajproje2024.data.repository.Status
import com.example.stajproje2024.data.vo.MovieResult
import com.example.stajproje2024.ui.single_movies.movie_details.Movie

class PopularMoviePagedListAdapter(private val context: Context) :
    PagedListAdapter<MovieResult, RecyclerView.ViewHolder>(MovieDiffCallback()) {

    companion object {
        const val DATA_VIEW_TYPE = 1
        const val NETWORK_VIEW_TYPE = 2
        const val POSTER_BASE_URL = "https://image.tmdb.org/t/p/w342"
    }

    val DATA_VIEW_TYPE = 1
    private var networkState: NetworkState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return if (viewType == DATA_VIEW_TYPE) {
            val view = layoutInflater.inflate(R.layout.movielistitem, parent, false)
            MovieItemViewHolder(view)
        } else {
            val view = layoutInflater.inflate(R.layout.network_state_item, parent, false)
            NetworkStateItemViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == DATA_VIEW_TYPE) {
            (holder as MovieItemViewHolder).bind(getItem(position), context)
        } else {
            (holder as NetworkStateItemViewHolder).bind(networkState)
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    private fun hasExtraRow(): Boolean {
        return networkState != null && networkState != NetworkState.LOADED
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            NETWORK_VIEW_TYPE
        } else {
            DATA_VIEW_TYPE
        }
    }

    class MovieItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val movieTitle: TextView = view.findViewById(R.id.cv_movie_title)
        private val movieReleaseDate: TextView = view.findViewById(R.id.cv_movie_release_date)
        private val moviePoster: ImageView = view.findViewById(R.id.cv_iv_movie_poster)

        fun bind(movie: MovieResult?, context: Context) {
            movie?.let {item->
                movieTitle.text = item.title
                movieReleaseDate.text = item.releaseDate

                val moviePosterURL = PopularMoviePagedListAdapter.POSTER_BASE_URL + item.posterPath
                Glide.with(itemView.context)
                    .load(moviePosterURL)
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(moviePoster)

                itemView.setOnClickListener {
                    val intent = Intent(context, Movie::class.java)
                    intent.putExtra("id", item.id)
                    context.startActivity(intent)
                }
            }
        }
    }

    class NetworkStateItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val progressBar: ProgressBar = view.findViewById(R.id.progress_bar)
        private val errorMsg: TextView = view.findViewById(R.id.error_msg)

        fun bind(networkState: NetworkState?) {
            networkState?.let {
                progressBar.visibility = if (it.status == Status.RUNNING) View.VISIBLE else View.GONE
                errorMsg.visibility = if (it.status == Status.FAILED) View.VISIBLE else View.GONE
                errorMsg.text = it.msg
            }
        }
    }



    class MovieDiffCallback : DiffUtil.ItemCallback<MovieResult>() {
        override fun areItemsTheSame(oldItem: MovieResult, newItem: MovieResult): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MovieResult, newItem: MovieResult): Boolean {
            return oldItem == newItem
        }
    }

    fun setNetworkState(newNetworkState: NetworkState) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }
}