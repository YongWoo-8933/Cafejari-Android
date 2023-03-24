package com.software.cafejariapp.presentation.feature.map.screen.mapScreen.mapContent

import android.graphics.Bitmap
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.zIndex
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.software.cafejariapp.presentation.component.ZoomableImage

@ExperimentalPagerApi
@Composable
fun ImagePopup(
    modalCafeBitmap: Bitmap?,
    screenTouched: () -> Unit,
){

    val imageState = rememberAsyncImagePainter(model = modalCafeBitmap)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .zIndex(5f),
        contentAlignment = Alignment.Center
    ){

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Black.copy(alpha = 0.8f))
                .clickable { screenTouched() }
        )

        ZoomableImage(
            painter = imageState,
            modifier = Modifier.fillMaxWidth()
        )
    }
}