package com.software.cafejariapp.presentation.feature.map.screen.mapScreen.bottomSheet

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.software.cafejariapp.R
import com.software.cafejariapp.core.isNearBy
import com.software.cafejariapp.core.toCrowded
import com.software.cafejariapp.core.toFloor
import com.software.cafejariapp.core.useNonBreakingSpace
import com.software.cafejariapp.presentation.GlobalState
import com.software.cafejariapp.presentation.component.*
import com.software.cafejariapp.presentation.feature.map.event.MapEvent
import com.software.cafejariapp.presentation.feature.map.viewModel.MapViewModel
import com.software.cafejariapp.presentation.theme.Gray
import com.software.cafejariapp.presentation.theme.HeavyGray
import com.software.cafejariapp.presentation.theme.LightGray
import com.software.cafejariapp.presentation.theme.White
import com.software.cafejariapp.presentation.util.Screen
import kotlinx.coroutines.launch

@Composable
fun MapBottomSheet(
    globalState: GlobalState,
    mapViewModel: MapViewModel,
    thumbsUpRecentLogIdState: MutableState<Int>,
    onCollapseBottomSheet: () -> Unit,
    onImageClick: (Bitmap) -> Unit
) {

    val mapState = mapViewModel.state.value
    val scope = rememberCoroutineScope()
    val bottomSheetListState = rememberLazyListState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.95f)
            .background(color = White)
    ) {

        if (globalState.modalCafeInfo.value.cafes.isEmpty()) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(80.dp)
                    .padding(20.dp),
                color = Gray
            )
        } else {
            LaunchedEffect(globalState.modalCafeInfo.value) {
                scope.launch {
                    globalState.modalCafe.value = globalState.modalCafeInfo.value.cafes[0]
                    bottomSheetListState.animateScrollToItem(0)
                }
            }

            BottomSheetHandle()

            CafeTitlePart(
                modalCafeInfo = globalState.modalCafeInfo.value,
                modalCafePlaceInfo = mapState.modalCafePlaceInfo
            )

            LazyColumn(
                state = bottomSheetListState,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colors.background
                    )
            ) {

                item {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = White)
                            .padding(
                                horizontal = 20.dp,
                                vertical = 12.dp
                            )
                    ) {

                        val onSaleCafe = mapViewModel.state.value.onSaleCafes.find { it.cafeInfoId == globalState.modalCafeInfo.value.id }

                        when {
                            onSaleCafe != null -> {

                                OnSaleCard(onSaleCafe = onSaleCafe)

                                VerticalSpacer(height = 20.dp)
                            }
                            !globalState.isMasterActivated.value -> {

                                CrowdedSharingNavigationButton(
                                    modalCafeInfo = globalState.modalCafeInfo.value,
                                    modalCafe = globalState.modalCafe.value,
                                    onClick = {
                                        if (globalState.modalCafe.value.master.userId == 0) {
                                            globalState.navController.navigate(Screen.MasterRoomScreen.route)
                                            onCollapseBottomSheet()
                                        }
                                    }
                                )

                                VerticalSpacer(height = 20.dp)
                            }
                        }

                        if (globalState.modalCafeInfo.value.cafes.isEmpty()) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .size(80.dp)
                                    .padding(20.dp),
                                color = Gray
                            )
                        } else  {
                            FloorTabRow(
                                modalCafeInfo = globalState.modalCafeInfo.value,
                                modalCafe = globalState.modalCafe.value,
                                onFloorClick = { cafe ->
                                    if (globalState.modalCafe.value.id != cafe.id) {
                                        globalState.modalCafe.value = cafe
                                    }
                                }
                            )

                            VerticalSpacer(height = 16.dp)

                            Text(
                                text = globalState.modalCafe.value.floor.toFloor() + "층 카페 혼잡도",
                                style = MaterialTheme.typography.body2
                            )

                            VerticalSpacer(height = 4.dp)

                            if (globalState.modalCafe.value.master.userId != 0) {
                                Text(
                                    text = "현재 " + globalState.modalCafe.value.floor.toFloor() + "층은 " + globalState.modalCafe.value.master.nickname + "님이 활동 중이에요",
                                    style = MaterialTheme.typography.caption,
                                    color = HeavyGray
                                )
                            } else {
                                Text(
                                    text = "현재 활동중인 마스터가 없어요!",
                                    style = MaterialTheme.typography.caption,
                                    color = HeavyGray
                                )
                            }

                            VerticalSpacer(height = 16.dp)

                            CrowdedDescriptionCard(modalCafe = globalState.modalCafe.value)

                            VerticalSpacer(height = 16.dp)

                            CrowdedSharingOrThumbsUpButton(
                                globalState = globalState,
                                onCollapseBottomSheet = onCollapseBottomSheet,
                                thumbsUpRecentLogIdState = thumbsUpRecentLogIdState
                            )

                            if (globalState.modalCafe.value.recentUpdatedLogs.isNotEmpty()) {

                                VerticalSpacer(height = 40.dp)

                                Text(
                                    text = "최근 3시간 카페 혼잡도",
                                    style = MaterialTheme.typography.body2
                                )

                                VerticalSpacer(height = 16.dp)

                                LazyRow {

                                    globalState.modalCafe.value.recentUpdatedLogs.forEach { recentLog ->
                                        item {

                                            ClockCard(
                                                crowded = recentLog.crowded.toCrowded(),
                                                timeString = recentLog.update,
                                            )

                                            HorizontalSpacer(width = 8.dp)
                                        }
                                    }
                                }
                            }

                            VerticalSpacer(height = 20.dp)
                        }
                    }

                    VerticalSpacer(height = 12.dp)
                }

                item {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(White)
                            .padding(
                                horizontal = 20.dp,
                                vertical = 40.dp
                            )
                    ) {

                        if (globalState.modalCafeInfo.value.moreInfo.id != 0) {
                            AssociatedCafePart(modalCafeInfo = globalState.modalCafeInfo.value)
                        }

                        Text(
                            text = "매장 정보",
                            style = MaterialTheme.typography.body2
                        )

                        VerticalSpacer(height = 16.dp)

                        if (mapState.isPlaceInfoLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .size(80.dp)
                                    .padding(20.dp),
                                color = HeavyGray
                            )
                        } else {

                            CafeImagePart(
                                mapViewModel = mapViewModel,
                                onImageClick = onImageClick
                            )

                            VerticalSpacer(height = 20.dp)

                            CafeInformationPart(
                                globalState = globalState,
                                modalCafePlaceInfo = mapState.modalCafePlaceInfo
                            )

                            VerticalSpacer(height = 100.dp)

                            if (mapState.modalAttributionTexts.isNotEmpty()) {
                                var attributionText = "위 사진은 google place api에서 제공되며, google map 닉네임 "
                                mapState.modalAttributionTexts.forEach { attribution ->
                                    if (attribution.parseAttribution().isNotBlank()) {
                                        attributionText += attribution.parseAttribution() + "님, "
                                    }
                                }
                                attributionText += "에게 저작권이 있습니다. 무단으로 사용 및 배포할 경우 google maps platform 서비스 정책에 따라 책임을 물을 수 있습니다."

                                Text(
                                    text = attributionText.useNonBreakingSpace(),
                                    style = MaterialTheme.typography.overline,
                                    color = LightGray
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun String?.parseAttribution(): String {
    return try {
        val text = this.orEmpty()
        if (text.isBlank()) {
            ""
        } else {
            val startIndex = text.indexOf(">") + 1
            val finishIndex = text.indexOf("</a>") - 1
            text.substring(startIndex..finishIndex)
        }
    } catch (e: IndexOutOfBoundsException) {
        ""
    }
}