import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.switchMap
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.stajproje2024.data.api.POST_PER_PAGE
import com.example.stajproje2024.data.api.TheMovieDbInterface

import com.example.stajproje2024.data.repository.NetworkState
import com.example.stajproje2024.data.vo.MovieResult
import io.reactivex.disposables.CompositeDisposable


class MoviePagedListRepository(private val apiService: TheMovieDbInterface) {

    private var moviePagedList: LiveData<PagedList<MovieResult>>? = null
    private lateinit var moviesDataSourceFactory: MovieDataSourceFactory
    private val compositeDisposable = CompositeDisposable()

    fun fetchLiveMoviePagedList(searchQuery: String): LiveData<PagedList<MovieResult>> {
        moviesDataSourceFactory = MovieDataSourceFactory(apiService, compositeDisposable, searchQuery)

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(POST_PER_PAGE)
            .build()

        moviePagedList = LivePagedListBuilder(moviesDataSourceFactory, config).build()
        return moviePagedList!!
    }

    val networkState: LiveData<NetworkState>
        get() = moviesDataSourceFactory.moviesLiveDataSource.switchMap { it.networkState }

    fun dispose() {
        compositeDisposable.dispose()
    }
}
