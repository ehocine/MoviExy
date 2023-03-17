package com.helic.moviexy.presentation.screens.latest_movies

import android.annotation.SuppressLint
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.helic.moviexy.data.network.models.movie.Movie
import com.helic.moviexy.data.viewmodels.MainViewModel
import com.helic.moviexy.presentation.navigation.MainAppScreens
import com.helic.moviexy.presentation.ui.theme.*
import com.helic.moviexy.utils.*
import com.helic.moviexy.utils.Constants.DEFAULT_GENRE
import com.helic.moviexy.utils.Constants.DEFAULT_MIN_RATING
import com.helic.moviexy.utils.Constants.DEFAULT_PAGE_NUMBER
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun LatestMovies(
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

    val getMoviesListLoadingState by mainViewModel.getMoviesListLoadingState.collectAsState()

    val searchText by mainViewModel.searchTextState

    val searchAppBarState: SearchAppBarState by mainViewModel.searchAppBarState

    LaunchedEffect(key1 = true) {
        mainViewModel.getMoviesList(
            queries = mainViewModel.applyQueries(),
            snackbar = snackbar,
            movieCategory = GetMovieCategory.LATEST
        )
    }
    val latestMovies by mainViewModel.latestMoviesList
    val searchMoviesList by mainViewModel.searchMoviesList

    val filteredList: List<Movie> = if (searchText.isEmpty()) {
        latestMovies
    } else {
        searchMoviesList
    }

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
                when (searchAppBarState) {
                    SearchAppBarState.CLOSED -> {
                        LatestMoviesTopAppBar(
                            navController = navController,
                            mainViewModel = mainViewModel,
                            onSortClicked = {
                                scope.launch {
                                    mainViewModel.modalBottomSheetState.show()
                                }
                            },
                            onSearchClicked = {
                                mainViewModel.searchAppBarState.value = SearchAppBarState.OPENED
                            }
                        )
                    }
                    else -> {
                        SearchAppBar(
                            mainViewModel = mainViewModel,
                            onSearch = { query ->
                                mainViewModel.searchTextState.value = query
                                mainViewModel.searchMoviesList(
                                    searchQuery = query,
                                    snackbar = snackbar
                                )
                            }
                        )
                        {
                            mainViewModel.searchAppBarState.value =
                                SearchAppBarState.CLOSED
                            mainViewModel.searchTextState.value = ""

                        }
                    }

                }
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
                                items(filteredList) { movie ->
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
                                            queries = mainViewModel.applyQueries(),
                                            snackbar = snackbar,
                                            movieCategory = GetMovieCategory.LATEST
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
fun LatestMoviesTopAppBar(
    navController: NavController,
    mainViewModel: MainViewModel,
    onSortClicked: () -> Unit,
    onSearchClicked: () -> Unit
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
            Text(text = "Latest Movies", color = LightGray)
        },
        actions = {
            TopBarActions(
                onSortClicked = { onSortClicked() },
                onSearchClicked = { onSearchClicked() },
            )

        }, backgroundColor = MaterialTheme.colors.topAppBarBackgroundColor
    )
}


@Composable
fun SearchAppBar(
    mainViewModel: MainViewModel,
    onSearch: (String) -> Unit,
    onCloseClicked: () -> Unit
) {
    var query by remember { mutableStateOf("") }
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(TOP_APP_BAR_HEIGHT),
        elevation = AppBarDefaults.TopAppBarElevation,
        color = MaterialTheme.colors.topAppBarBackgroundColor
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = query,
            onValueChange = {
                query = it
            },
            placeholder = {
                Text(
                    modifier = Modifier
                        .alpha(ContentAlpha.medium),
                    text = "Search",
                    color = Color.White
                )
            },
            textStyle = TextStyle(
                color = MaterialTheme.colors.topAppBarContentColor,
                fontSize = MaterialTheme.typography.subtitle1.fontSize
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearch(query)
                }
            ),
            leadingIcon = {
                IconButton(
                    modifier = Modifier
                        .alpha(ContentAlpha.disabled),
                    onClick = {}
                ) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Search Icon",
                        tint = MaterialTheme.colors.topAppBarContentColor
                    )
                }
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        if (query.isNotEmpty()) {
                            query = ""
                            mainViewModel.searchTextState.value = ""
                        } else {
                            onCloseClicked()
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Close Icon",
                        tint = MaterialTheme.colors.topAppBarContentColor
                    )
                }
            },
            colors = TextFieldDefaults.textFieldColors(
                cursorColor = MaterialTheme.colors.topAppBarContentColor,
                focusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                backgroundColor = Color.Transparent
            )
        )
    }
}

@Composable
fun TopBarActions(
    onSortClicked: () -> Unit,
    onSearchClicked: () -> Unit
) {
    IconButton(onClick = { onSortClicked() }) {
        Icon(
            imageVector = Icons.Default.Sort,
            contentDescription = "Sort Button",
            tint = MaterialTheme.colors.topAppBarContentColor
        )
    }
    IconButton(onClick = { onSearchClicked() }) {
        Icon(
            imageVector = Icons.Default.Search, contentDescription = "Search Button",
            tint = MaterialTheme.colors.topAppBarContentColor
        )
    }

}
