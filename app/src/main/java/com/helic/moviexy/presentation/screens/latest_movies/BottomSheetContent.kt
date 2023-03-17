package com.helic.moviexy.presentation.screens.latest_movies

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.helic.moviexy.data.viewmodels.MainViewModel
import com.helic.moviexy.presentation.ui.theme.ButtonColor
import com.helic.moviexy.presentation.ui.theme.LightGray
import com.helic.moviexy.presentation.ui.theme.backgroundColor
import com.helic.moviexy.utils.Constants.DEFAULT_PAGE_NUMBER
import com.helic.moviexy.utils.DropDownOptions
import com.helic.moviexy.utils.GetMovieCategory
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheetContent(mainViewModel: MainViewModel, snackbar: (String, SnackbarDuration) -> Unit) {

    val scope = rememberCoroutineScope()

    val listOfGenres = listOf(
        "All",
        "Action",
        "Adventure",
        "Animation",
        "Biography",
        "Comedy",
        "Crime",
        "Documentary",
        "Drama",
        "Family",
        "Fantasy",
        "Film Noir",
        "History",
        "Horror",
        "Music",
        "Musical",
        "Mystery",
        "Romance",
        "Sci-Fi",
        "Short Film",
        "Sport",
        "Superhero",
        "Thriller",
        "War",
        "Western"
    )
    val listOfRating = listOf(
        "All",
        "9+",
        "8+",
        "7+",
        "6+",
        "5+",
        "4+",
        "3+",
        "2+",
        "1+"

    )

    Column(Modifier.background(MaterialTheme.colors.backgroundColor)) {
        Column(Modifier.padding(15.dp)) {
            Text(
                text = "Genre",
                fontSize = MaterialTheme.typography.h6.fontSize,
                fontWeight = FontWeight.Bold,
                color = LightGray
            )
            DropDownOptions(
                label = mainViewModel.selectedGenre.value,
                optionsList = listOfGenres,
                onOptionSelected = {
                    mainViewModel.selectedGenre.value = it
                })
        }
        Column(Modifier.padding(15.dp)) {
            Text(
                text = "Minimum rating",
                fontSize = MaterialTheme.typography.h6.fontSize,
                fontWeight = FontWeight.Bold,
                color = LightGray
            )
            DropDownOptions(
                label = mainViewModel.selectedMinimumRating.value,
                optionsList = listOfRating,
                onOptionSelected = {
                    mainViewModel.selectedMinimumRating.value =
                        it.replace("+", "")
                })
        }
        Box(
            Modifier
                .fillMaxWidth()
                .padding(15.dp),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = {
                    //To Start the search from 1st page
                    mainViewModel.pageNumber.value = DEFAULT_PAGE_NUMBER

                    mainViewModel.getMoviesList(
                        queries = mainViewModel.applyQueries(),
                        snackbar = snackbar,
                        movieCategory = GetMovieCategory.LATEST
                    )
                    mainViewModel.getMoviesList(
                        queries = mainViewModel.applyPopularMoviesQueries(),
                        snackbar = snackbar,
                        movieCategory = GetMovieCategory.POPULAR
                    )
                    scope.launch {
                        mainViewModel.modalBottomSheetState.hide()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(MaterialTheme.colors.ButtonColor)
            ) {
                Text(
                    text = "Apply",
                    fontSize = 16.sp,
                    color = Color.White
                )

            }
        }
    }
}

