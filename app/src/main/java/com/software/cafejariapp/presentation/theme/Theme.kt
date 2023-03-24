package com.software.cafejariapp.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorPalette = Colors(
    primary = Color(0xff50342B),
    primaryVariant = Color(0xffA38B83),
    secondary = Color(0xffC2948A),
    secondaryVariant = Color(0xffBC796B),
    background = Color(0xFFF6EBDA),
    surface = MoreLightGray,
    error = Color(0xFFFF6B6B),
    onPrimary = Color(0xffA06C6C),
    onSecondary = Color(0xffA06C6C),
    onBackground = Color(0xff4f342c),
    onSurface = Color(0xff835749),
    onError = Color(0xFFFDF7EA),
    isLight = true
)

@Composable
fun CafeJariAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        // 원래는 다크 컬러 테마
        LightColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}