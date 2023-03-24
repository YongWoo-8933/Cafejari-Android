package com.software.cafejariapp.presentation.feature.map.screen.mapScreen.bottomSheet

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.skydoves.landscapist.glide.GlideImage
import com.software.cafejariapp.R
import com.software.cafejariapp.data.source.remote.HttpRoutes
import com.software.cafejariapp.domain.entity.CafeInfo
import com.software.cafejariapp.presentation.component.VerticalSpacer

@Composable
fun AssociatedCafePart(
    modalCafeInfo: CafeInfo
) {

    val EMPTY = "_none"

    if (modalCafeInfo.moreInfo.event1 != EMPTY || modalCafeInfo.moreInfo.event2 != EMPTY || modalCafeInfo.moreInfo.event3 != EMPTY) {

        Text(
            text = "이 카페에서만 열리는 이벤트",
            style = MaterialTheme.typography.body2
        )

        VerticalSpacer(height = 16.dp)

        if (modalCafeInfo.moreInfo.event1 != EMPTY) {
            Row {

                Text("\uD83C\uDF89  ")

                Text(modalCafeInfo.moreInfo.event1)
            }

            VerticalSpacer(height = 6.dp)
        }

        if (modalCafeInfo.moreInfo.event2 != EMPTY) {
            Row {

                Text("\uD83C\uDF89  ")

                Text(modalCafeInfo.moreInfo.event2)
            }

            VerticalSpacer(height = 6.dp)
        }

        if (modalCafeInfo.moreInfo.event3 != EMPTY) {
            Row {

                Text("\uD83C\uDF89  ")

                Text(modalCafeInfo.moreInfo.event3)
            }

            VerticalSpacer(height = 6.dp)
        }

        VerticalSpacer(height = 40.dp)
    }

    if (modalCafeInfo.moreInfo.image != HttpRoutes.BASE_IMAGE_SERVER_URL + "/" + EMPTY) {
        GlideImage(
            modifier = Modifier.fillMaxWidth(),
            imageModel = modalCafeInfo.moreInfo.image,
            contentScale = ContentScale.FillWidth,
            placeHolder = painterResource(id = R.drawable.glide_image_placeholder)
        )

        VerticalSpacer(height = 40.dp)
    }

    if (modalCafeInfo.moreInfo.notice1 != EMPTY || modalCafeInfo.moreInfo.notice2 != EMPTY || modalCafeInfo.moreInfo.notice3 != EMPTY) {
        Text(
            text = "사장님의 한마디",
            style = MaterialTheme.typography.body2
        )

        VerticalSpacer(height = 16.dp)

        if (modalCafeInfo.moreInfo.notice1 != EMPTY) {
            Row {

                Text("☝  ")

                Text(modalCafeInfo.moreInfo.notice1)
            }

            VerticalSpacer(height = 6.dp)
        }
        if (modalCafeInfo.moreInfo.notice2 != EMPTY) {
            Row {

                Text("☝  ")

                Text(modalCafeInfo.moreInfo.notice2)
            }

            VerticalSpacer(height = 6.dp)
        }
        if (modalCafeInfo.moreInfo.notice3 != EMPTY) {
            Row {

                Text("☝  ")

                Text(modalCafeInfo.moreInfo.notice3)
            }

            VerticalSpacer(height = 6.dp)
        }

        VerticalSpacer(height = 40.dp)
    }
}