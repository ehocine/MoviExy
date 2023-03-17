package com.helic.moviexy.presentation.screens.popular_movies

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.helic.moviexy.utils.convertMinutes


@Composable
fun LatestMovieItem(
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
            .height(secondCardHeight)
            .clip(RoundedCornerShape(5.dp))
            .clickable {
                mainViewModel.selectedMovieID.value = movie.id
                //Navigate to movie details
                navController.navigate(MainAppScreens.PopularMovieDetails.route) {
                    popUpTo(navController.graph.findStartDestination().id)
                    launchSingleTop = true
                }
            },
        backgroundColor = MaterialTheme.colors.backgroundColor,
        elevation = 4.dp
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            SubcomposeAsyncImage(
                modifier = Modifier
                    .weight(1f)
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
            Column(
                modifier = Modifier
                    .padding(5.dp)
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = movie.title,
//                        fontFamily = fancyFont,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colors.CardTitleColor,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = movie.descriptionFull,
                        fontSize = MaterialTheme.typography.body1.fontSize,
                        color = MaterialTheme.colors.CardDesColor,
                        maxLines = 4,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Row(
                    modifier = Modifier
                        .padding(start = 20.dp, end = 20.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Movie's likes",
                            tint = LightGray
                        )
                        Text(
                            text = "${movie.likeCount}",
                            fontSize = MaterialTheme.typography.subtitle2.fontSize,
                            color = LightGray
                        )
                    }
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = "Movie's time",
                            tint = LightGray
                        )
                        Text(
                            text = convertMinutes(movie.runtime),
                            fontSize = MaterialTheme.typography.subtitle2.fontSize,
                            color = LightGray
                        )
                    }
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Movie's rating",
                            tint = LightGray
                        )
                        Text(
                            text = "${movie.rating}/10",
                            fontSize = MaterialTheme.typography.subtitle2.fontSize,
                            color = LightGray
                        )
                    }
                }
            }
        }
    }
}