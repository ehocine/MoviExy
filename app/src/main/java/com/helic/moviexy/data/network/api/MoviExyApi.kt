package com.helic.moviexy.data.network.api

import com.helic.moviexy.data.network.models.movie_details.MovieDetailsResponse
import com.helic.moviexy.data.network.models.movies_list.MoviesListResponse
import com.helic.moviexy.data.network.models.movies_suggestions.MoviesSuggestions
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface MoviExyApi {

    @GET("list_movies.json")
    suspend fun getMoviesList(
        @QueryMap queries: Map<String, String>
    ): Response<MoviesListResponse>

    @GET("movie_details.json")
    suspend fun getMovieDetails(@QueryMap movieID: Map<String, String>): Response<MovieDetailsResponse>

    @GET("movie_suggestions.json")
    suspend fun getMoviesSuggestions(@QueryMap movieID: Map<String, String>): Response<MoviesSuggestions>
}