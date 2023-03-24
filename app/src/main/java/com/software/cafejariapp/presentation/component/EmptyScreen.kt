package com.software.cafejariapp.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.software.cafejariapp.R

@Composable
fun EmptyScreen(
    text: String
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 100.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = text,
            style = MaterialTheme.typography.h5,
            color = MaterialTheme.colors.primary
        )

        VerticalSpacer(height = 36.dp)

        Image(
            modifier = Modifier.height(120.dp),
            painter = painterResource(id = R.drawable.empty),
            contentDescription = "비어있음",
            contentScale = ContentScale.FillHeight
        )
    }
}