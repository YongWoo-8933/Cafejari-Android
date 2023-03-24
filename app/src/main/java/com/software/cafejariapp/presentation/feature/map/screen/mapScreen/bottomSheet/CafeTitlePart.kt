package com.software.cafejariapp.presentation.feature.map.screen.mapScreen.bottomSheet

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.android.libraries.places.api.model.Place
import com.software.cafejariapp.R
import com.software.cafejariapp.domain.entity.CafeInfo
import com.software.cafejariapp.presentation.component.HorizontalSpacer
import com.software.cafejariapp.presentation.theme.LightGray
import com.software.cafejariapp.presentation.theme.White

@Composable
fun CafeTitlePart(
    modalCafeInfo: CafeInfo,
    modalCafePlaceInfo: Place?
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = White)
    ) {

        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                painter = painterResource(
                    if (modalCafePlaceInfo?.isOpen == false) {
                        R.drawable.gray_coffee_bean_marker
                    } else {
                        R.drawable.coffee_bean_marker
                    }
                ),
                contentDescription = "커피마커",
                modifier = Modifier.size(32.dp).padding(top = 4.dp)
            )

            HorizontalSpacer(width = 12.dp)

            if (modalCafePlaceInfo?.isOpen == false) {
                Text(
                    text = modalCafeInfo.name + " (영업종료)",
                    style = MaterialTheme.typography.subtitle2,
                    color = LightGray
                )
            } else {
                Text(
                    text = modalCafeInfo.name,
                    style = MaterialTheme.typography.subtitle2,
                    color = MaterialTheme.colors.primary
                )
            }
        }
    }
}