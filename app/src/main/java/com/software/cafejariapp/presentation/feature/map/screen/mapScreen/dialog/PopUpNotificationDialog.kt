package com.software.cafejariapp.presentation.feature.map.screen.mapScreen.dialog

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewModelScope
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.skydoves.landscapist.glide.GlideImage
import com.software.cafejariapp.R
import com.software.cafejariapp.presentation.component.CustomButton
import com.software.cafejariapp.presentation.component.HorizontalSpacer
import com.software.cafejariapp.domain.entity.PopUpNotification
import com.software.cafejariapp.domain.util.DisableDateId
import com.software.cafejariapp.presentation.GlobalState
import com.software.cafejariapp.presentation.feature.map.event.MapEvent
import com.software.cafejariapp.presentation.feature.map.viewModel.MapViewModel
import com.software.cafejariapp.presentation.theme.HalfTransparentBlack
import com.software.cafejariapp.presentation.theme.Transparent
import com.software.cafejariapp.presentation.theme.White
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(
    ExperimentalComposeUiApi::class,
    ExperimentalPagerApi::class,
    ExperimentalNaverMapApi::class
)
@Composable
fun PopUpNotificationDialog(
    globalState: GlobalState,
    mapViewModel: MapViewModel,
    peekState: MutableState<Boolean>,
    scope: CoroutineScope
) {

    val mapState = mapViewModel.state.value

    Dialog(
        onDismissRequest = {
            mapViewModel.onEvent(MapEvent.ClearPopUpNotifications)
            mapViewModel.onEvent(MapEvent.SetPopUpViewed)
        },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false,
        ),
    ) {

        Card(
            modifier = Modifier
                .width(280.dp)
                .height(334.dp),
            backgroundColor = White,
            shape = MaterialTheme.shapes.large
        ) {

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {

                Box(
                    contentAlignment = Alignment.TopCenter
                ) {

                    if (mapState.popUpNotifications.isNotEmpty()) {
                        HorizontalPager(
                            state = mapState.popUpNotificationPagerState,
                            dragEnabled = true,
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) { index ->

                            GlideImage(
                                imageModel = mapState.popUpNotifications[index].image,
                                modifier = Modifier
                                    .size(280.dp)
                                    .clickable {
                                        val popUpNotification = mapState.popUpNotifications[index]
                                        if (!popUpNotification.hasConnectedCafeInfo()) {
                                            globalState.navigateToWebView(
                                                "자세히 보기",
                                                popUpNotification.url
                                            )
                                        } else {
                                            scope.launch {
                                                globalState.cameraPositionState.animate(
                                                    CameraUpdate.scrollTo(
                                                        LatLng(
                                                            popUpNotification.cafeInfo.latitude,
                                                            popUpNotification.cafeInfo.longitude
                                                        )
                                                    ),
                                                    durationMs = 1500
                                                )
                                            }
                                            globalState.refreshCafeInfos(
                                                latLng = LatLng(
                                                    popUpNotification.cafeInfo.latitude,
                                                    popUpNotification.cafeInfo.longitude
                                                ),
                                                onSuccess = { cafeInfos ->
                                                    val selectedCafeInfo = cafeInfos.find { cafeInfo ->
                                                        cafeInfo.id == popUpNotification.cafeInfo.id
                                                    }
                                                    if (selectedCafeInfo != null) {
                                                        globalState.modalCafeInfo.value = selectedCafeInfo
                                                        if (selectedCafeInfo.cafes.isNotEmpty()) {
                                                            globalState.modalCafe.value = selectedCafeInfo.cafes[0]
                                                        }
                                                        mapViewModel.onEvent(MapEvent.ClearModalPlaceInfo)
                                                        mapViewModel.onEvent(
                                                            MapEvent.FetchModalCafePlaceInfo(
                                                                selectedCafeInfo
                                                            )
                                                        )
                                                        peekState.value = true
                                                    }
                                                }
                                            )
                                        }
                                        mapViewModel.onEvent(MapEvent.ClearPopUpNotifications)
                                        mapViewModel.onEvent(MapEvent.SetPopUpViewed)
                                    },
                                contentScale = ContentScale.FillBounds,
                                placeHolder = painterResource(id = R.drawable.glide_image_placeholder)
                            )
                        }
                    }

                    Row(
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .background(
                                color = HalfTransparentBlack,
                                shape = RoundedCornerShape(50)
                            )
                            .padding(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        HorizontalSpacer(width = 4.dp)

                        mapState.popUpNotifications.forEachIndexed { index, _ ->

                            if (index == mapState.popUpNotificationPagerState.currentPage) {
                                Image(
                                    painter = painterResource(id = R.drawable.stamp_icon),
                                    contentDescription = "선택된페이지",
                                    modifier = Modifier.size(16.dp),
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Filled.Circle,
                                    contentDescription = "미선택된페이지",
                                    modifier = Modifier.size(12.dp),
                                    tint = White.copy(alpha = 0.5f)
                                )
                            }

                            HorizontalSpacer(width = 4.dp)
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .background(MaterialTheme.colors.primary)
                ) {

                    CustomButton(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        text = "오늘하루보지않기",
                        textColor = MaterialTheme.colors.onPrimary,
                        textStyle = MaterialTheme.typography.subtitle2,
                        onClick = {
                            mapViewModel.viewModelScope.launch {
                                mapViewModel.mainUseCase.setTodayDisable(DisableDateId.popUp)
                            }
                            mapViewModel.onEvent(MapEvent.ClearPopUpNotifications)
                            mapViewModel.onEvent(MapEvent.SetPopUpViewed)
                        },
                        backgroundColor = Transparent
                    )
                    CustomButton(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        text = "닫기",
                        textColor = White,
                        textStyle = MaterialTheme.typography.subtitle2,
                        onClick = {
                            mapViewModel.onEvent(MapEvent.ClearPopUpNotifications)
                            mapViewModel.onEvent(MapEvent.SetPopUpViewed)
                        },
                        backgroundColor = Transparent
                    )
                }
            }
        }
    }
}