package com.software.cafejariapp.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.software.cafejariapp.presentation.theme.HalfTransparentBlack
import com.software.cafejariapp.presentation.theme.White

@Composable
fun FullSizeLoadingScreen(
    modifier: Modifier = Modifier,
    loadingImage: @Composable () -> Unit = {
        CircularProgressIndicator(
            modifier = Modifier
                .size(80.dp)
                .padding(20.dp),
            color = White
        )
    },
    loadingText: String = "로딩중.."
) {

    Box(
        modifier = modifier.fillMaxSize()
            .background(color = HalfTransparentBlack)
            .zIndex(10f)
            .clickable { },
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            loadingImage()

            VerticalSpacer(height = 12.dp)

            Text(
                text = loadingText,
                style = MaterialTheme.typography.subtitle1,
                color = White
            )
        }
    }
}