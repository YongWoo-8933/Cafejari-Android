package com.software.cafejariapp.presentation.feature.map.screen.mapScreen.bottomSheet

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.software.cafejariapp.R
import com.software.cafejariapp.core.toFloor
import com.software.cafejariapp.domain.entity.Cafe
import com.software.cafejariapp.domain.entity.CafeInfo
import com.software.cafejariapp.presentation.theme.White

@Composable
fun CrowdedSharingNavigationButton(
    modalCafeInfo: CafeInfo,
    modalCafe: Cafe,
    onClick: () -> Unit
) {

    Button(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = White),
        onClick = onClick,
        contentPadding = PaddingValues(20.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.background
        ),
        shape = MaterialTheme.shapes.medium,
        elevation = ButtonDefaults.elevation(
            defaultElevation = 0.dp,
            pressedElevation = (-1).dp
        )
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier.weight(4f),
                horizontalAlignment = Alignment.Start
            ) {

                if (modalCafe.isMasterAvailable()) {
                    Row {

                        Text(
                            text = "지금 ",
                            color = MaterialTheme.colors.primary
                        )

                        modalCafeInfo.cafes.forEach { cafe ->
                            if (cafe.master.userId == 0) {
                                if (modalCafeInfo.cafes.last().id == cafe.id) {
                                    Text(
                                        text = cafe.floor.toFloor(),
                                        color = MaterialTheme.colors.primary
                                    )
                                } else {
                                    Text(
                                        text = "${cafe.floor.toFloor()}, ",
                                        color = MaterialTheme.colors.primary
                                    )
                                }
                            }
                        }

                        Text(
                            text = "층에서 활동이 가능해요!",
                            color = MaterialTheme.colors.primary
                        )
                    }

                    Text(
                        text = "혼잡도 3초만에 공유하고 포인트얻기",
                        style = MaterialTheme.typography.subtitle2,
                        color = MaterialTheme.colors.primary
                    )
                } else {
                    Text(
                        text = "현재 혼잡도 공유 불가",
                        color = MaterialTheme.colors.primary
                    )

                    Text(
                        text = "다른 마스터가 이미 활동중이에요",
                        style = MaterialTheme.typography.subtitle2,
                        color = MaterialTheme.colors.primary
                    )
                }
            }

            Image(
                painter = painterResource(id = R.drawable.yellow_armchair),
                contentDescription = "쇼파사진",
                modifier = Modifier.weight(1f)
            )
        }
    }
}