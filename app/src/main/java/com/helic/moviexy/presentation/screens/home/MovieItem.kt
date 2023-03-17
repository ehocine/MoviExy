package com.helic.moviexy.presentation.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import com.helic.moviexy.data.network.models.movie.Movie
import com.helic.moviexy.data.viewmodels.MainViewModel
import com.helic.moviexy.presentation.navigation.MainAppScreens
import com.helic.moviexy.presentation.ui.theme.*

@Composable
fun MovieCard(
    navController: NavController,
    mainViewModel: MainViewModel,
    movie: Movie
) {

    Card(
        modifier = Modifier
            .padding(top = 5.dp, end = 10.dp, start = 10.dp, bottom = 5.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colors.CardBorderColor,
                shape = RoundedCornerShape(5.dp)
            )
            .fillMaxWidth()
            .height(cardHeight)
            .width(cardWidth)
            .clip(RoundedCornerShape(5.dp))
            .clickable {
                mainViewModel.selectedMovieID.value = movie.id
                //Navigate to movie details
                navController.navigate(MainAppScreens.MovieDetails.route) {
                    popUpTo(navController.graph.findStartDestination().id)
                    launchSingleTop = true
                }
            },
        backgroundColor = MaterialTheme.colors.backgroundColor,
        elevation = 4.dp
    ) {
        Box(Modifier.fillMaxSize()) {
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
                    Box(
                        Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colors.backgroundColor),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = MaterialTheme.colors.ProgressIndicatorColor)
                    }
                } else {
                    SubcomposeAsyncImageContent(
                        modifier = Modifier.clip(RectangleShape),
                        contentScale = ContentScale.Crop
                    )
                }
            }
            Column(
                Modifier
                    .background(BlackWithAlpha)
                    .align(Alignment.BottomCenter)
                    .padding(10.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = movie.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = MaterialTheme.typography.subtitle1.fontSize,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "${movie.rating}/10",
                    fontWeight = FontWeight.Bold,
                    fontSize = MaterialTheme.typography.subtitle1.fontSize,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}