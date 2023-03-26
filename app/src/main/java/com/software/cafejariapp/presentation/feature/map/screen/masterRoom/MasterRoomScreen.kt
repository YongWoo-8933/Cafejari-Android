package com.software.cafejariapp.presentation.feature.map.screen.masterRoom

import android.Manifest
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.software.cafejariapp.core.LocationTrackingNotPermitted
import com.software.cafejariapp.presentation.GlobalState
import com.software.cafejariapp.presentation.component.*
import com.software.cafejariapp.presentation.feature.map.event.AdEvent
import com.software.cafejariapp.presentation.feature.map.event.MasterRoomEvent
import com.software.cafejariapp.presentation.feature.map.viewModel.MasterRoomViewModel
import com.software.cafejariapp.presentation.feature.map.util.PointResultType
import com.software.cafejariapp.presentation.feature.map.viewModel.AdViewModel
import com.software.cafejariapp.presentation.theme.HeavyGray
import com.software.cafejariapp.presentation.theme.White
import com.software.cafejariapp.presentation.util.Screen
import com.software.cafejariapp.presentation.util.TransitionAnimations
import kotlinx.coroutines.delay

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MasterRoomScreen(
    globalState: GlobalState,
    masterRoomViewModel: MasterRoomViewModel,
    adViewModel: AdViewModel,
    onNavigateToPointResultScreen: (Int, PointResultType) -> Unit
) {

    val context = LocalContext.current
    val selectedDetailLogId: MutableState<Int?> = rememberSaveable { mutableStateOf(null) }
    val isExpireDialogOpened = rememberSaveable { mutableStateOf(false) }
    val isAdLoading = rememberSaveable { mutableStateOf(false) }
    val locationPermissionsState = rememberMultiplePermissionsState(
        permissions = listOf (
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
        ),
        onPermissionsResult = {
            if(it[Manifest.permission.ACCESS_COARSE_LOCATION] == true && it[Manifest.permission.ACCESS_FINE_LOCATION] == true) {
                globalState.startLocationTracking(context)
            } else {
                globalState.showSnackBar("위치 권한을 허용하지 않으면 마스터 활동이 불가합니다")
            }
        }
    )

    NetworkChecker(globalState)

    LaunchedEffect(Unit) {
        globalState.checkMasterActivity()
        delay(200L)

        try {
            globalState.startLocationTracking(context)
        } catch (e: LocationTrackingNotPermitted) {
            locationPermissionsState.launchMultiplePermissionRequest()
        }
    }

    if (isExpireDialogOpened.value) {
        CustomAlertDialog(
            onDismiss = { isExpireDialogOpened.value = false },
            onPositiveButtonClick = {
                adViewModel.onEvent(AdEvent.ShowRewardedAd(
                    globalState = globalState,
                    context = context,
                    loadingState = isAdLoading,
                    onSuccess = {
                        masterRoomViewModel.onEvent( MasterRoomEvent.ExpireMaster(
                            globalState,
                            true,
                            onNavigateToPointResultScreen
                        ))
                    }
                ))
            },
            positiveButtonText = "광고보고 종료",
            negativeButtonText = "그냥종료",
            onNegativeButtonClick = {
                masterRoomViewModel.onEvent(MasterRoomEvent.ExpireMaster(
                    globalState,
                    false,
                    onNavigateToPointResultScreen
                ))
            },
            content = {
                Text(
                    text = "혼잡도 공유활동을 종료하시겠어요?",
                    style = MaterialTheme.typography.subtitle1
                )

                Text(
                    text = "광고 시청시 포인트가 1.5배!",
                    style = MaterialTheme.typography.subtitle2,
                    color = MaterialTheme.colors.primary
                )

                VerticalSpacer(height = 4.dp)

                Text(
                    text = "* 커피 기프티콘 구매 가능",
                    color = HeavyGray
                )
            }
        )
    }

    if (selectedDetailLogId.value != null) {
        CustomAlertDialog(
            onDismiss = { selectedDetailLogId.value = null },
            onPositiveButtonClick = {
                masterRoomViewModel.onEvent(
                    MasterRoomEvent.DeleteCafeDetailLog(
                        globalState,
                        selectedDetailLogId.value!!
                    )
                )
            },
            positiveButtonText = "삭제",
            negativeButtonText = "아니오",
            onNegativeButtonClick = {  },
            content = {
                Text(
                    text = "해당 로그를 삭제하시겠습니까?",
                    style = MaterialTheme.typography.subtitle1
                )
            }
        )
    }

    BackHandler(
        enabled = true,
        onBack = {
            when {
                isExpireDialogOpened.value -> isExpireDialogOpened.value = false
                selectedDetailLogId.value != null -> selectedDetailLogId.value = null
                else -> globalState.navController.popBackStack()
            }
        }
    )

    if (isAdLoading.value) {
        FullSizeLoadingScreen(loadingText = "광고 로드중..")
    }

    Scaffold(
        topBar = {
            BaseTopAppBar(
                isBackButtonEnable = true,
                onBackButtonClick = {
                    globalState.navController.navigate(Screen.MapScreen.route) {
                        popUpTo(Screen.MapScreen.route) { inclusive = true }
                    }
                },
                title = if (globalState.isMasterActivated.value) {
                    globalState.masterCafeLog.value.name
                } else {
                    globalState.modalCafeInfo.value.name
                },
                backgroundColor = MaterialTheme.colors.background,
                titleColor = MaterialTheme.colors.primary
            )
        },
        backgroundColor = White
    ) { paddingValue ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValue.calculateTopPadding())
        ) {

            // 마스터 활동 아닐때
            AnimatedVisibility(
                visible = !globalState.isMasterActivated.value,
                enter = TransitionAnimations.fadeInAnimation,
                exit = TransitionAnimations.fadeOutAnimation
            ) {

                InactivatedMasterRoom(
                    globalState = globalState,
                    onMasterRegisterButtonClick = { periodMinute, crowdedInt ->
                        masterRoomViewModel.onEvent(MasterRoomEvent.RegisterMaster(
                            globalState,
                            periodMinute,
                            crowdedInt
                        ))
                    }
                )
            }

            // 마스터 활동 중일때
            AnimatedVisibility(
                visible = globalState.isMasterActivated.value,
                enter = TransitionAnimations.fadeInAnimation,
                exit = TransitionAnimations.fadeOutAnimation
            ) {

                ActivatedMasterRoom(
                    globalState = globalState,
                    onCrowdedHistoryClick = { selectedDetailLogId.value = it },
                    onCrowdedUpdateButtonClick = { masterRoomViewModel.onEvent(
                        MasterRoomEvent.UpdateCrowded(
                            globalState,
                            it
                        )
                    ) },
                    onExpireMasterTextClick = { isExpireDialogOpened.value = true }
                )
            }
        }
    }
}




