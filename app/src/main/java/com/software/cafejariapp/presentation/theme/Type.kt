package com.software.cafejariapp.presentation.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.software.cafejariapp.R

val interFonts = FontFamily(
    Font(R.font.inter_eb, weight = FontWeight.ExtraBold),
    Font(R.font.inter_b, weight = FontWeight.Bold),
    Font(R.font.inter_r, weight = FontWeight.Medium),
    Font(R.font.inter_t, weight = FontWeight.Light),
)

val Typography = Typography(
    defaultFontFamily = interFonts,
    h1 = TextStyle(
        color = TextBlack,
        fontSize = 24.sp,
        fontFamily = FontFamily(Font(R.font.inter_eb))
    ),
    h2 = TextStyle(
        color = TextBlack,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
    ),
    h3 = TextStyle(
        color = TextBlack,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
    ),
    h4 = TextStyle(
        color = TextBlack,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp,
    ),
    h5 = TextStyle(
        color = TextBlack,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
    ),
    h6 = TextStyle(
        color = TextBlack,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
    ),
    subtitle1 = TextStyle(
        color = TextBlack,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    subtitle2 = TextStyle(
        color = TextBlack,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    body1 = TextStyle(
        color = TextBlack,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 21.sp
    ),
    body2 = TextStyle(
        color = TextBlack,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        lineHeight = 21.sp
    ),
    button = TextStyle(
        color = TextBlack,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
    ),
    caption = TextStyle(
        color = TextBlack,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
    ),
    overline = TextStyle(
        color = TextBlack,
        fontWeight = FontWeight.Medium,
        fontSize = 10.sp,
    )
)