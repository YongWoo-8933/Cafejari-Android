package com.software.cafejariapp.presentation.feature.map.screen.mapScreen.mapContent

import android.Manifest
import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewModelScope
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.himanshoe.kalendar.common.theme.TextUnit
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.compose.*
import com.naver.maps.map.overlay.OverlayImage
import com.software.cafejariapp.R
import com.software.cafejariapp.presentation.GlobalState
import com.software.cafejariapp.presentation.component.CustomButton
import com.software.cafejariapp.presentation.component.VerticalSpacer
import com.software.cafejariapp.presentation.util.Screen
import com.software.cafejariapp.presentation.feature.map.util.Locations
import com.software.cafejariapp.presentation.feature.map.util.rememberCustomFusedLocationSource
import com.software.cafejariapp.domain.util.DisableDateId
import com.software.cafejariapp.presentation.feature.map.component.VerticalCrowdedColorBar
import com.software.cafejariapp.presentation.feature.map.event.AdEvent
import com.software.cafejariapp.presentation.feature.map.event.MapEvent
import com.software.cafejariapp.presentation.feature.map.viewModel.AdViewModel
import com.software.cafejariapp.presentation.feature.map.viewModel.MapViewModel
import com.software.cafejariapp.presentation.theme.*
import kotlinx.coroutines.*


