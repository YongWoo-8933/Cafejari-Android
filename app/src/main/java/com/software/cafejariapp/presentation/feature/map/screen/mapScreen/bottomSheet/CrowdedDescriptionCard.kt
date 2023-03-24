package com.software.cafejariapp.presentation.feature.map.screen.mapScreen.bottomSheet

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.software.cafejariapp.domain.entity.Cafe
import com.software.cafejariapp.presentation.component.VerticalSpacer
import com.software.cafejariapp.presentation.feature.map.component.HorizontalCrowdedColorBar
import com.software.cafejariapp.presentation.feature.map.component.HorizontalCrowdedGrayBar
import com.software.cafejariapp.presentation.theme.HalfTransparentBlack
import com.software.cafejariapp.presentation.theme.HeavyGray
import com.software.cafejariapp.presentation.theme.MoreLightGray
import com.software.cafejariapp.presentation.theme.White
import com.software.cafejariapp.presentation.util.Crowded
import com.software.cafejariapp.presentation.util.Time

@Composable
fun CrowdedDescriptionCard(
    modalCafe: Cafe
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        border = BorderStroke(
            width = (1.5).dp,
            color = MoreLightGray
        ),
        shape = MaterialTheme.shapes.large,
        backgroundColor = White,
        elevation = 0.dp
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp,
                    vertical = 20.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (modalCafe.recentUpdatedLogs.isEmpty()) {
                Text(
                    text = "최근 3시간내 혼잡도 정보 없음",
                    color = MaterialTheme.colors.primary
                )

                VerticalSpacer(height = 20.dp)
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Crowded.crowdedListWithoutNegative.forEach { crowded ->
                        if (crowded.int == modalCafe.crowded) {
                            Column(
                                modifier = Modifier.weight(1f),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                Text(
                                    text = crowded.string,
                                    color = MaterialTheme.colors.primary,
                                    style = MaterialTheme.typography.body2
                                )

                                Text(
                                    text = "${Time.getPassingTimeFrom(modalCafe.recentUpdatedLogs[0].update)}전",
                                    style = MaterialTheme.typography.overline,
                                    color = HeavyGray,
                                    overflow = TextOverflow.Visible,
                                    textAlign = TextAlign.Center,
                                    maxLines = 2
                                )

                                VerticalSpacer(height = 8.dp)

                                Image(
                                    painter = painterResource(id = crowded.icon),
                                    contentDescription = "혼잡도",
                                    modifier = Modifier.height(24.dp),
                                    alignment = Alignment.Center
                                )

                                VerticalSpacer(height = 12.dp)
                            }
                        } else {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }

            if (modalCafe.recentUpdatedLogs.isEmpty()) {
                HorizontalCrowdedGrayBar()
            } else {
                HorizontalCrowdedColorBar()
            }

            VerticalSpacer(height = 4.dp)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    modifier = Modifier
                        .background(
                            color = HalfTransparentBlack,
                            shape = RoundedCornerShape(50)
                        )
                        .padding(
                            horizontal = 8.dp,
                            vertical = 4.dp
                        ),
                    text = Crowded.crowded0.string,
                    style = MaterialTheme.typography.overline,
                    color = White,
                )

                Text(
                    modifier = Modifier
                        .background(
                            color = HalfTransparentBlack,
                            shape = RoundedCornerShape(50)
                        )
                        .padding(
                            horizontal = 8.dp,
                            vertical = 4.dp
                        ),
                    text = Crowded.crowded4.string,
                    style = MaterialTheme.typography.overline,
                    color = White,
                )
            }
        }
    }
}