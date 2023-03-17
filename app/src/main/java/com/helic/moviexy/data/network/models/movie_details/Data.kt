package com.helic.moviexy.data.network.models.movie_details


import com.google.gson.annotations.SerializedName
import com.helic.moviexy.data.network.models.movie.Movie

data class Data(
    @SerializedName("movie")
    val movie: Movie
)