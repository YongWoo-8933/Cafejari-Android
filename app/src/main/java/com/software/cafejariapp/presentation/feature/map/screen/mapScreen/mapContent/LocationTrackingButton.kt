package com.software.cafejariapp.presentation.feature.map.screen.mapScreen.mapContent

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MyLocation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.software.cafejariapp.core.isLocationTrackingPermitted
import com.software.cafejariapp.presentation.GlobalState
import com.software.cafejariapp.presentation.feature.map.util.Locations
import com.software.cafejariapp.presentation.theme.HeavyGray
import com.software.cafejariapp.presentation.theme.White
import kotlinx.coroutines.launch

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun LocationTrackingButton(
    globalState: GlobalState
) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    if (context.isLocationTrackingPermitted()) {
        Card(
            modifier = Modifier.size(56.dp),
            backgroundColor = White,
            elevation = 2.dp,
            shape = RoundedCornerShape(50)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable {
                        globalState.refreshCafeInfos(
                            LatLng(
                                globalState.userLocation.value?.latitude
                                    ?: Locations.sinchon.cameraPosition.target.latitude,
                                globalState.userLocation.value?.longitude
                                    ?: Locations.sinchon.cameraPosition.target.longitude
                            )
                        )
                        scope.launch {
                            globalState.cameraPositionState.animate(
                                CameraUpdate.scrollTo(
                                    LatLng(
                                        globalState.userLocation.value?.latitude
                                            ?: Locations.sinchon.cameraPosition.target.latitude,
                                        globalState.userLocation.value?.longitude
                                            ?: Locations.sinchon.cameraPosition.target.longitude
                                    )
                                ),
                                durationMs = 1500
                            )
                        }
                        globalState.startLocationTracking(context)
                    },
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                Icon(
                    modifier = Modifier.size(28.dp),
                    imageVector = Icons.Rounded.MyLocation,
                    contentDescription = "내위치로이동",
                    tint = HeavyGray,
                )
            }
        }
    }
}