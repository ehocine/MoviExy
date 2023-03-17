package com.helic.moviexy

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.gms.ads.MobileAds
import com.helic.moviexy.data.viewmodels.MainViewModel
import com.helic.moviexy.presentation.navigation.nav_graph.RootNavGraph
import com.helic.moviexy.presentation.ui.theme.MoviExyTheme
import com.helic.moviexy.utils.MSnackbar
import com.helic.moviexy.utils.loadInterstitial
import com.helic.moviexy.utils.rememberSnackbarState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    lateinit var navController: NavHostController
    private val mainViewModel: MainViewModel by viewModels()

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //initialize the mobile ads sdk
        MobileAds.initialize(this) {}
        setContent {
            MoviExyTheme {
                val systemUiController = rememberSystemUiController()
                val darkTheme = isSystemInDarkTheme()
                navController = rememberNavController()
                val appState: MSnackbar = rememberSnackbarState()
                SideEffect {
                    systemUiController.setStatusBarColor(
                        color = if (darkTheme) Color.Black else Color.Black
                    )
                }
                Scaffold(
                    scaffoldState = appState.scaffoldState
                ) {
                    RootNavGraph(
                        navController = navController,
                        mainViewModel = mainViewModel,
                        showSnackbar = { message, duration ->
                            appState.showSnackbar(message = message, duration = duration)
                        })
                }
            }
        }
        loadInterstitial(this)
    }
}
