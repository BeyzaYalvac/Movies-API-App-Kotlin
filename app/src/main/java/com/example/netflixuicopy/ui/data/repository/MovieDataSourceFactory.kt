import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.stajproje2024.data.api.TheMovieDbInterface
import com.example.stajproje2024.data.vo.MovieResult
import io.reactivex.disposables.CompositeDisposable

class MovieDataSourceFactory(
    private val apiService: TheMovieDbInterface,
    private val compositeDisposable: CompositeDisposable,
    private val searchQuery: String?
) : DataSource.Factory<Int, MovieResult>() {

    val moviesLiveDataSource = MutableLiveData<MovieDataSource>()

    override fun create(): DataSource<Int, MovieResult> {
        val movieDataSource = MovieDataSource(apiService, compositeDisposable, searchQuery)
        moviesLiveDataSource.postValue(movieDataSource)
        return movieDataSource
    }
}






