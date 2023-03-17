package com.helic.moviexy.presentation.screens.home

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.helic.moviexy.R
import com.helic.moviexy.data.network.models.movie.Movie
import com.helic.moviexy.data.viewmodels.MainViewModel
import com.helic.moviexy.presentation.navigation.MainAppScreens
import com.helic.moviexy.presentation.ui.theme.*
import com.helic.moviexy.utils.Constants.DEFAULT_GENRE
import com.helic.moviexy.utils.Constants.DEFAULT_MIN_RATING
import com.helic.moviexy.utils.Constants.DEFAULT_PAGE_NUMBER
import com.helic.moviexy.utils.ErrorLoadingResults
import com.helic.moviexy.utils.GetMovieCategory
import com.helic.moviexy.utils.LoadingList
import com.helic.moviexy.utils.LoadingState

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun Home(
    navController: NavController,
    mainViewModel: MainViewModel,
    snackbar: (String, SnackbarDuration) -> Unit
) {
    val getMoviesListLoadingState by mainViewModel.getMoviesListLoadingState.collectAsState()

    mainViewModel.moviesNumberLimit.value = 10
    mainViewModel.pageNumber.value = DEFAULT_PAGE_NUMBER
    mainViewModel.selectedGenre.value = DEFAULT_GENRE
    mainViewModel.selectedMinimumRating.value = DEFAULT_MIN_RATING

    LaunchedEffect(key1 = true) {
        mainViewModel.getMoviesList(
            mainViewModel.applyPopularMoviesQueries(),
            snackbar = snackbar,
            movieCategory = GetMovieCategory.POPULAR
        )
        mainViewModel.getMoviesList(
            mainViewModel.applyQueries(),
            snackbar = snackbar,
            movieCategory = GetMovieCategory.LATEST
        )
    }
    val popularMovies by mainViewModel.popularMoviesList
    val latestMovies by mainViewModel.latestMoviesList

    Scaffold(topBar = {
        HomeTopAppBar(navController = navController)
    }) {
        when (getMoviesListLoadingState) {
            LoadingState.LOADING -> {
                LoadingList()
            }
            LoadingState.ERROR -> {
                ErrorLoadingResults(mainViewModel = mainViewModel, snackbar = snackbar)
            }
            LoadingState.LOADED -> {

                HomeContent(
                    popularMovies = popularMovies,
                    latestMovies = latestMovies,
                    navController = navController,
                    mainViewModel = mainViewModel
                )

            }
            else -> Unit
        }
    }
}

@Composable
fun HomeTopAppBar(navController: NavController) {
    TopAppBar(
        title = {
            Text(text = "Home", color = LightGray)
        },
        actions = {
            DropMenu {
                navController.navigate(MainAppScreens.FavoriteMovies.route) {
                    popUpTo(navController.graph.findStartDestination().id)
                    launchSingleTop = true
                }
            }
        },
        backgroundColor = MaterialTheme.colors.topAppBarBackgroundColor
    )
}

@Composable
fun HomeContent(
    popularMovies: List<Movie>,
    latestMovies: List<Movie>,
    navController: NavController,
    mainViewModel: MainViewModel
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.backgroundColor)
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            PopularMovies(
                popularMovies = popularMovies,
                navController = navController,
                mainViewModel = mainViewModel
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            LatestMovies(
                latestMovies = latestMovies,
                navController = navController,
                mainViewModel = mainViewModel
            )
        }

    }
}

@Composable
fun PopularMovies(
    popularMovies: List<Movie>,
    navController: NavController,
    mainViewModel: MainViewModel
) {
    Column(Modifier.padding(10.dp)) {
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Popular movies",
                fontSize = MaterialTheme.typography.h6.fontSize,
                fontWeight = FontWeight.Bold,
                color = LightGray
            )
            Text(
                text = "Browse more",
                fontWeight = FontWeight.Bold,
                color = secondColor,
                modifier = Modifier.clickable {
                    mainViewModel.firstTimeInScreen.value = true
                    navController.navigate(MainAppScreens.PopularMovies.route) {
                        popUpTo(navController.graph.findStartDestination().id)
                        launchSingleTop = true
                    }
                })
        }

        Spacer(modifier = Modifier.padding(10.dp))

        LazyRow(modifier = Modifier.fillMaxWidth()) {
            items(popularMovies) { popularMovie ->
                MovieCard(
                    navController = navController,
                    mainViewModel = mainViewModel,
                    movie = popularMovie
                )
            }
        }
    }
}

@Composable
fun LatestMovies(
    latestMovies: List<Movie>,
    navController: NavController,
    mainViewModel: MainViewModel
) {
    Column(Modifier.padding(10.dp)) {

        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Latest movies",
                fontSize = MaterialTheme.typography.h6.fontSize,
                fontWeight = FontWeight.Bold,
                color = LightGray
            )
            Text(
                text = "Browse more",
                color = secondColor,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable {
                    mainViewModel.firstTimeInScreen.value = true
                    navController.navigate(MainAppScreens.LatestMovies.route) {
                        popUpTo(navController.graph.findStartDestination().id)
                        launchSingleTop = true
                    }
                })
        }
        Spacer(modifier = Modifier.padding(10.dp))
        LazyRow(modifier = Modifier.fillMaxWidth()) {
            items(latestMovies) { latestMovie ->
                MovieCard(
                    navController = navController,
                    mainViewModel = mainViewModel,
                    movie = latestMovie
                )
            }
        }
    }
}


@Composable
fun DropMenu(onFavoriteMoviesClicked: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    IconButton(onClick = { expanded = true }) {
        Icon(
            painterResource(id = R.drawable.ic_vert),
            contentDescription = "Menu",
            tint = MaterialTheme.colors.topAppBarContentColor
        )
        DropdownMenu(
            modifier = Modifier.background(firstColor),
            expanded = expanded,
            onDismissRequest = { expanded = false }) {
            DropdownMenuItem(onClick = {
                expanded = false
                onFavoriteMoviesClicked()
            }) {
                Text(
                    text = "Favorite movies",
                    modifier = Modifier.padding(start = 5.dp),
                    color = MediumGray
//                    fontSize = MaterialTheme.typography.subtitle2.fontSize
                )
            }
        }
    }
}