package com.software.cafejariapp.presentation.feature.map.screen.masterRoom

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.compose.*
import com.naver.maps.map.overlay.OverlayImage
import com.software.cafejariapp.core.toFloor
import com.software.cafejariapp.presentation.util.Crowded
import com.software.cafejariapp.presentation.feature.map.util.Locations
import com.software.cafejariapp.presentation.feature.map.util.Zoom

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun MiniNaverMap(
    cafeName: String,
    cafeFloor: Int,
    cafeLatLng: LatLng,
    cafeCrowded: Crowded,

    modifier: Modifier = Modifier,
    cameraPositionState: CameraPositionState = rememberCameraPositionState {
        if (cafeLatLng.latitude != 0.0 && cafeLatLng.longitude != 0.0) {
            this.position = CameraPosition(
                LatLng(cafeLatLng.latitude, cafeLatLng.longitude), Zoom.LARGE
            )
        } else {
            this.position = Locations.sinchon.cameraPosition
        }
    }
) {

    NaverMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
        locationSource = rememberFusedLocationSource(),
        properties = MapProperties(
            extent = Locations.koreaLatLngBounds,
            minZoom = 9.0,
            maxZoom = 21.0,
            isBuildingLayerGroupEnabled = false,
            locationTrackingMode = LocationTrackingMode.Follow
        ),
        uiSettings = MapUiSettings(
            isTiltGesturesEnabled = false,
            isCompassEnabled = false,
            isScaleBarEnabled = false,
            isZoomControlEnabled = false,
            isLocationButtonEnabled = false
        ),
    ) {

        if (cafeLatLng.longitude != 0.0 && cafeLatLng.latitude != 0.0 && cafeName.isNotBlank()) {
            val markerState = rememberMarkerState(position = cafeLatLng)
            Marker(
                icon = OverlayImage.fromResource(cafeCrowded.icon),
                state = markerState,
                captionText = "$cafeName ${cafeFloor.toFloor()}층\n현재 ${cafeCrowded.string}",
                captionAligns = arrayOf(Align.Top, Align.Bottom)
            )
        }
    }

}