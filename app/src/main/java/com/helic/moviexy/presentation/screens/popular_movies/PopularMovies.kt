package com.helic.moviexy.presentation.screens.popular_movies

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Sort
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.helic.moviexy.data.viewmodels.MainViewModel
import com.helic.moviexy.presentation.navigation.MainAppScreens
import com.helic.moviexy.presentation.screens.latest_movies.BottomSheetContent
import com.helic.moviexy.presentation.ui.theme.*
import com.helic.moviexy.utils.*
import com.helic.moviexy.utils.Constants.DEFAULT_GENRE
import com.helic.moviexy.utils.Constants.DEFAULT_MIN_RATING
import com.helic.moviexy.utils.Constants.DEFAULT_PAGE_NUMBER
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun PopularMovies(
    navController: NavController,
    mainViewModel: MainViewModel,
    snackbar: (String, SnackbarDuration) -> Unit
) {
    val scope = rememberCoroutineScope()
    mainViewModel.moviesNumberLimit.value = 50

    if (mainViewModel.firstTimeInScreen.value) {
        mainViewModel.pageNumber.value = DEFAULT_PAGE_NUMBER
        mainViewModel.selectedGenre.value = DEFAULT_GENRE
        mainViewModel.selectedMinimumRating.value = DEFAULT_MIN_RATING
        mainViewModel.firstTimeInScreen.value = false
    }
    Log.d("Tag", "Page: ${mainViewModel.pageNumber.value}")
    Log.d("Tag", "Page: ${mainViewModel.applyPopularMoviesQueries()}")
    val getMoviesListLoadingState by mainViewModel.getMoviesListLoadingState.collectAsState()

    LaunchedEffect(key1 = true) {
        mainViewModel.getMoviesList(
            queries = mainViewModel.applyPopularMoviesQueries(),
            snackbar = snackbar,
            movieCategory = GetMovieCategory.POPULAR
        )
    }
    val popularMovies by mainViewModel.popularMoviesList
    val listState = rememberLazyListState()

    ModalBottomSheetLayout(
        sheetContent = {
            BottomSheetContent(mainViewModel = mainViewModel, snackbar = snackbar)
        },
        sheetState = mainViewModel.modalBottomSheetState,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
    ) {
        Scaffold(
            topBar = {
                PopularMoviesTopAppBar(
                    navController = navController,
                    mainViewModel = mainViewModel,
                    onSortClicked = {
                        scope.launch {
                            mainViewModel.modalBottomSheetState.show()
                        }
                    }
                )
            }) {
            when (getMoviesListLoadingState) {
                LoadingState.LOADING -> {
                    LoadingList()
                }
                LoadingState.ERROR -> {
                    ErrorLoadingResults(mainViewModel = mainViewModel, snackbar = snackbar)
                }
                LoadingState.LOADED -> {
                    Surface(Modifier.fillMaxSize(), color = MaterialTheme.colors.backgroundColor) {
                        Column(
                            Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colors.backgroundColor)
                        ) {
                            LazyColumn(
                                state = listState,
                                modifier = Modifier.weight(1f)
                            ) {
                                items(popularMovies) { movie ->
                                    LatestMovieItem(
                                        navController = navController,
                                        mainViewModel = mainViewModel,
                                        movie = movie
                                    )
                                }
                            }
                            val scrollContext = rememberScrollContext(listState)
                            AnimatedVisibility(
                                scrollContext.isBottom,
                            ) {
                                Button(
                                    onClick = {
                                        mainViewModel.pageNumber.value += 1
                                        mainViewModel.getMoviesList(
                                            queries = mainViewModel.applyPopularMoviesQueries(),
                                            snackbar = snackbar,
                                            movieCategory = GetMovieCategory.POPULAR
                                        )
                                        if (getMoviesListLoadingState == LoadingState.LOADED) {
                                            scope.launch {
                                                // Scroll to the first item
                                                listState.scrollToItem(index = 0)
                                            }
                                        }
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(50.dp),
                                    colors = ButtonDefaults.buttonColors(MaterialTheme.colors.ButtonColor)
                                ) {
                                    Text(
                                        text = "Load more",
                                        fontSize = 16.sp,
                                        color = Color.White
                                    )

                                }
                            }
                        }

                    }
                }
                else -> Unit
            }
        }
    }
}


@Composable
fun PopularMoviesTopAppBar(
    navController: NavController,
    mainViewModel: MainViewModel,
    onSortClicked: () -> Unit
) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = {
                mainViewModel.firstTimeInScreen.value = false
                navController.navigate(MainAppScreens.Home.route) {
                    popUpTo(navController.graph.findStartDestination().id)
                    launchSingleTop = true
                }
            }) {
                Icon(
                    imageVector = Icons.Default.ArrowBackIos,
                    contentDescription = "Back Arrow",
                    tint = MaterialTheme.colors.topAppBarContentColor
                )
            }
        },
        title = {
            Text(text = "Popular Movies", color = LightGray)
        },
        actions = {
            TopBarActions(
                onSortClicked = { onSortClicked() }
            )

        }, backgroundColor = MaterialTheme.colors.topAppBarBackgroundColor
    )
}

@Composable
fun TopBarActions(
    onSortClicked: () -> Unit
) {
    IconButton(onClick = { onSortClicked() }) {
        Icon(
            imageVector = Icons.Default.Sort,
            contentDescription = "Sort Button",
            tint = MaterialTheme.colors.topAppBarContentColor
        )
    }


}
