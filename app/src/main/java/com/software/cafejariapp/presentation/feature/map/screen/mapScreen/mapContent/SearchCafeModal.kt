package com.software.cafejariapp.presentation.feature.map.screen.mapScreen.mapContent

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.NavigateBefore
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.software.cafejariapp.presentation.GlobalState
import com.software.cafejariapp.presentation.util.Screen
import com.software.cafejariapp.presentation.feature.map.event.MapEvent
import com.software.cafejariapp.presentation.feature.map.state.MapState
import com.software.cafejariapp.presentation.feature.map.viewModel.MapViewModel
import com.software.cafejariapp.presentation.feature.map.util.Zoom
import com.software.cafejariapp.presentation.component.BaseDivider
import com.software.cafejariapp.presentation.component.VerticalSpacer
import com.software.cafejariapp.presentation.theme.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun SearchCafeModal(
    globalState: GlobalState,
    mapViewModel: MapViewModel,
    scope: CoroutineScope,
    peekState: MutableState<Boolean>,
    onDismiss: () -> Unit,
) {

    val mapState: MapState = mapViewModel.state.value
    val searchQuery = rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = White)
            .statusBarsPadding()
    ) {

        VerticalSpacer(height = 24.dp)

        OutlinedTextField(
            value = searchQuery.value,
            onValueChange = {
                searchQuery.value = it
                when (it.length) {
                    0 -> {
                        mapViewModel.onEvent(MapEvent.ClearSearchCafes)
                    }
                    1 -> {

                    }
                    else -> {
                        mapViewModel.onEvent(
                            MapEvent.SearchCafe(
                                globalState = globalState,
                                query = searchQuery.value
                            )
                        )
                    }
                }
            },
            textStyle = MaterialTheme.typography.body1,
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .padding(horizontal = 16.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions {

            },
            leadingIcon = {
                IconButton(
                    onClick = onDismiss
                ) {
                    Icon(
                        Icons.Rounded.NavigateBefore,
                        "뒤로가기",
                        tint = TextBlack,
                        modifier = Modifier.size(24.dp)
                    )
                }
            },
            trailingIcon = {
                if (searchQuery.value.isNotBlank()) {
                    IconButton(
                        onClick = {
                            searchQuery.value = ""
                            mapViewModel.onEvent(MapEvent.ClearSearchCafes)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Cancel,
                            contentDescription = "싹 지우기",
                            tint = HeavyGray
                        )
                    }
                }
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = TextBlack,
                backgroundColor = MoreLightGray,
                focusedBorderColor = MoreLightGray,
                unfocusedBorderColor = MoreLightGray,
                cursorColor = TextBlack,
                trailingIconColor = HeavyGray
            ),
            shape = MaterialTheme.shapes.medium,
            placeholder = {
                Text(
                    text = "카페명을 정확히 입력해주세요",
                    style = MaterialTheme.typography.caption,
                    color = Gray
                )
            }
        )

        VerticalSpacer(height = 20.dp)

        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {

            mapState.searchCafeInfos.forEach { cafeInfo ->
                item {
                    Button(
                        shape = RoundedCornerShape(0.dp),
                        onClick = {
                            onDismiss()
                            scope.launch {
                                globalState.cameraPositionState.animate(
                                    CameraUpdate.scrollAndZoomTo(
                                        LatLng(cafeInfo.latitude, cafeInfo.longitude), Zoom.LARGE
                                    ), durationMs = 1500
                                )
                            }
                            globalState.refreshCafeInfos(
                                latLng = LatLng(cafeInfo.latitude, cafeInfo.longitude),
                                onSuccess = { cafeInfos ->
                                    val selectedCafeInfo = cafeInfos.find {
                                        it.id == cafeInfo.id
                                    }
                                    if (selectedCafeInfo != null) {
                                        globalState.modalCafeInfo.value = selectedCafeInfo
                                        if (selectedCafeInfo.cafes.isNotEmpty()) {
                                            globalState.modalCafe.value = selectedCafeInfo.cafes[0]
                                        }
                                        mapViewModel.onEvent(MapEvent.ClearModalPlaceInfo)
                                        mapViewModel.onEvent(
                                            MapEvent.FetchModalCafePlaceInfo(selectedCafeInfo)
                                        )
                                        peekState.value = true
                                    }
                                }
                            )
                        },
                        elevation = ButtonDefaults.elevation(
                            defaultElevation = 0.dp,
                            pressedElevation = (-1).dp
                        ),
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = White
                        )
                    ) {

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    vertical = 12.dp,
                                    horizontal = 16.dp
                                )
                        ) {

                            Text(
                                text = cafeInfo.name,
                                style = MaterialTheme.typography.body2
                            )

                            VerticalSpacer(height = 4.dp)

                            Text(
                                text = "${cafeInfo.city} ${cafeInfo.gu} ${cafeInfo.address}",
                                style = MaterialTheme.typography.caption,
                                color = Gray
                            )
                        }
                    }

                    BaseDivider()
                }
            }

            if (searchQuery.value.isNotBlank()) {
                item {

                    Text(
                        text = "원하는 카페가 없다면, 바로 추가해보세요!",
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.primary,
                        textDecoration = TextDecoration.Underline,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(36.dp)
                            .clickable {
                                onDismiss()
                                globalState.navController.navigate(Screen.RegisterCafeScreen.route)
                            },
                    )
                }
            }

            item {

                VerticalSpacer(height = 200.dp)
            }
        }
    }
}