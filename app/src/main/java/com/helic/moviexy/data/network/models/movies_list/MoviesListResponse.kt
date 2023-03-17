package com.helic.moviexy.data.network.models.movies_list


import com.google.gson.annotations.SerializedName

data class MoviesListResponse(
    @SerializedName("data")
    val data: Data,
    @SerializedName("status")
    val status: String,
    @SerializedName("status_message")
    val statusMessage: String
)