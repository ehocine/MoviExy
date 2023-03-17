package com.helic.moviexy.data.network.models.movie_details


import com.google.gson.annotations.SerializedName

data class MovieDetailsResponse(
    @SerializedName("data")
    val data: Data,
    @SerializedName("status")
    val status: String,
    @SerializedName("status_message")
    val statusMessage: String
)