package com.helic.moviexy.data.network.models.movie


import com.google.gson.annotations.SerializedName

data class Movie(
    @SerializedName("background_image")
    val backgroundImage: String = "",
    @SerializedName("background_image_original")
    val backgroundImageOriginal: String = "",
    @SerializedName("date_uploaded")
    val dateUploaded: String = "",
    @SerializedName("date_uploaded_unix")
    val dateUploadedUnix: Int = 0,
    @SerializedName("description_full")
    val descriptionFull: String = "",
    @SerializedName("description_intro")
    val descriptionIntro: String = "",
    @SerializedName("download_count")
    val downloadCount: Int = 0,
    @SerializedName("genres")
    val genres: List<String> = listOf(),
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("imdb_code")
    val imdbCode: String = "",
    @SerializedName("language")
    val language: String = "",
    @SerializedName("large_cover_image")
    val largeCoverImage: String = "",
    @SerializedName("like_count")
    val likeCount: Int = 0,
    @SerializedName("medium_cover_image")
    val mediumCoverImage: String = "",
    @SerializedName("mpa_rating")
    val mpaRating: String = "",
    @SerializedName("rating")
    val rating: Double = 0.0,
    @SerializedName("runtime")
    val runtime: Int = 0,
    @SerializedName("slug")
    val slug: String = "",
    @SerializedName("small_cover_image")
    val smallCoverImage: String = "",
    @SerializedName("title")
    val title: String = "",
    @SerializedName("title_english")
    val titleEnglish: String = "",
    @SerializedName("title_long")
    val titleLong: String = "",
    @SerializedName("url")
    val url: String = "",
    @SerializedName("year")
    val year: Int = 0,
    @SerializedName("yt_trailer_code")
    val ytTrailerCode: String = ""
)