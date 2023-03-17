package com.helic.moviexy.presentation.screens.favorite

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.helic.moviexy.R
import com.helic.moviexy.data.viewmodels.MainViewModel
import com.helic.moviexy.presentation.navigation.MainAppScreens
import com.helic.moviexy.presentation.ui.theme.*
import com.helic.moviexy.utils.DisplayAlertDialog
import com.helic.moviexy.utils.NoResults

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun FavoriteMovies(
    navController: NavController,
    mainViewModel: MainViewModel,
    snackbar: (String, SnackbarDuration) -> Unit
) {
    LaunchedEffect(key1 = true) {
        mainViewModel.readFavoriteMovies()
    }
    val favoriteMoviesEntityList by mainViewModel.favoriteMoviesList.collectAsState()

    Scaffold(topBar = {
        FavoriteMoviesTopAppBar(
            navController = navController,
            mainViewModel = mainViewModel,
            snackbar = snackbar
        )
    }) {
        if (favoriteMoviesEntityList.isEmpty()) {
            NoResults()
        } else {
            Surface(Modifier.fillMaxSize(), color = MaterialTheme.colors.backgroundColor) {
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(favoriteMoviesEntityList) { movieEntity ->
                        FavoriteMovieItem(
                            navController = navController,
                            mainViewModel = mainViewModel,
                            movieEntity = movieEntity
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FavoriteMoviesTopAppBar(
    navController: NavController,
    mainViewModel: MainViewModel,
    snackbar: (String, SnackbarDuration) -> Unit
) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = {
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
            Text(text = "Favorite movies", color = LightGray)
        }, actions = {
            DeleteAllAction(
                mainViewModel = mainViewModel,
                snackbar = snackbar
            )
        }, backgroundColor = MaterialTheme.colors.topAppBarBackgroundColor
    )
}

@Composable
fun DeleteAllAction(
    mainViewModel: MainViewModel, snackbar: (String, SnackbarDuration) -> Unit
) {
    var openDeleteAllDialog by remember { mutableStateOf(false) }
    DropMenu(
        onDeleteAllClicked = { openDeleteAllDialog = true },
    )

    DisplayAlertDialog(
        title = "Delete all movies",
        message = {
            Column {
                Text(
                    text = "Are you sure you want to delete all your favorite movies?",
                    fontSize = MaterialTheme.typography.subtitle1.fontSize,
                    color = LightGray
                )
            }
        },
        openDialog = openDeleteAllDialog,
        closeDialog = { openDeleteAllDialog = false },
        onYesClicked = {
            mainViewModel.deleteAllFavoriteMovies()
            snackbar("All movies were deleted", SnackbarDuration.Short)
        }
    )
}

@Composable
fun DropMenu(onDeleteAllClicked: () -> Unit) {
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
                onDeleteAllClicked()
            }) {
                Text(
                    text = "Delete all",
                    modifier = Modifier.padding(start = 5.dp),
                    color = MediumGray
//                    fontSize = MaterialTheme.typography.subtitle2.fontSize
                )
            }
        }
    }
}