package com.software.cafejariapp.presentation.feature.map.screen.mapScreen.bottomSheet

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.software.cafejariapp.R
import com.software.cafejariapp.domain.entity.OnSaleCafe
import com.software.cafejariapp.presentation.theme.White

@Composable
fun OnSaleCard(
    onSaleCafe: OnSaleCafe
) {

    Box {

        Card(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = Color(0xFFFF3E00),
            shape = MaterialTheme.shapes.small,
            elevation = 0.dp
        ) {

            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(
                        horizontal = 16.dp,
                        vertical = 18.dp
                    )
            ) {

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 52.dp),
                    text = onSaleCafe.saleContent,
                    color = White,
                    style = MaterialTheme.typography.body2
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 8.dp),
            horizontalArrangement = Arrangement.End
        ) {

            Image(
                painter = painterResource(id = R.drawable.paper_coffee_cup),
                contentDescription = "종이컵사진",
                modifier = Modifier.height(64.dp).graphicsLayer { translationY = -30f },
                contentScale = ContentScale.FillHeight
            )
        }
    }
}