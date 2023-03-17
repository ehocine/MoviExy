package com.helic.moviexy.presentation.navigation

sealed class MainAppScreens(
    val route: String
) {
    object Home : MainAppScreens(
        route = "home"
    )

    object PopularMovies : MainAppScreens(
        route = "popular_movies"
    )

    object PopularMovieDetails : MainAppScreens(
        route = "popular_movie_details"
    )

    object LatestMovies : MainAppScreens(
        route = "latest_movies"
    )

    object MovieDetails : MainAppScreens(
        route = "movie_details"
    )

    object LatestMovieDetails : MainAppScreens(
        route = "latest_movie_details"
    )

    object FavoriteMovies : MainAppScreens(
        route = "favorite_movies"
    )

    object FavoriteMovieDetails : MainAppScreens(
        route = "favorite_movie_details"
    )

}
