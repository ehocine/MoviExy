package com.helic.moviexy.data.viewmodels

import android.app.Application
import android.util.Log
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.SnackbarDuration
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.helic.moviexy.R
import com.helic.moviexy.data.database.FavoriteMovieEntity
import com.helic.moviexy.data.network.models.movie.Movie
import com.helic.moviexy.data.repository.Repository
import com.helic.moviexy.utils.Constants.DEFAULT_GENRE
import com.helic.moviexy.utils.Constants.DEFAULT_MIN_RATING
import com.helic.moviexy.utils.Constants.DEFAULT_MOVIES_NUMBER
import com.helic.moviexy.utils.Constants.DEFAULT_ORDER_BY
import com.helic.moviexy.utils.Constants.DEFAULT_PAGE_NUMBER
import com.helic.moviexy.utils.Constants.DEFAULT_SORT_BY
import com.helic.moviexy.utils.Constants.GENRE
import com.helic.moviexy.utils.Constants.LIMIT_NUMBER
import com.helic.moviexy.utils.Constants.MIN_RATING
import com.helic.moviexy.utils.Constants.MOVIE_ID
import com.helic.moviexy.utils.Constants.PAGE_NUMBER
import com.helic.moviexy.utils.Constants.QUERY_TERM
import com.helic.moviexy.utils.Constants.SORT_BY
import com.helic.moviexy.utils.Constants.TIMEOUT_IN_MILLIS
import com.helic.moviexy.utils.GetMovieCategory
import com.helic.moviexy.utils.LoadingState
import com.helic.moviexy.utils.SearchAppBarState
import com.helic.moviexy.utils.hasInternetConnection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {

    @OptIn(ExperimentalMaterialApi::class)
    val modalBottomSheetState: ModalBottomSheetState =
        ModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

    var firstTimeInScreen: MutableState<Boolean> = mutableStateOf(false)

    /** ROOM DATABASE */

    val selectedFavoriteMovieEntity: MutableState<FavoriteMovieEntity> = mutableStateOf(
        FavoriteMovieEntity()
    )

    private var _favoriteMoviesList = MutableStateFlow<List<FavoriteMovieEntity>>(emptyList())
    val favoriteMoviesList: StateFlow<List<FavoriteMovieEntity>> = _favoriteMoviesList

    fun readFavoriteMovies() {
        viewModelScope.launch {
            repository.local.readFavoriteMovies().collect {
                _favoriteMoviesList.emit(it)
            }
        }
    }

    fun insertFavoriteMovie(favoriteMovieEntity: FavoriteMovieEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertFavoriteMovie(favoriteMovieEntity)
        }

    fun deleteFavoriteMovie(favoriteMovieEntity: FavoriteMovieEntity) =
        viewModelScope.launch(Dispatchers.IO) {

            repository.local.deleteFavoriteMovie(favoriteMovieEntity)
        }

    fun deleteAllFavoriteMovies() {
        viewModelScope.launch {
            repository.local.deleteAllFavoriteMovies()
        }
    }


    /**   Retrofit: Network Requests   */

    val searchAppBarState: MutableState<SearchAppBarState> =
        mutableStateOf(SearchAppBarState.CLOSED)
    val searchTextState: MutableState<String> = mutableStateOf("")

    val popularMoviesList: MutableState<List<Movie>> = mutableStateOf(listOf())
    val latestMoviesList: MutableState<List<Movie>> = mutableStateOf(listOf())
    val moviesSuggestionsList: MutableState<List<Movie>> = mutableStateOf(listOf())

    val searchMoviesList: MutableState<List<Movie>> = mutableStateOf(listOf())

    val selectedMovieID: MutableState<Int> = mutableStateOf(0)
    val selectedMovieDetails: MutableState<Movie> = mutableStateOf(Movie())

    var selectedSortByValue: MutableState<String> = mutableStateOf(DEFAULT_SORT_BY)
    var selectedGenre: MutableState<String> = mutableStateOf(DEFAULT_GENRE)
    var selectedMinimumRating: MutableState<String> = mutableStateOf(DEFAULT_MIN_RATING)
    var selectedOrderBy: MutableState<String> = mutableStateOf(DEFAULT_ORDER_BY)

    var pageNumber: MutableState<Int> = mutableStateOf(DEFAULT_PAGE_NUMBER)
    var moviesNumberLimit: MutableState<Int> = mutableStateOf(DEFAULT_MOVIES_NUMBER)

    fun applyQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()
        queries[LIMIT_NUMBER] = moviesNumberLimit.value.toString()
        queries[PAGE_NUMBER] = pageNumber.value.toString()
        queries[GENRE] = selectedGenre.value.lowercase(Locale.getDefault())
        queries[MIN_RATING] = selectedMinimumRating.value.lowercase(Locale.getDefault())

        return queries
    }

    fun applyPopularMoviesQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()

        queries[LIMIT_NUMBER] = moviesNumberLimit.value.toString()
        queries[PAGE_NUMBER] = pageNumber.value.toString()
        queries[SORT_BY] = "download_count"
        queries[GENRE] = selectedGenre.value.lowercase(Locale.getDefault())
        queries[MIN_RATING] = selectedMinimumRating.value.lowercase(Locale.getDefault())

        return queries
    }

    var getMoviesListLoadingState = MutableStateFlow(LoadingState.IDLE)

    fun getMoviesList(
        queries: Map<String, String>,
        snackbar: (String, SnackbarDuration) -> Unit,
        movieCategory: GetMovieCategory
    ) {

        viewModelScope.launch {
            if (hasInternetConnection(getApplication<Application>())) {
                try {
                    withTimeoutOrNull(TIMEOUT_IN_MILLIS) {
                        getMoviesListLoadingState.emit(LoadingState.LOADING)
                        val response = repository.remote.getMoviesList(queries = queries)
                        if (response.isSuccessful) {
                            when (movieCategory) {
                                GetMovieCategory.POPULAR -> {
                                    popularMoviesList.value = response.body()!!.data.movies
                                }
                                else -> {
                                    latestMoviesList.value = response.body()!!.data.movies
                                }
                            }

                            getMoviesListLoadingState.emit(LoadingState.LOADED)
                        } else {
                            getMoviesListLoadingState.emit(LoadingState.ERROR)
                            Log.d("Error", "1: ${response.errorBody()}")
                            snackbar(
                                getApplication<Application>().getString(R.string.error_occurred),
                                SnackbarDuration.Short
                            )
                        }
                    } ?: withContext(Dispatchers.Main) {
                        getMoviesListLoadingState.emit(LoadingState.ERROR)
                        snackbar(
                            getApplication<Application>().getString(R.string.connection_time_out),
                            SnackbarDuration.Short
                        )
                    }
                } catch (e: Exception) {
                    Log.d("Error", "2: ${e.message}")
                    getMoviesListLoadingState.emit(LoadingState.ERROR)
                    snackbar(
                        getApplication<Application>().getString(R.string.error_occurred),
                        SnackbarDuration.Short
                    )
                }
            } else {
                getMoviesListLoadingState.emit(LoadingState.ERROR)
                snackbar(
                    getApplication<Application>().getString(R.string.device_not_connected_internet),
                    SnackbarDuration.Short
                )
            }
        }
    }


    var getMovieDetailLoadingState = MutableStateFlow(LoadingState.IDLE)

    fun getMovieDetails(
        movieID: Int,
        snackbar: (String, SnackbarDuration) -> Unit
    ) {
        val movieIDQuery: HashMap<String, String> = HashMap()
        movieIDQuery[MOVIE_ID] = movieID.toString()
        viewModelScope.launch {
            if (hasInternetConnection(getApplication<Application>())) {
                try {
                    withTimeoutOrNull(TIMEOUT_IN_MILLIS) {
                        getMovieDetailLoadingState.emit(LoadingState.LOADING)
                        val response = repository.remote.getMovieDetails(movieID = movieIDQuery)
                        if (response.isSuccessful) {
                            selectedMovieDetails.value = response.body()!!.data.movie
                            getMovieDetailLoadingState.emit(LoadingState.LOADED)
                        } else {
                            Log.d("Error", "1: ${response.errorBody()}")
                            getMovieDetailLoadingState.emit(LoadingState.ERROR)
                            snackbar(
                                getApplication<Application>().getString(R.string.error_occurred),
                                SnackbarDuration.Short
                            )
                        }
                    } ?: withContext(Dispatchers.Main) {
                        getMovieDetailLoadingState.emit(LoadingState.ERROR)
                        snackbar(
                            getApplication<Application>().getString(R.string.connection_time_out),
                            SnackbarDuration.Short
                        )
                    }
                } catch (e: Exception) {
                    Log.d("Error", "2: ${e.message}")
                    getMovieDetailLoadingState.emit(LoadingState.ERROR)
                    snackbar(
                        getApplication<Application>().getString(R.string.error_occurred),
                        SnackbarDuration.Short
                    )
                }
            } else {
                getMovieDetailLoadingState.emit(LoadingState.ERROR)
                snackbar(
                    getApplication<Application>().getString(R.string.device_not_connected_internet),
                    SnackbarDuration.Short
                )
            }
        }
    }

    fun getMoviesSuggestions(
        movieID: Int,
        snackbar: (String, SnackbarDuration) -> Unit
    ) {
        val movieIDQuery: HashMap<String, String> = HashMap()
        movieIDQuery[MOVIE_ID] = movieID.toString()
        viewModelScope.launch {
            if (hasInternetConnection(getApplication<Application>())) {
                try {
                    withTimeoutOrNull(TIMEOUT_IN_MILLIS) {
//                        getMovieDetailLoadingState.emit(LoadingState.LOADING)
                        val response =
                            repository.remote.getMoviesSuggestions(movieID = movieIDQuery)
                        if (response.isSuccessful) {
                            moviesSuggestionsList.value = response.body()!!.data.movies
//                            getMovieDetailLoadingState.emit(LoadingState.LOADED)
                        } else {
                            Log.d("Error", "1: ${response.errorBody()}")
//                            getMovieDetailLoadingState.emit(LoadingState.ERROR)
                            snackbar(
                                getApplication<Application>().getString(R.string.error_occurred),
                                SnackbarDuration.Short
                            )
                        }
                    } ?: withContext(Dispatchers.Main) {
//                        getMovieDetailLoadingState.emit(LoadingState.ERROR)
                        snackbar(
                            getApplication<Application>().getString(R.string.connection_time_out),
                            SnackbarDuration.Short
                        )
                    }
                } catch (e: Exception) {
                    Log.d("Error", "2: ${e.message}")
//                    getMovieDetailLoadingState.emit(LoadingState.ERROR)
                    snackbar(
                        getApplication<Application>().getString(R.string.error_occurred),
                        SnackbarDuration.Short
                    )
                }
            } else {
//                getMovieDetailLoadingState.emit(LoadingState.ERROR)
                snackbar(
                    getApplication<Application>().getString(R.string.device_not_connected_internet),
                    SnackbarDuration.Short
                )
            }
        }
    }


    fun searchMoviesList(
        searchQuery: String,
        snackbar: (String, SnackbarDuration) -> Unit
    ) {
        val query: HashMap<String, String> = HashMap()
        query[QUERY_TERM] = searchQuery
        viewModelScope.launch {
            if (hasInternetConnection(getApplication<Application>())) {
                try {
                    withTimeoutOrNull(TIMEOUT_IN_MILLIS) {
                        getMoviesListLoadingState.emit(LoadingState.LOADING)
                        val response = repository.remote.getMoviesList(queries = query)
                        if (response.isSuccessful) {
                            searchMoviesList.value = response.body()!!.data.movies
                            getMoviesListLoadingState.emit(LoadingState.LOADED)
                        } else {
                            getMoviesListLoadingState.emit(LoadingState.ERROR)
                            Log.d("Error", "1: ${response.errorBody()}")
                            snackbar(
                                getApplication<Application>().getString(R.string.error_occurred),
                                SnackbarDuration.Short
                            )
                        }
                    } ?: withContext(Dispatchers.Main) {
                        getMoviesListLoadingState.emit(LoadingState.ERROR)
                        snackbar(
                            getApplication<Application>().getString(R.string.connection_time_out),
                            SnackbarDuration.Short
                        )
                    }
                } catch (e: Exception) {
                    Log.d("Error", "2: ${e.message}")
                    getMoviesListLoadingState.emit(LoadingState.ERROR)
                    snackbar(
                        getApplication<Application>().getString(R.string.error_occurred),
                        SnackbarDuration.Short
                    )
                }
            } else {
                getMoviesListLoadingState.emit(LoadingState.ERROR)
                snackbar(
                    getApplication<Application>().getString(R.string.device_not_connected_internet),
                    SnackbarDuration.Short
                )
            }
        }
    }
}