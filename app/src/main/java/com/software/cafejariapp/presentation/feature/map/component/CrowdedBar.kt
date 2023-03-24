package com.software.cafejariapp.presentation.feature.map.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.software.cafejariapp.presentation.component.VerticalSpacer
import com.software.cafejariapp.presentation.theme.*
import com.software.cafejariapp.presentation.util.Crowded


@Composable
fun HorizontalCrowdedColorBar() {

    Card(
        modifier = Modifier
            .height(10.dp)
            .fillMaxWidth(),
        border = BorderStroke(
            width = 1.dp,
            color = White
        ),
        shape = RoundedCornerShape(50),
        elevation = 0.dp
    ) {

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {

            Crowded.crowdedListWithoutNegative.forEach { crowded ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .background(color = crowded.color)
                        .weight(1f)
                        .border(
                            width = (0.5).dp,
                            color = White
                        )
                ) {

                }
            }
        }
    }
}


@Composable
fun HorizontalCrowdedGrayBar() {
    Card(
        modifier = Modifier
            .height(10.dp)
            .fillMaxWidth(),
        border = BorderStroke(
            width = 1.dp,
            color = White
        ),
        shape = RoundedCornerShape(50),
        elevation = 0.dp
    ) {

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {

            for (i in 0..4) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .background(color = LightGray)
                        .weight(1f)
                        .border(
                            width = (0.5).dp,
                            color = White
                        )
                ) {

                }
            }
        }
    }
}


@Composable
fun VerticalCrowdedColorBar() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            modifier = Modifier
                .background(
                    color = HalfTransparentBlack,
                    shape = RoundedCornerShape(50)
                )
                .padding(4.dp),
            text = Crowded.crowded4.string,
            style = MaterialTheme.typography.overline,
            fontWeight = FontWeight.ExtraBold,
            color = White
        )

        VerticalSpacer(height = 4.dp)

        Card(
            modifier = Modifier
                .height(180.dp)
                .width(10.dp),
            border = BorderStroke(
                width = 1.dp,
                color = White
            ),
            shape = RoundedCornerShape(50),
            elevation = 2.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
            ) {

                Crowded.crowdedListWithoutNegative.reversed().forEach { crowded ->
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(10.dp)
                            .weight(1f)
                            .background(color = crowded.color)
                            .border(
                                width = (0.5).dp,
                                color = White
                            )
                    ) {

                    }
                }
            }
        }

        VerticalSpacer(height = 4.dp)

        Text(
            modifier = Modifier
                .background(
                    color = HalfTransparentBlack,
                    shape = RoundedCornerShape(50)
                )
                .padding(4.dp),
            text = Crowded.crowded0.string,
            style = MaterialTheme.typography.overline,
            fontWeight = FontWeight.ExtraBold,
            color = White
        )
    }
}


@Composable
fun VerticalMasterAvailabilityColorBar() {
    Column(
        modifier = Modifier.height(160.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            modifier = Modifier
                .background(
                    color = HalfTransparentBlack,
                    shape = RoundedCornerShape(50)
                )
                .padding(4.dp),
            text = "가능",
            style = MaterialTheme.typography.overline,
            fontWeight = FontWeight.ExtraBold,
            color = White
        )

        VerticalSpacer(height = 4.dp)

        Card(
            modifier = Modifier
                .height(100.dp)
                .width(10.dp),
            border = BorderStroke(
                width = 1.dp,
                color = White
            ),
            shape = RoundedCornerShape(50),
            elevation = 2.dp
        ) {

            Column(
                modifier = Modifier.fillMaxHeight()
            ) {

                Column(
                    modifier = Modifier.fillMaxHeight()
                        .width(10.dp)
                        .weight(1f)
                        .background(color = CrowdedGreen)
                        .border(
                            width = (0.5).dp,
                            color = White
                        )
                ) {

                }

                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(10.dp)
                        .weight(1f)
                        .background(color = CrowdedRed)
                        .border(
                            width = (0.5).dp,
                            color = White
                        )
                ) {

                }
            }
        }

        VerticalSpacer(height = 4.dp)

        Text(
            modifier = Modifier
                .background(
                    color = HalfTransparentBlack,
                    shape = RoundedCornerShape(50)
                )
                .padding(4.dp),
            text = "불가능",
            style = MaterialTheme.typography.overline,
            fontWeight = FontWeight.ExtraBold,
            color = White
        )
    }
}