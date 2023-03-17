package com.helic.moviexy.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.helic.moviexy.R
import com.helic.moviexy.data.viewmodels.MainViewModel
import com.helic.moviexy.presentation.ui.theme.ButtonColor
import com.helic.moviexy.presentation.ui.theme.MediumGray
import com.helic.moviexy.presentation.ui.theme.ProgressIndicatorColor
import com.helic.moviexy.presentation.ui.theme.backgroundColor


@Composable
fun LoadingList() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.backgroundColor),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(color = MaterialTheme.colors.ProgressIndicatorColor)
    }
}

@Composable
fun ErrorLoadingResults(
    mainViewModel: MainViewModel,
    snackbar: (String, SnackbarDuration) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.backgroundColor),
//            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier.size(120.dp),
            painter = painterResource(id = R.drawable.ic_sentiment),
            contentDescription = "", tint = MediumGray
        )
        Text(
            text = stringResource(R.string.error_loading_data),
            color = MediumGray,
            fontSize = MaterialTheme.typography.h6.fontSize,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.padding(30.dp))
        Button(onClick = {
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
        }, colors = ButtonDefaults.buttonColors(MaterialTheme.colors.ButtonColor)) {
            Text(text = "Refresh", color = White)
        }
    }
}

@Composable
fun NoResults() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.backgroundColor),
//            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier.size(120.dp),
            painter = painterResource(id = R.drawable.ic_sentiment),
            contentDescription = "", tint = MediumGray
        )
        Text(
            text = stringResource(R.string.no_data),
            color = MediumGray,
            fontSize = MaterialTheme.typography.h6.fontSize,
            fontWeight = FontWeight.Bold
        )
    }
}

val LazyListState.isLastItemVisible: Boolean
    get() = layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1

val LazyListState.isFirstItemVisible: Boolean
    get() = firstVisibleItemIndex == 0

@Composable
fun rememberScrollContext(listState: LazyListState): ScrollContext {
    val scrollContext by remember {
        derivedStateOf {
            ScrollContext(
                isTop = listState.isFirstItemVisible,
                isBottom = listState.isLastItemVisible
            )
        }
    }
    return scrollContext
}