import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.example.stajproje2024.data.repository.NetworkState
import com.example.stajproje2024.data.vo.MovieResult

class MainActivityViewModel(private val movieRepository: MoviePagedListRepository) : ViewModel() {
    private val _moviePagedList = MutableLiveData<PagedList<MovieResult>>()
    val moviePagedList: LiveData<PagedList<MovieResult>> get() = _moviePagedList

    private val _networkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState> get() = _networkState

    private var searchQuery: String = ""

    init {
        loadMovies(searchQuery)
    }

    fun setSearchQuery(query: String) {
        searchQuery = query
        loadMovies(query)
    }

    private fun loadMovies(query: String) {
        val moviePagedList = movieRepository.fetchLiveMoviePagedList(query)
        moviePagedList
            .observeForever {
                _moviePagedList.postValue(it)
            }
        movieRepository.networkState.observeForever {
            _networkState.postValue(it)
        }
    }

    fun listIsEmpty(): Boolean = _moviePagedList.value?.isEmpty() ?: true
}
