package com.helic.moviexy.presentation.screens.popular_movies

import android.annotation.SuppressLint
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.helic.moviexy.R
import com.helic.moviexy.data.database.FavoriteMovieEntity
import com.helic.moviexy.data.network.models.movie.Movie
import com.helic.moviexy.data.viewmodels.MainViewModel
import com.helic.moviexy.presentation.navigation.MainAppScreens
import com.helic.moviexy.presentation.screens.home.MovieCard
import com.helic.moviexy.presentation.ui.theme.*
import com.helic.moviexy.utils.convertMinutes
import com.helic.moviexy.utils.showInterstitial
import java.util.*


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun PopularMovieDetails(
    navController: NavController,
    mainViewModel: MainViewModel,
    snackbar: (String, SnackbarDuration) -> Unit
) {
    val context = LocalContext.current
    LaunchedEffect(key1 = true) {
        mainViewModel.getMovieDetails(
            movieID = mainViewModel.selectedMovieID.value,
            snackbar = snackbar
        )
        mainViewModel.getMoviesSuggestions(
            movieID = mainViewModel.selectedMovieID.value,
            snackbar = snackbar
        )
    }

    val selectedMovieDetails by mainViewModel.selectedMovieDetails
    val moviesSuggestionsList by mainViewModel.moviesSuggestionsList

    mainViewModel.readFavoriteMovies()

    val favoriteMoviesList by mainViewModel.favoriteMoviesList.collectAsState()

    var movieSaved by remember { mutableStateOf(false) }

    val filteredList = favoriteMoviesList.filter {
        it.movie.id == selectedMovieDetails.id
    }
    if (filteredList.isNotEmpty()) movieSaved = true

    Scaffold(topBar = {
        MovieDetailsAppBar(
            navController = navController,
            mainViewModel = mainViewModel,
            selectedMovie = selectedMovieDetails,
            movieSaved = movieSaved,
            snackbar = snackbar
        )
    }) {
        MovieDetailsContent(
            movie = selectedMovieDetails,
            mainViewModel = mainViewModel,
            navController = navController,
            moviesSuggestions = moviesSuggestionsList
        )
        showInterstitial(context)

    }
}

@Composable
fun MovieDetailsAppBar(
    navController: NavController,
    mainViewModel: MainViewModel,
    selectedMovie: Movie,
    movieSaved: Boolean,
    snackbar: (String, SnackbarDuration) -> Unit
) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = {
                navController.navigate(MainAppScreens.PopularMovies.route) {
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
            Text(
                text = selectedMovie.title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = LightGray
            )
        },
        actions = {
            IconButton(onClick = {
                if (!movieSaved) {
                    mainViewModel.insertFavoriteMovie(FavoriteMovieEntity(0, selectedMovie))
                    snackbar("Added successfully", SnackbarDuration.Short)
                } else {
                    snackbar("Movie already saved", SnackbarDuration.Short)
                }

            }) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "",
                    tint = if (movieSaved) secondColor else LightGray
                )
            }
        }, backgroundColor = MaterialTheme.colors.topAppBarBackgroundColor
    )
}

@Composable
fun MovieDetailsContent(
    movie: Movie,
    mainViewModel: MainViewModel,
    navController: NavController,
    moviesSuggestions: List<Movie>
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.backgroundColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            SubcomposeAsyncImage(
                modifier = Modifier
                    .fillMaxHeight(),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(movie.largeCoverImage)
                    .crossfade(true)
                    .build(),
                contentDescription = "Movie Image"
            ) {
                val state = painter.state
                if (state is AsyncImagePainter.State.Loading || state is AsyncImagePainter.State.Error) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = MaterialTheme.colors.ProgressIndicatorColor)
                    }
                } else {
                    SubcomposeAsyncImageContent(
                        modifier = Modifier.clip(RectangleShape),
                        contentScale = ContentScale.Crop
                    )
                }
            }
            Row(
                Modifier
                    .align(Alignment.TopCenter)
                    .background(Color.Transparent)
            ) {
                // shows a traditional banner test ad
                AndroidView(
                    factory = { context ->
                        AdView(context).apply {
                            setAdSize(AdSize.BANNER)
                            adUnitId = context.getString(R.string.ad_id_banner)
                            loadAd(AdRequest.Builder().build())
                        }
                    }
                )
            }
            Box(
                Modifier
                    .align(Alignment.TopStart)
                    .background(BlackWithAlpha)
                    .padding(10.dp)
            ) {
                Text(
                    text = "Language: ${movie.language.uppercase(Locale.ROOT)}",
                    color = White,
                    fontWeight = FontWeight.Bold
                )
            }
            Row(
                Modifier
                    .align(Alignment.BottomEnd)
                    .background(BlackWithAlpha)
                    .padding(10.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Movie's likes",
                        tint = White
                    )
                    Text(
                        text = "${movie.likeCount}",
                        fontSize = MaterialTheme.typography.subtitle2.fontSize,
                        fontWeight = FontWeight.Bold,
                        color = White
                    )
                }
                Spacer(modifier = Modifier.padding(10.dp))
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = "Movie's time",
                        tint = White
                    )
                    Text(
                        text = convertMinutes(movie.runtime),
                        fontSize = MaterialTheme.typography.subtitle2.fontSize,
                        fontWeight = FontWeight.Bold,
                        color = White
                    )
                }
            }

        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(2f)
                .padding(10.dp)
        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = movie.title,
                    fontSize = MaterialTheme.typography.h6.fontSize,
                    fontWeight = FontWeight.Bold,
                    color = LightGray
                )
                Spacer(modifier = Modifier.padding(2.dp))
                Text(text = movie.year.toString(), color = LightGray)
                Spacer(modifier = Modifier.padding(2.dp))
                Text(text = "${movie.rating}/10", color = LightGray)
                Spacer(modifier = Modifier.padding(2.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(text = "Genre:", fontWeight = FontWeight.Bold, color = LightGray)
                    Spacer(modifier = Modifier.padding(2.dp))
                    Row(
                        Modifier
                            .padding(5.dp)
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                    ) {
                        movie.genres.forEach {
                            GenreChip(it)
                        }
                    }
                }
                Spacer(modifier = Modifier.padding(5.dp))
                Text(
                    text = "Description",
                    fontWeight = FontWeight.Bold,
                    fontSize = MaterialTheme.typography.h5.fontSize,
                    color = LightGray
                )
                Spacer(modifier = Modifier.padding(5.dp))
                Column {
                    Text(
                        text = movie.descriptionFull,
                        fontSize = MaterialTheme.typography.subtitle1.fontSize,
                        color = LightGray
                    )
                    Spacer(modifier = Modifier.padding(5.dp))
                    Text(
                        text = "Related movies",
                        fontSize = MaterialTheme.typography.h6.fontSize,
                        fontWeight = FontWeight.Bold,
                        color = LightGray
                    )
                    Spacer(modifier = Modifier.padding(5.dp))
                    LazyRow(modifier = Modifier.fillMaxWidth()) {
                        items(moviesSuggestions) { movie ->
                            MovieCard(
                                navController = navController,
                                mainViewModel = mainViewModel,
                                movie = movie
                            )
                        }
                    }
                }
            }
        }

    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun GenreChip(
    title: String
) {
    Chip(
        modifier = Modifier
            .padding(2.dp),
        onClick = {
        },
        border = BorderStroke(
            ChipDefaults.OutlinedBorderSize,
            MaterialTheme.colors.CardBorderColor
        ),
        colors = ChipDefaults.chipColors(
            backgroundColor = Color.Transparent,
            contentColor = secondColor
        )
    ) {
        Text(title)
    }
}