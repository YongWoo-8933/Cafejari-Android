package com.software.cafejariapp.presentation.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.software.cafejariapp.presentation.util.Crowded
import com.software.cafejariapp.presentation.util.TimeUtil
import com.software.cafejariapp.presentation.theme.MoreLightGray
import com.software.cafejariapp.presentation.theme.White

@Composable
fun ClockCard(
    crowded: Crowded,
    timeString: String,

    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    padding: Dp = 8.dp
){

    Card(
        elevation = 0.dp,
        border = BorderStroke(width = 1.dp, color = MoreLightGray),
        shape = RoundedCornerShape(50),
        backgroundColor = White
    ) {

        Row(
            modifier = modifier
                .clickable { onClick() }
                .padding(
                    start = padding,
                    end = padding + 4.dp,
                    top = padding,
                    bottom = padding
                ),
            verticalAlignment = Alignment.CenterVertically
        ){

            Text(
                modifier = Modifier
                    .background(
                        color = crowded.color,
                        shape = RoundedCornerShape(50)
                    )
                    .padding(4.dp),
                text = crowded.string,
                style = MaterialTheme.typography.button,
                color = crowded.complementaryColor,
            )

            HorizontalSpacer(width = 8.dp)

            Text(
                text = TimeUtil.getKoreanHourMinute(timeString),
                style = MaterialTheme.typography.button
            )

            HorizontalSpacer(width = 4.dp)

            Text(
                text = "(${TimeUtil.getPassingTimeFrom(timeString)}전)",
                style = MaterialTheme.typography.caption
            )
        }
    }
}
