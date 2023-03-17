package com.helic.moviexy.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Purple200 = Color(0xFFBB86FC)
val Purple500 = Color(0xFF6200EE)
val Purple700 = Color(0xFF3700B3)
val Teal200 = Color(0xFF03DAC5)

val firstColor = Color(0xFF282828)
val secondColor = Color(0xFF6ac045)
val thirdColor = Color(0xFFccccac)


val Green = Color(0xFF00C980)
val Yellow = Color(0xFFFFC114)
val DarkerGray = Color(0xFF141414)
val DarkGray = Color(0xFF4B4B4B)
val MediumGray = Color(0xFF9C9C9C)
val LightGray = Color(0xFFFCFCFC)
val Red = Color(0xFFFF4646)
val BlackWithAlpha = Color(0xAA000000)

val Colors.CardBorderColor: Color
    @Composable
    get() = if (!isSystemInDarkTheme()) secondColor else secondColor

val Colors.CardTitleColor: Color
    @Composable
    get() = if (!isSystemInDarkTheme()) Color.White else Color.White

val Colors.CardDesColor: Color
    @Composable
    get() = if (!isSystemInDarkTheme()) thirdColor else thirdColor

val Colors.topAppBarBackgroundColor: Color
    @Composable
    get() = if (!isSystemInDarkTheme()) Color.Black else Color.Black

val Colors.topAppBarContentColor: Color
    @Composable
    get() = if (!isSystemInDarkTheme()) LightGray else LightGray

val Colors.backgroundColor: Color
    @Composable
    get() = if (!isSystemInDarkTheme()) Color.Black else Color.Black

val Colors.ProgressIndicatorColor: Color
    @Composable
    get() = if (!isSystemInDarkTheme()) secondColor else secondColor

val Colors.ButtonColor: Color
    @Composable
    get() = if (!isSystemInDarkTheme()) secondColor else secondColor

val Colors.DialogNoText: Color
    @Composable
    get() = if (!isSystemInDarkTheme()) secondColor else secondColor