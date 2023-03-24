package com.software.cafejariapp.presentation.feature.main.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.skydoves.landscapist.CircularReveal
import com.skydoves.landscapist.glide.GlideImage
import com.software.cafejariapp.R

@Composable
fun ProfileImage(
    image: Any?,
    modifier: Modifier = Modifier,
    border: BorderStroke? = null
) {

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(50),
        elevation = 1.dp,
        border = border
    ) {

        GlideImage(
            modifier = modifier,
            imageModel = image,
            contentScale = ContentScale.Crop,
            circularReveal = CircularReveal(),
            placeHolder = painterResource(id = R.drawable.glide_image_placeholder)
        )
    }
}