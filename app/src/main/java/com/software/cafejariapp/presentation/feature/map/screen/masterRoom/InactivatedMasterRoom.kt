package com.software.cafejariapp.presentation.feature.map.screen.masterRoom

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MyLocation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.naver.maps.geometry.LatLng
import com.software.cafejariapp.core.LocationTrackingNotPermitted
import com.software.cafejariapp.presentation.GlobalState
import com.software.cafejariapp.presentation.component.HorizontalSpacer
import com.software.cafejariapp.presentation.component.PrimaryCtaButton
import com.software.cafejariapp.presentation.component.VerticalSpacer
import com.software.cafejariapp.core.isNearBy
import com.software.cafejariapp.core.toCrowded
import com.software.cafejariapp.core.toFloor
import com.software.cafejariapp.presentation.theme.Gray
import com.software.cafejariapp.presentation.theme.HeavyGray
import com.software.cafejariapp.presentation.theme.MoreLightGray
import com.software.cafejariapp.presentation.theme.White
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun InactivatedMasterRoom(
    globalState: GlobalState,
    onMasterRegisterButtonClick: (Int, Int) -> Unit,
) {

    val context: Context = LocalContext.current
    val scope: CoroutineScope = rememberCoroutineScope()
    val selectedPeriodMinute = rememberSaveable { mutableStateOf(30) }
    val selectedCrowdedIndex = rememberSaveable { mutableStateOf(1) }
    val isLocationLoading = rememberSaveable { mutableStateOf(false) }
    val activityLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { }
    )

    LazyColumn {

        item {

            Column(
                modifier = Modifier.padding(
                    vertical = 40.dp,
                    horizontal = 20.dp
                )
            ) {

                Text(
                    text = "혼잡도 공유할 층 선택",
                    style = MaterialTheme.typography.subtitle2
                )

                VerticalSpacer(height = 12.dp)

                Row {

                    globalState.modalCafeInfo.value.cafes.forEach { cafe ->
                        Button(
                            modifier = Modifier
                                .width(64.dp)
                                .height(32.dp),
                            onClick = {
                                if (globalState.modalCafe.value != cafe) {
                                    globalState.modalCafe.value = cafe
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = if (globalState.modalCafe.value == cafe) {
                                    White
                                } else {
                                    MoreLightGray
                                },
                                disabledBackgroundColor = HeavyGray
                            ),
                            border = if (globalState.modalCafe.value == cafe) {
                                BorderStroke(
                                    width = (1.5).dp,
                                    color = MaterialTheme.colors.primary
                                )
                            } else null,
                            shape = MaterialTheme.shapes.medium,
                            contentPadding = PaddingValues(0.dp),
                            elevation = ButtonDefaults.elevation(
                                defaultElevation = 0.dp,
                                pressedElevation = (-1).dp
                            ),
                            enabled = cafe.master.userId == 0
                        ) {

                            Text(
                                text = "${cafe.floor.toFloor()}층",
                                style = MaterialTheme.typography.body2,
                                color = if (globalState.modalCafe.value == cafe) {
                                    MaterialTheme.colors.primary
                                } else {
                                    HeavyGray
                                }
                            )
                        }

                        HorizontalSpacer(width = 8.dp)
                    }
                }

                VerticalSpacer(height = 40.dp)

                Text(
                    text = "업데이트 주기 선택",
                    style = MaterialTheme.typography.subtitle2
                )

                Text(
                    text = "주기가 짧을수록 받을 수 있는 포인트가 많아져요!",
                    style = MaterialTheme.typography.caption,
                    color = HeavyGray
                )

                VerticalSpacer(height = 12.dp)

                Row {

                    listOf(30, 60).forEach { minute ->
                        Button(
                            modifier = Modifier
                                .width(64.dp)
                                .height(32.dp),
                            onClick = { selectedPeriodMinute.value = minute },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = if (selectedPeriodMinute.value == minute) {
                                    White
                                } else {
                                    MoreLightGray
                                },
                                disabledBackgroundColor = HeavyGray
                            ),
                            border = if (selectedPeriodMinute.value == minute) {
                                BorderStroke(
                                    width = (1.5).dp,
                                    color = MaterialTheme.colors.primary
                                )
                            } else null,
                            shape = MaterialTheme.shapes.medium,
                            contentPadding = PaddingValues(0.dp),
                            elevation = ButtonDefaults.elevation(
                                defaultElevation = 0.dp,
                                pressedElevation = (-1).dp
                            )
                        ) {

                            Text(
                                text = "${minute}분",
                                style = MaterialTheme.typography.body2,
                                color = if (selectedPeriodMinute.value == minute) {
                                    MaterialTheme.colors.primary
                                } else {
                                    HeavyGray
                                }
                            )
                        }

                        HorizontalSpacer(width = 8.dp)
                    }
                }

                VerticalSpacer(height = 40.dp)

                Text(
                    text = "선택한 층의 혼잡도 설정",
                    style = MaterialTheme.typography.subtitle2
                )

                VerticalSpacer(height = 12.dp)

                CrowdedEditor(
                    selectedCrowdedIndex = selectedCrowdedIndex.value,
                    onCrowdedChange = { selectedCrowdedIndex.value = it },
                    isTapAnimationEnable = true
                )
            }
        }

        item {

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
                    .background(color = MaterialTheme.colors.background)
            )
        }

        item {

            Column(
                modifier = Modifier
                    .padding(
                        vertical = 40.dp,
                        horizontal = 20.dp
                    )
            ) {

                Text(
                    text = "현재 위치 확인",
                    style = MaterialTheme.typography.subtitle2
                )

                VerticalSpacer(height = 4.dp)

                when {
                    globalState.userLocation.value == null -> {
                        Text(
                            text = "* 위치 권한을 허용해주세요",
                            style = MaterialTheme.typography.caption,
                            color = Gray
                        )
                    }
                    !globalState.userLocation.value!!.isNearBy(
                        globalState.modalCafeInfo.value.latitude,
                        globalState.modalCafeInfo.value.longitude
                    ) -> {
                        Text(
                            text = "* 현재 위치와 카페위치가 같아야 마스터 등록을 할 수 있어요",
                            style = MaterialTheme.typography.caption,
                            color = Gray
                        )
                    }
                    else -> {
                        Text(
                            text = "* 현재 위치와 카페위치가 동일해요!",
                            style = MaterialTheme.typography.caption,
                            color = Gray
                        )
                    }
                }

                VerticalSpacer(height = 12.dp)

                Box(
                    modifier = Modifier
                        .height(200.dp)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.BottomEnd
                ) {

                    MiniNaverMap(
                        modifier = Modifier.fillMaxSize(),
                        cafeName = globalState.modalCafeInfo.value.name,
                        cafeFloor = globalState.modalCafe.value.floor,
                        cafeLatLng = LatLng(
                            globalState.modalCafeInfo.value.latitude,
                            globalState.modalCafeInfo.value.longitude,
                        ),
                        cafeCrowded = globalState.modalCafe.value.crowded.toCrowded()
                    )

                    Row(
                        modifier = Modifier.padding(8.dp)
                    ) {

                        Button(
                            onClick = {
                                try {
                                    globalState.startLocationTracking(context)
                                    scope.launch {
                                        isLocationLoading.value = true
                                        delay(1500L)
                                        isLocationLoading.value = false
                                    }
                                } catch (e: LocationTrackingNotPermitted) {
                                    globalState.showSnackBar("위치 권한을 허락해주세요. 잠시후 설정으로 이동합니다")
                                    scope.launch {
                                        delay(2000L)
                                        activityLauncher.launch(
                                            Intent(
                                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                                Uri.parse("package:com.software.cafejariapp")
                                            )
                                        )
                                    }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = White
                            ),
                            shape = RoundedCornerShape(50),
                            elevation = ButtonDefaults.elevation(
                                defaultElevation = 1.dp,
                                pressedElevation = 0.dp
                            ),
                            contentPadding = PaddingValues(
                                horizontal = 12.dp,
                                vertical = 4.dp
                            )
                        ) {

                            if (isLocationLoading.value) {
                                CircularProgressIndicator(
                                    strokeWidth = 2.dp,
                                    color = HeavyGray,
                                    modifier = Modifier.size(16.dp)
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Rounded.MyLocation,
                                    contentDescription = "위치아이콘",
                                    tint = HeavyGray,
                                    modifier = Modifier.size(20.dp)
                                )

                                HorizontalSpacer(width = 8.dp)

                                Text(
                                    text = "위치 조정하기",
                                    style = MaterialTheme.typography.button,
                                    color = Gray
                                )
                            }
                        }
                    }
                }

                VerticalSpacer(height = 40.dp)

                PrimaryCtaButton(
                    text = "마스터 등록 완료!",
                    onClick = {
                        when {
                            globalState.userLocation.value == null -> {
                                globalState.showSnackBar("위치 정보가 없습니다. 위치 조정 후 다시 시도해주세요!")
                            }
                            !globalState.userLocation.value!!.isNearBy(
                                globalState.modalCafeInfo.value.latitude,
                                globalState.modalCafeInfo.value.longitude
                            ) && !globalState.user.value.isAdmin -> {
                                globalState.showSnackBar("카페와 거리가 너무 멉니다. 위치 조정 후 다시 시도해주세요")
                            }
                            else -> onMasterRegisterButtonClick(
                                selectedPeriodMinute.value,
                                selectedCrowdedIndex.value
                            )
                        }
                    }
                )

                VerticalSpacer(height = 40.dp)
            }
        }
    }
}