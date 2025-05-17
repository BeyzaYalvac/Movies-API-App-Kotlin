import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.stajproje2024.data.api.TheMovieDbInterface
import com.example.stajproje2024.data.repository.NetworkState
import com.example.stajproje2024.data.vo.MovieResult
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MovieDataSource(
    private val apiService: TheMovieDbInterface,
    private val compositeDisposable: CompositeDisposable,
    private val searchQuery: String?
) : PageKeyedDataSource<Int, MovieResult>() {

    private val PAGE_SIZE = 20

    val networkState: MutableLiveData<NetworkState> = MutableLiveData()

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, MovieResult>) {
        networkState.postValue(NetworkState.LOADING) //Veri yüklenirken ağ state'ini loading yapmak?
        val page = 1
        val disposable = if (searchQuery.isNullOrEmpty()) {
            apiService.getPopularMovie(page)
        } else {
            apiService.searchMovies(searchQuery, page)
        }
        disposable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                networkState.postValue(NetworkState.LOADED) //Veri yüklendiğinde ağ state'ini loaded yaptım.
                callback.onResult(it.movieList, null, page + 1)
            }, {
                networkState.postValue(NetworkState.ERROR) //veri yüklenemediğinde log'a hata bas ve ağ state'ini error yaptım.
                Log.e("MovieDataSource", "Data başlangıcında hata", it)
            }).let { compositeDisposable.add(it) }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, MovieResult>) {
        networkState.postValue(NetworkState.LOADING)
        val page = params.key
        val disposable = if (searchQuery.isNullOrEmpty()) {
            apiService.getPopularMovie(page)
        } else {
            apiService.searchMovies(searchQuery, page)
        }
        disposable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                networkState.postValue(NetworkState.LOADED)
                callback.onResult(it.movieList, page + 1)
            }, {
                networkState.postValue(NetworkState.ERROR)
                Log.e("MovieDataSource", "daha fazla data yüklerken hata oldu", it)
            }).let { compositeDisposable.add(it) } //Uygulama kapatıldığında hafızaya ekler?
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, MovieResult>) {
       //Boş
    }
}
