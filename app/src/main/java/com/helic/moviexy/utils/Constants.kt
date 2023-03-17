package com.helic.moviexy.utils

object Constants {

    const val ROOT_ROUTE = "root"

    const val TIMEOUT_IN_MILLIS = 15000L

    //API Query
    const val LIMIT_NUMBER = "limit"
    const val PAGE_NUMBER = "page"
    const val SORT_BY = "sort_by"
    const val ORDER_BY = "order_by"
    const val QUERY_TERM = "query_term"
    const val GENRE = "genre"
    const val MIN_RATING = "minimum_rating"

    const val DEFAULT_MOVIES_NUMBER = 10
    const val DEFAULT_PAGE_NUMBER = 1
    const val DEFAULT_SORT_BY = "date_added"
    const val DEFAULT_ORDER_BY = "desc"
    const val DEFAULT_QUERY_TERM = "0"
    const val DEFAULT_GENRE = "All"
    const val DEFAULT_MIN_RATING = "0"

    const val MOVIE_ID = "movie_id"

    // ROOM Database
    const val DATABASE_NAME = "movies_database"
    const val FAVORITE_MOVIES_TABLE = "favorite_movies_table"


    const val BASE_URL = "https://yts.mx/api/v2/"
}