package com.software.cafejariapp.presentation.feature.map.screen.mapScreen.mapContent

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.software.cafejariapp.domain.entity.Event
import com.software.cafejariapp.presentation.component.HorizontalSpacer
import com.software.cafejariapp.presentation.theme.Gray
import com.software.cafejariapp.presentation.theme.HalfTransparentBlack
import com.software.cafejariapp.presentation.theme.White

@Composable
fun RandomEventBanner(
    event: Event,
    isEventLoading: Boolean,
    onClick: () -> Unit
) {

    Button(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .height(52.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = HalfTransparentBlack
        ),
        elevation = ButtonDefaults.elevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp
        ),
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "\uD83C\uDF89",
                    style = MaterialTheme.typography.h6
                )

                Text(
                    text = "EVENT",
                    style = MaterialTheme.typography.overline,
                    fontWeight = FontWeight.Bold,
                    color = White
                )
            }

            HorizontalSpacer(width = 12.dp)

            if (isEventLoading) {
                CircularProgressIndicator(
                    color = White,
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(16.dp)
                )
            } else {
                Text(
                    text = event.preview,
                    style = MaterialTheme.typography.body1,
                    color = White
                )
            }
        }
    }
}