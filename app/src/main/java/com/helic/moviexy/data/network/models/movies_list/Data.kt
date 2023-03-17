package com.helic.moviexy.data.network.models.movies_list


import com.google.gson.annotations.SerializedName
import com.helic.moviexy.data.network.models.movie.Movie

data class Data(
    @SerializedName("limit")
    val limit: Int,
    @SerializedName("movie_count")
    val movieCount: Int,
    @SerializedName("movies")
    val movies: List<Movie>,
    @SerializedName("page_number")
    val pageNumber: Int
)