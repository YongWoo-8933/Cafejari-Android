package com.software.cafejariapp.presentation.feature.map.screen.mapScreen.bottomSheet

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.software.cafejariapp.core.toFloor
import com.software.cafejariapp.domain.entity.Cafe
import com.software.cafejariapp.domain.entity.CafeInfo
import com.software.cafejariapp.presentation.component.HorizontalSpacer
import com.software.cafejariapp.presentation.theme.HeavyGray
import com.software.cafejariapp.presentation.theme.MoreLightGray
import com.software.cafejariapp.presentation.theme.White

@Composable
fun FloorTabRow(
    modalCafeInfo: CafeInfo,
    modalCafe: Cafe,
    onFloorClick: (Cafe) -> Unit
) {

    Row(
        modifier = Modifier.fillMaxWidth()
    ) {

        modalCafeInfo.cafes.forEach { cafe ->
            Button(
                modifier = Modifier
                    .width(64.dp)
                    .height(32.dp),
                onClick = { onFloorClick(cafe) },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = if (modalCafe.id == cafe.id) White else MoreLightGray
                ),
                border = if (modalCafe.id == cafe.id) {
                    BorderStroke(
                        width = (1.5).dp,
                        color = MaterialTheme.colors.primary
                    )
                } else null,
                shape = MaterialTheme.shapes.medium,
                contentPadding = PaddingValues(0.dp),
                elevation = ButtonDefaults.elevation(
                    defaultElevation = 0.dp,
                    pressedElevation = 0.dp
                )
            ) {

                Text(
                    text = "${cafe.floor.toFloor()}층",
                    style = MaterialTheme.typography.body2,
                    color = if (modalCafe.id == cafe.id) MaterialTheme.colors.primary else HeavyGray,
                )
            }

            HorizontalSpacer(width = 8.dp)
        }
    }
}