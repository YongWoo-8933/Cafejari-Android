package com.software.cafejariapp.presentation.feature.map.screen.mapScreen.dialog

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.NearMe
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
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.skydoves.landscapist.glide.GlideImage
import com.software.cafejariapp.R
import com.software.cafejariapp.domain.entity.OnSaleCafe
import com.software.cafejariapp.presentation.GlobalState
import com.software.cafejariapp.presentation.component.BaseDivider
import com.software.cafejariapp.presentation.component.CustomButton
import com.software.cafejariapp.presentation.component.HorizontalSpacer
import com.software.cafejariapp.presentation.component.VerticalSpacer
import com.software.cafejariapp.presentation.feature.map.event.MapEvent
import com.software.cafejariapp.presentation.feature.map.viewModel.MapViewModel
import com.software.cafejariapp.presentation.theme.Gray
import com.software.cafejariapp.presentation.theme.White
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(
    ExperimentalComposeUiApi::class,
    ExperimentalNaverMapApi::class
)
@Composable
fun OnSaleCafeDialog(
    globalState: GlobalState,
    mapViewModel: MapViewModel,
    peekState: MutableState<Boolean>,
    onDismiss: () -> Unit,
    scope: CoroutineScope
) {

    val mapState = mapViewModel.state.value

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false,
        ),
    ) {

        Card(
            modifier = Modifier.width(320.dp),
            backgroundColor = White,
            shape = MaterialTheme.shapes.large
        ) {

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                VerticalSpacer(height = 12.dp)

                if (mapState.onSaleCafes.isEmpty()) {
                    VerticalSpacer(height = 42.dp)

                    Text(
                        text = "할인 이벤트중인 카페가 없어요",
                        style = MaterialTheme.typography.subtitle2,
                        color = MaterialTheme.colors.primary
                    )

                    VerticalSpacer(height = 36.dp)

                    Image(
                        modifier = Modifier.height(120.dp),
                        painter = painterResource(id = R.drawable.empty),
                        contentDescription = "비어있음",
                        contentScale = ContentScale.FillHeight
                    )

                    VerticalSpacer(height = 72.dp)
                } else {
                    LazyColumn(
                        modifier = if (mapState.onSaleCafes.count() > 2) {
                            Modifier.fillMaxHeight(0.75f)
                        } else {
                            Modifier
                        }
                    ) {

                        items(mapState.onSaleCafes) { onSaleCafe ->
                            Column(
                                modifier = Modifier
                                    .clickable {
                                        scope.launch {
                                            globalState.cameraPositionState.animate(
                                                CameraUpdate.scrollTo(
                                                    LatLng(
                                                        onSaleCafe.latitude,
                                                        onSaleCafe.longitude
                                                    )
                                                ), durationMs = 1500
                                            )
                                        }
                                        globalState.refreshCafeInfos(
                                            latLng = LatLng(onSaleCafe.latitude, onSaleCafe.longitude),
                                            onSuccess = { cafeInfos ->
                                                val selectedCafeInfo = cafeInfos.find { cafeInfo ->
                                                    cafeInfo.id == onSaleCafe.cafeInfoId
                                                }
                                                if (selectedCafeInfo != null) {
                                                    globalState.modalCafeInfo.value = selectedCafeInfo
                                                    if (selectedCafeInfo.cafes.isNotEmpty()) {
                                                        globalState.modalCafe.value = selectedCafeInfo.cafes[0]
                                                    }
                                                    mapViewModel.onEvent(MapEvent.ClearModalPlaceInfo)
                                                    mapViewModel.onEvent(MapEvent.FetchModalCafePlaceInfo(selectedCafeInfo))
                                                    peekState.value = true
                                                }
                                            }
                                        )
                                        onDismiss()
                                    }
                                    .fillMaxWidth()
                                    .padding(20.dp)
                            ) {

                                GlideImage(
                                    modifier = Modifier.fillMaxWidth(),
                                    imageModel = onSaleCafe.image,
                                    contentScale = ContentScale.FillWidth,
                                    placeHolder = painterResource(id = R.drawable.glide_image_placeholder)
                                )

                                VerticalSpacer(height = 12.dp)

                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    Text(
                                        text = onSaleCafe.name,
                                        style = MaterialTheme.typography.subtitle2
                                    )

                                    HorizontalSpacer(width = 6.dp)

                                    Row(
                                        modifier = Modifier
                                            .background(
                                                color = MaterialTheme.colors.secondary,
                                                shape = MaterialTheme.shapes.small
                                            )
                                            .padding(horizontal = 4.dp, vertical = 2.dp)
                                    ) {

                                        if(globalState.userLocation.value != null) {
                                            val distanceString = if(onSaleCafe.order < 1000) {
                                                "${onSaleCafe.order}m"
                                            } else {
                                                "${(onSaleCafe.order / 1000)}km+"
                                            }

                                            Icon(
                                                imageVector = Icons.Rounded.NearMe,
                                                contentDescription = "거리표시아이콘",
                                                tint = White,
                                                modifier = Modifier.size(14.dp)
                                            )

                                            HorizontalSpacer(width = 2.dp)

                                            Text(
                                                text = distanceString,
                                                style = MaterialTheme.typography.overline,
                                                color = White
                                            )
                                        } else {
                                            Text(
                                                text = "이벤트",
                                                style = MaterialTheme.typography.overline,
                                                color = White
                                            )
                                        }
                                    }
                                }

                                VerticalSpacer(height = 4.dp)

                                Text(
                                    text = "${onSaleCafe.city} ${onSaleCafe.gu} ${onSaleCafe.address}",
                                    color = Gray,
                                    style = MaterialTheme.typography.caption
                                )
                            }

                            BaseDivider()
                        }
                    }
                }

                CustomButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    text = "확인",
                    textColor = White,
                    textStyle = MaterialTheme.typography.subtitle2,
                    shape = RoundedCornerShape(0.dp),
                    onClick = onDismiss,
                    backgroundColor = MaterialTheme.colors.primary
                )
            }
        }
    }
}