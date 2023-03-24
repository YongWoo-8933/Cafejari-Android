package com.software.cafejariapp.presentation.feature.login.screen

import android.Manifest
import android.os.Build
import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.rounded.ExpandLess
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material.icons.rounded.FolderOpen
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.software.cafejariapp.core.LocationTrackingNotPermitted
import com.software.cafejariapp.presentation.GlobalState
import com.software.cafejariapp.presentation.component.*
import com.software.cafejariapp.presentation.feature.login.component.BaseColumn
import com.software.cafejariapp.presentation.util.Screen
import com.software.cafejariapp.presentation.theme.HeavyGray

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionScreen(
    globalState: GlobalState
) {

    val context = LocalContext.current
    val isLocationDescriptionExpanded = rememberSaveable { mutableStateOf(false) }
    val isStorageDescriptionExpanded = rememberSaveable { mutableStateOf(false) }
    val isPushDescriptionExpanded = rememberSaveable { mutableStateOf(false) }
    val permissionsState = rememberMultiplePermissionsState(
        permissions = if (Build.VERSION.SDK_INT >= 33) {
            listOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.POST_NOTIFICATIONS
            )
        } else {
            listOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        },
        onPermissionsResult = {
            if (it[Manifest.permission.ACCESS_COARSE_LOCATION] == true && it[Manifest.permission.ACCESS_FINE_LOCATION] == true) {
                try {
                    globalState.startLocationTracking(context)
                } catch (e: LocationTrackingNotPermitted) {
                }
            }
            globalState.navController.navigate(Screen.MapScreen.route) {
                popUpTo(Screen.PermissionScreen.route) { inclusive = true }
            }
            globalState.showSnackBar("권한 설정이 완료되었습니다. 카페자리에 오신것을 환영합니다!")
        }
    )

    BaseColumn(
        modifier = Modifier.fillMaxSize()
    ) {

        LazyColumn {

            item {

                Text(
                    text = "앱 권한",
                    style = MaterialTheme.typography.h5
                )

                VerticalSpacer(height = 20.dp)

                Text(
                    text = "* 모든 권한을 허용하는 것을 권장합니다",
                    color = MaterialTheme.colors.secondary
                )

                VerticalSpacer(height = 40.dp)
            }

            item {

                Text(
                    modifier = Modifier.padding(8.dp),
                    text = "권고",
                    style = MaterialTheme.typography.subtitle2,
                    color = MaterialTheme.colors.primary
                )

                VerticalSpacer(height = 8.dp)

                BaseDivider()

                VerticalSpacer(height = 8.dp)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable {
                            isStorageDescriptionExpanded.value = false
                            isPushDescriptionExpanded.value = false
                            isLocationDescriptionExpanded.value = !isLocationDescriptionExpanded.value
                        },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(
                            imageVector = Icons.Outlined.LocationOn,
                            contentDescription = "위치아이콘",
                            tint = HeavyGray,
                            modifier = Modifier.size(24.dp)
                        )

                        HorizontalSpacer(width = 20.dp)

                        Text(
                            text = "앱 사용중 기기 위치 탐색",
                            style = MaterialTheme.typography.subtitle1,
                            color = HeavyGray
                        )
                    }

                    Icon(
                        imageVector = if (isLocationDescriptionExpanded.value) {
                            Icons.Rounded.ExpandLess
                        } else {
                            Icons.Rounded.ExpandMore
                        },
                        contentDescription = "확장아이콘",
                        tint = HeavyGray,
                        modifier = Modifier.size(20.dp)
                    )
                }

                AnimatedVisibility(
                    visible = isLocationDescriptionExpanded.value,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {

                    Text(
                        text = "혼잡도 정보를 공유할 때, 사용자가 해당 카페에 위치하는지 확인하는데 주로 사용됩니다. 또한, 지도에서 내 위치를 표시하려면 권한이 반드시 필요합니다. 따라서 허용하지 않으실 경우, 혼잡도 공유는 물론 지도에서 내 위치도 확인하실 수 없습니다.",
                        color = HeavyGray,
                        modifier = Modifier.padding(12.dp)
                    )
                }

                VerticalSpacer(height = 20.dp)
            }

            item {

                Text(
                    modifier = Modifier.padding(8.dp),
                    text = "추천",
                    style = MaterialTheme.typography.subtitle2,
                    color = MaterialTheme.colors.primary
                )

                VerticalSpacer(height = 8.dp)

                BaseDivider()

                VerticalSpacer(height = 8.dp)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable {
                            isLocationDescriptionExpanded.value = false
                            isStorageDescriptionExpanded.value = false
                            isPushDescriptionExpanded.value = !isPushDescriptionExpanded.value
                        },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Notifications,
                            contentDescription = "알림 아이콘",
                            tint = HeavyGray,
                            modifier = Modifier.size(24.dp)
                        )

                        HorizontalSpacer(width = 20.dp)

                        Text(
                            text = "푸시알림",
                            style = MaterialTheme.typography.subtitle1,
                            color = HeavyGray
                        )
                    }

                    Icon(
                        imageVector = if (isPushDescriptionExpanded.value) {
                            Icons.Rounded.ExpandLess
                        } else {
                            Icons.Rounded.ExpandMore
                        },
                        contentDescription = "확장아이콘",
                        tint = HeavyGray,
                        modifier = Modifier.size(20.dp)
                    )
                }

                AnimatedVisibility(
                    visible = isPushDescriptionExpanded.value,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {

                    Text(
                        text = "해당 권한은 알림을 통해 각종 정보를 볼수 있게 합니다. 허용하지 않으실 경우, 혼잡도 공유 활동과 각종 문의에 대한 답변 알림을 받을 수 없습니다.",
                        color = HeavyGray,
                        modifier = Modifier.padding(12.dp)
                    )
                }

                VerticalSpacer(height = 12.dp)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable {
                            isLocationDescriptionExpanded.value = false
                            isPushDescriptionExpanded.value = false
                            isStorageDescriptionExpanded.value = !isStorageDescriptionExpanded.value
                        },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(
                            imageVector = Icons.Rounded.FolderOpen,
                            contentDescription = "저장소아이콘",
                            tint = HeavyGray,
                            modifier = Modifier.size(24.dp)
                        )

                        HorizontalSpacer(width = 20.dp)

                        Text(
                            text = "저장소(이미지) 접근",
                            style = MaterialTheme.typography.subtitle1,
                            color = HeavyGray
                        )
                    }

                    Icon(
                        imageVector = if (isStorageDescriptionExpanded.value) {
                            Icons.Rounded.ExpandLess
                        } else {
                            Icons.Rounded.ExpandMore
                        },
                        contentDescription = "확장아이콘",
                        tint = HeavyGray,
                        modifier = Modifier.size(20.dp)
                    )
                }

                AnimatedVisibility(
                    visible = isStorageDescriptionExpanded.value,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {

                    Text(
                        text = "해당 권한은 사용자의 프로필 사진 변경과 추후 업데이트될 카페사진 공유등을 위해 사진을 업로드할때 필요한 권한입니다. 허용하지 않으실 경우, 프로필 사진 변경이 불가합니다.",
                        color = HeavyGray,
                        modifier = Modifier.padding(12.dp)
                    )
                }

                VerticalSpacer(height = 60.dp)

                PrimaryCtaButton(
                    text = "다음",
                    onClick = { permissionsState.launchMultiplePermissionRequest() }
                )
            }
        }
    }
}


