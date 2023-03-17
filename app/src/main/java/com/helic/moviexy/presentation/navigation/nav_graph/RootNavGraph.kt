package com.helic.moviexy.presentation.navigation.nav_graph

import androidx.compose.material.SnackbarDuration
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.helic.moviexy.data.viewmodels.MainViewModel
import com.helic.moviexy.presentation.navigation.MainAppScreens
import com.helic.moviexy.presentation.screens.favorite.FavoriteMovieDetails
import com.helic.moviexy.presentation.screens.favorite.FavoriteMovies
import com.helic.moviexy.presentation.screens.home.Home
import com.helic.moviexy.presentation.screens.home.MovieDetails
import com.helic.moviexy.presentation.screens.latest_movies.LatestMovieDetails
import com.helic.moviexy.presentation.screens.latest_movies.LatestMovies
import com.helic.moviexy.presentation.screens.popular_movies.PopularMovieDetails
import com.helic.moviexy.presentation.screens.popular_movies.PopularMovies
import com.helic.moviexy.utils.Constants.ROOT_ROUTE

@Composable
fun RootNavGraph(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    showSnackbar: (String, SnackbarDuration) -> Unit
) {

    NavHost(
        navController = navController,
        startDestination = MainAppScreens.Home.route,
        route = ROOT_ROUTE
    ) {
        composable(route = MainAppScreens.Home.route) {
            Home(
                navController = navController,
                mainViewModel = mainViewModel,
                snackbar = showSnackbar
            )
        }
        composable(route = MainAppScreens.LatestMovies.route) {
            LatestMovies(
                navController = navController,
                mainViewModel = mainViewModel,
                snackbar = showSnackbar
            )
        }
        composable(route = MainAppScreens.MovieDetails.route) {
            MovieDetails(
                navController = navController,
                mainViewModel = mainViewModel,
                snackbar = showSnackbar
            )
        }
        composable(route = MainAppScreens.PopularMovies.route) {
            PopularMovies(
                navController = navController,
                mainViewModel = mainViewModel,
                snackbar = showSnackbar
            )
        }

        composable(route = MainAppScreens.PopularMovieDetails.route) {
            PopularMovieDetails(
                navController = navController,
                mainViewModel = mainViewModel,
                snackbar = showSnackbar
            )
        }

        composable(route = MainAppScreens.LatestMovieDetails.route) {
            LatestMovieDetails(
                navController = navController,
                mainViewModel = mainViewModel,
                snackbar = showSnackbar
            )
        }

        composable(route = MainAppScreens.FavoriteMovies.route) {
            FavoriteMovies(
                navController = navController,
                mainViewModel = mainViewModel,
                snackbar = showSnackbar
            )
        }
        composable(route = MainAppScreens.FavoriteMovieDetails.route) {
            FavoriteMovieDetails(
                navController = navController,
                mainViewModel = mainViewModel,
                snackbar = showSnackbar
            )
        }
    }
}