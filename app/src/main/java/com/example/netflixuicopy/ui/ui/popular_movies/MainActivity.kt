package com.example.stajproje2024.ui.popular_movies

import MainActivityViewModel
import MoviePagedListRepository
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.ComponentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.stajproje2024.R
import com.example.stajproje2024.data.api.TheMovieDBClient
import com.example.stajproje2024.data.api.TheMovieDbInterface
import com.example.stajproje2024.data.repository.NetworkState
import com.example.stajproje2024.databinding.ActivityMainBinding
import com.example.stajproje2024.ui.Favorite_movies.FavoriteMovies
import android.text.Editable
import android.text.TextWatcher
import androidx.recyclerview.widget.RecyclerView

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var movieRepository: MoviePagedListRepository
    private lateinit var movieAdapter: PopularMoviePagedListAdapter
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val apiService: TheMovieDbInterface = TheMovieDBClient.getClient()
        movieRepository = MoviePagedListRepository(apiService)
        viewModel = ViewModelProvider(this, MainActivityViewModelFactory(movieRepository)).get(MainActivityViewModel::class.java)

        movieAdapter = PopularMoviePagedListAdapter(this)

        binding.rvMovieList.layoutManager = GridLayoutManager(this, 2)
        binding.rvMovieList.setHasFixedSize(true)
        binding.rvMovieList.adapter = movieAdapter

        viewModel.moviePagedList.observe(this, Observer {
            movieAdapter.submitList(it)
        })

        viewModel.networkState.observe(this, Observer {
            binding.progressBarPopular.visibility = if (viewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
            binding.txtErrorPopular.visibility = if (viewModel.listIsEmpty() && it == NetworkState.ERROR) View.VISIBLE else View.GONE

            if (!viewModel.listIsEmpty()) {
                movieAdapter.setNetworkState(it)
            }
        })

        binding.mainToolbarFavoriteButton.setOnClickListener {
            val intent = Intent(this, FavoriteMovies::class.java)
            startActivity(intent)
        }

        binding.editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s?.let { filterMovies(it.toString()) }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        val spinnerAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.gridview_or_listview_array,
            android.R.layout.simple_spinner_item
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerViewType.adapter = spinnerAdapter

        binding.spinnerViewType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> setRecyclerViewLayoutManager(GridLayoutManager(this@MainActivity, 2)) // Grid View
                    1 -> setRecyclerViewLayoutManager(LinearLayoutManager(this@MainActivity)) // List View
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }


        viewModel.setSearchQuery("") // Default to popular movies
    }

    private fun filterMovies(query: String) {
        viewModel.setSearchQuery(query)
    }

    private fun setRecyclerViewLayoutManager(layoutManager: RecyclerView.LayoutManager) {
        binding.rvMovieList.layoutManager = layoutManager
        binding.rvMovieList.adapter = movieAdapter
    }
}
