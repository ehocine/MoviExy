package com.helic.moviexy.data.network.models.movies_suggestions


import com.google.gson.annotations.SerializedName
import com.helic.moviexy.data.network.models.movie.Movie

data class Data(
    @SerializedName("movie_count")
    val movieCount: Int,
    @SerializedName("movies")
    val movies: List<Movie>
)