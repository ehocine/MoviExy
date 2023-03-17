package com.helic.moviexy.data

import com.helic.moviexy.data.network.api.MoviExyApi
import com.helic.moviexy.data.network.models.movie_details.MovieDetailsResponse
import com.helic.moviexy.data.network.models.movies_list.MoviesListResponse
import com.helic.moviexy.data.network.models.movies_suggestions.MoviesSuggestions
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val moviExyApi: MoviExyApi) {

    suspend fun getMoviesList(queries: Map<String, String>): Response<MoviesListResponse> {
        return moviExyApi.getMoviesList(queries = queries)
    }

    suspend fun getMovieDetails(movieID: Map<String, String>): Response<MovieDetailsResponse> {
        return moviExyApi.getMovieDetails(movieID = movieID)
    }

    suspend fun getMoviesSuggestions(movieID: Map<String, String>): Response<MoviesSuggestions> {
        return moviExyApi.getMoviesSuggestions(movieID = movieID)
    }
}