@OptIn(
    ExperimentalNaverMapApi::class,
    ExperimentalPermissionsApi::class, ExperimentalMaterialApi::class
)
@Composable
fun NaverMapFrame(
    globalState: GlobalState,
    mapViewModel: MapViewModel,

    menuState: MutableState<Boolean>,
    permissionDialogState: MutableState<Boolean>,
    onSaleCafeDialogState: MutableState<Boolean>,
    peekState: MutableState<Boolean>,
    searchModalState: MutableState<Boolean>,
    bottomSheetScaffoldState: BottomSheetScaffoldState,
) {

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val mapState = mapViewModel.state.value
    val isCafeInfoRefreshButtonVisible = remember { mutableStateOf(false) }
    val locationPermissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        ),
        onPermissionsResult = {
            if (it[Manifest.permission.ACCESS_COARSE_LOCATION] == true && it[Manifest.permission.ACCESS_FINE_LOCATION] == true) {
                globalState.startLocationTracking(context)
            } else {
                mapViewModel.viewModelScope.launch {
                    permissionDialogState.value =
                        mapViewModel.mainUseCase.isTodayExecutable(DisableDateId.permission)
                }
            }
        }
    )

    LaunchedEffect(globalState.cameraPositionState.position.target.latitude) {
        if (!globalState.cameraPositionState.isMoving) {
            isCafeInfoRefreshButtonVisible.value = true
            delay(3000L)
            isCafeInfoRefreshButtonVisible.value = false
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    vertical = 32.dp,
                    horizontal = 16.dp
                )
                .zIndex(3f),
            contentAlignment = Alignment.BottomEnd
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                VerticalCrowdedColorBar()

                VerticalSpacer(height = 12.dp)

                LocationTrackingButton(globalState)
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 172.dp)
                .zIndex(3f),
            contentAlignment = Alignment.Center
        ) {

            AnimatedVisibility(
                visible = isCafeInfoRefreshButtonVisible.value,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                CustomButton(
                    contentPadding = 8.dp,
                    shape = RoundedCornerShape(50),
                    backgroundColor = White,
                    borderColor = MaterialTheme.colors.primary,
                    text = if (globalState.isCafeInfoLoading.value) "불러오는 중... " else "현 지도에서 검색 ",
                    textColor = MaterialTheme.colors.primary,
                    textStyle = MaterialTheme.typography.button,
                    iconImageResource = R.drawable.refresh,
                    iconColor = MaterialTheme.colors.primary,
                    elevation = 2.dp,
                    onClick = { globalState.refreshCafeInfos() },
                    enabled = !globalState.isCafeInfoLoading.value
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .zIndex(3f)
                .padding(16.dp)
        ) {

            VerticalSpacer(height = 8.dp)

            RandomEventBanner(
                event = mapState.randomEvent,
                isEventLoading = mapState.isEventLoading,
                onClick = { globalState.navController.navigate(Screen.EventScreen.route) }
            )

            VerticalSpacer(height = 8.dp)

            MapTopButtonRow(
                isMenuOpened = menuState.value,
                onSearchButtonClick = {
                    searchModalState.value = true
                    peekState.value = false
                    scope.launch {
                        bottomSheetScaffoldState.bottomSheetState.collapse()
                    }
                },
                onOnSaleCafeButtonClick = {
                    mapViewModel.onEvent(MapEvent.SortOnSaleCafeByDistance(globalState))
                    onSaleCafeDialogState.value = true
                },
                onRegisterCafeButtonClick = {
                    globalState.navController.navigate(Screen.RegisterCafeScreen.route)
                },
                onMenuButtonClick = {
                    menuState.value = !menuState.value
                    isCafeInfoRefreshButtonVisible.value = false
                },
                onMenuItemButtonClick = { location ->
                    globalState.refreshCafeInfos(
                        location.cameraPosition.target
                    )
                    scope.launch {
                        globalState.cameraPositionState.animate(
                            CameraUpdate.toCameraPosition(
                                location.cameraPosition
                            ), durationMs = 1500
                        )
                    }
                    menuState.value = false
                }
            )
        }

        if(globalState.isMasterActivated.value) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 16.dp, bottom = 32.dp)
                    .zIndex(4f),
                contentAlignment = Alignment.BottomStart
            ){

                CustomButton(
                    backgroundColor = MaterialTheme.colors.primary,
                    shape = RoundedCornerShape(50),
                    elevation = 1.dp,
                    modifier = Modifier
                        .fillMaxWidth(0.75f)
                        .height(52.dp),
                    text = "혼잡도 공유 이어서 하기",
                    textColor = White,
                    textStyle = MaterialTheme.typography.subtitle2,
                    onClick = {
                        globalState.navController.navigate(Screen.MasterRoomScreen.route)
                        globalState.cameraPositionState.move(
                            CameraUpdate.scrollTo(
                                LatLng(
                                    globalState.masterCafeLog.value.latitude,
                                    globalState.masterCafeLog.value.longitude
                                )
                            )
                        )
                        globalState.refreshCafeInfos(
                            LatLng(
                                globalState.masterCafeLog.value.latitude,
                                globalState.masterCafeLog.value.longitude
                            )
                        )
                    },
                )
            }
        }

        NaverMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = globalState.cameraPositionState,
            locationSource = rememberCustomFusedLocationSource(locationPermissionsState),
            properties = MapProperties(
                extent = Locations.koreaLatLngBounds,
                minZoom = 8.0,
                maxZoom = 24.0,
                symbolScale = 0.75f,
                locationTrackingMode = LocationTrackingMode.NoFollow,
            ),
            uiSettings = MapUiSettings(
                isTiltGesturesEnabled = false,
                isCompassEnabled = false,
                isScaleBarEnabled = false,
                isZoomControlEnabled = false,
                isLogoClickEnabled = false
            ),
            onMapClick = { _, _ ->
                menuState.value = false
                peekState.value = false
            },
        ) {

            globalState.cafeInfos.value.forEach { cafeInfo ->
                val markerState = rememberMarkerState(
                    key = cafeInfo.name,
                    position = LatLng(
                        cafeInfo.latitude,
                        cafeInfo.longitude
                    )
                )
                val markerWidth = animateDpAsState(
                    targetValue = if(globalState.modalCafeInfo.value.id == cafeInfo.id) 36.dp else 31.dp,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy
                    )
                )
                val markerHeight = animateDpAsState(
                    targetValue = if(globalState.modalCafeInfo.value.id == cafeInfo.id) 42.dp else 36.dp,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioHighBouncy
                    )
                )

                Marker(
                    icon = OverlayImage.fromResource(cafeInfo.getMinCrowded().icon),
                    width = markerWidth.value,
                    height = markerHeight.value,
                    state = markerState,
                    captionText = if(globalState.modalCafeInfo.value.id == cafeInfo.id) cafeInfo.name else null,
                    captionTextSize = TextUnit.Eighteen,
                    captionAligns = arrayOf(Align.Top, Align.Bottom),
                    subCaptionText = if(globalState.modalCafeInfo.value.id == cafeInfo.id) cafeInfo.getMinCrowded().string else null,
                    subCaptionTextSize = TextUnit.Sixteen,
                    subCaptionColor = cafeInfo.getMinCrowded().complementaryColor,
                    subCaptionHaloColor = cafeInfo.getMinCrowded().color,
                    captionOffset = 2.dp,
                    isHideCollidedSymbols = true,
                    isHideCollidedCaptions = false,
                    isHideCollidedMarkers = false,
                    isForceShowCaption = globalState.modalCafeInfo.value.id == cafeInfo.id,
                    globalZIndex = if(globalState.modalCafeInfo.value.id == cafeInfo.id) 5 else 0,
                    onClick = {
                        globalState.modalCafeInfo.value = cafeInfo
                        if (cafeInfo.cafes.isNotEmpty()) {
                            globalState.modalCafe.value = cafeInfo.cafes[0]
                        }
                        mapViewModel.onEvent(MapEvent.ClearModalPlaceInfo)
                        mapViewModel.onEvent(MapEvent.FetchModalCafePlaceInfo(cafeInfo))
                        peekState.value = true

                        scope.launch {
                            globalState.cameraPositionState.animate(
                                CameraUpdate.scrollTo(
                                    LatLng(cafeInfo.latitude, cafeInfo.longitude)
                                ),
                                durationMs = 1500
                            )
                        }
                        true
                    }
                )
            }
        }
    }
}
