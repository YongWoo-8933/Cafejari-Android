package com.software.cafejariapp.presentation.feature.map.screen.mapScreen

import android.graphics.Bitmap
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import com.google.accompanist.pager.ExperimentalPagerApi
import com.software.cafejariapp.core.*
import com.software.cafejariapp.presentation.GlobalState
import com.software.cafejariapp.domain.util.DisableDateId
import com.software.cafejariapp.presentation.feature.map.event.MapEvent
import com.software.cafejariapp.presentation.feature.map.viewModel.MapViewModel
import com.software.cafejariapp.presentation.component.*
import com.software.cafejariapp.presentation.feature.map.event.AdEvent
import com.software.cafejariapp.presentation.feature.map.screen.mapScreen.bottomSheet.MapBottomSheet
import com.software.cafejariapp.presentation.feature.map.screen.mapScreen.mapContent.NaverMapFrame
import com.software.cafejariapp.presentation.feature.map.screen.mapScreen.dialog.*
import com.software.cafejariapp.presentation.feature.map.screen.mapScreen.mapContent.SearchCafeModal
import com.software.cafejariapp.presentation.feature.map.util.PointResultType
import com.software.cafejariapp.presentation.feature.map.viewModel.AdViewModel
import com.software.cafejariapp.presentation.theme.*
import com.software.cafejariapp.presentation.util.BottomSheetShape
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(
    ExperimentalMaterialApi::class,
    ExperimentalPagerApi::class
)
@Composable
fun MapScreen(
    globalState: GlobalState,
    mapViewModel: MapViewModel,
    adViewModel: AdViewModel,
    onNavigateToPointResultScreen: (Int, PointResultType) -> Unit
) {

    val mapState = mapViewModel.state.value
    val adState = adViewModel.state.value
    val context = LocalContext.current
    val scope: CoroutineScope = rememberCoroutineScope()

    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState()
    val thumbsUpRecentLogId = rememberSaveable { mutableStateOf(0) }
    val pickedImage: MutableState<Bitmap?> = rememberSaveable { mutableStateOf(null) }

    val isAppFinishable = rememberSaveable { mutableStateOf(false) }
    val isOnboardingDialogOpened = rememberSaveable { mutableStateOf(false) }
    val isImagePopupExpanded = rememberSaveable { mutableStateOf(false) }
    val isOnSaleCafeDialogOpened = rememberSaveable { mutableStateOf(false) }
    val isAdPointDialogOpened = rememberSaveable { mutableStateOf(false) }
    val isPeekExpanded = rememberSaveable { mutableStateOf(false) }
    val isPeekControlDisabled = rememberSaveable { mutableStateOf(false) }
    val isLocationPermissionDialogOpened = rememberSaveable { mutableStateOf(false) }
    val isMenuOpened = rememberSaveable { mutableStateOf(false) }
    val isAdLoading = rememberSaveable { mutableStateOf(false) }
    val isSearchModalOpened = rememberSaveable { mutableStateOf(false) }

    NetworkChecker(globalState)

    LaunchedEffect(Unit) {
        adViewModel.onEvent(AdEvent.LoadInterstitialAd(context))
        delay(200L)

        mapViewModel.onEvent(MapEvent.MapInit(
            globalState = globalState,
            onOnBoardingDialogOpen = { isOnboardingDialogOpened.value = true }
        ))
    }

    LaunchedEffect(bottomSheetScaffoldState.bottomSheetState.overflow.value != 0f) {
        if (bottomSheetScaffoldState.bottomSheetState.overflow.value != 0f && !isPeekControlDisabled.value) {
            isPeekExpanded.value = false
            bottomSheetScaffoldState.bottomSheetState.collapse()
        }
    }

    LaunchedEffect(isPeekExpanded.value) {
        if (isPeekExpanded.value) {
            isPeekControlDisabled.value = true
            delay(1000L)
            isPeekControlDisabled.value = false
        }
    }

    // 마스터 활동 자동종료 로그 여부 dialog
    if (globalState.autoExpiredCafeLog.value.id != 0) {
        AutoExpiredLogDialog(
            globalState = globalState,
            mapViewModel = mapViewModel,
            adViewModel = adViewModel,
            adLoadingState = isAdLoading
        )
    }

    // 놓친광고 다시보기 결과 dialog
    if (isAdPointDialogOpened.value) {
        CustomAlertDialog(
            onDismiss = { isAdPointDialogOpened.value = false },
            positiveButtonText = "확인",
            onPositiveButtonClick = { },
            negativeButtonText = "",
            isNegativeButtonEnabled = false,
            content = {
                Text(
                    text = "광고를 보고 추가 포인트가\n적립되었습니다.",
                    style = MaterialTheme.typography.subtitle1,
                    textAlign = TextAlign.Center
                )
            }
        )
    }

    // 마스터 추천시 광고시청 권유 dialog
    if (thumbsUpRecentLogId.value != 0) {
        ThumbsUpDialog(
            globalState = globalState,
            mapViewModel = mapViewModel,
            adViewModel = adViewModel,
            adLoadingState = isAdLoading,
            thumbsUpRecentLogId = thumbsUpRecentLogId.value,
            onDismiss = { thumbsUpRecentLogId.value = 0 },
            onNavigateToPointResultScreen = onNavigateToPointResultScreen
        )
    }

    // 위치권한 경고 dialog
    if (isLocationPermissionDialogOpened.value) {
        LocationPermissionDialog(
            onDismiss = { isLocationPermissionDialogOpened.value = false },
            onTodayInvisibleButtonClick = {
                mapViewModel.viewModelScope.launch {
                    mapViewModel.mainUseCase.setTodayDisable(DisableDateId.permission)
                }
            }
        )
    }

    // 팝업 공지 or 광고
    if (mapState.popUpNotificationPagerState.pageCount != 0 && mapState.popUpNotifications.isNotEmpty()) {
        PopUpNotificationDialog(
            globalState = globalState,
            mapViewModel = mapViewModel,
            peekState = isPeekExpanded,
            scope = scope
        )
    }

    // 세일중인 카페 보기
    if (isOnSaleCafeDialogOpened.value) {
        OnSaleCafeDialog(
            globalState = globalState,
            mapViewModel = mapViewModel,
            peekState = isPeekExpanded,
            onDismiss = { isOnSaleCafeDialogOpened.value = false },
            scope = scope
        )
    }

    // 첫 사용자에게 보여주는 가이드(온보딩)
    if (isOnboardingDialogOpened.value) {
        OnboardingDialog(
            onDismiss = {
                isOnboardingDialogOpened.value = false
                mapViewModel.viewModelScope.launch {
                    mapViewModel.mainUseCase.setTodayDisable(DisableDateId.onBoarding)
                }
            }
        )
    }

    BackHandler(
        enabled = true,
        onBack = {
            when {
                globalState.initiated.value && !globalState.isLoggedIn.value -> {

                }
                globalState.autoExpiredCafeLog.value.id != 0 -> {
                    mapViewModel.onEvent(MapEvent.DeleteAutoExpiredCafeLog(globalState))
                }
                mapState.popUpNotificationPagerState.pageCount != 0 && mapState.popUpNotifications.isNotEmpty() -> {
                    mapViewModel.onEvent(MapEvent.ClearPopUpNotifications)
                    mapViewModel.onEvent(MapEvent.SetPopUpViewed)
                }
                thumbsUpRecentLogId.value != 0 -> {
                    thumbsUpRecentLogId.value = 0
                }
                isImagePopupExpanded.value -> {
                    isImagePopupExpanded.value = false
                }
                isOnSaleCafeDialogOpened.value -> {
                    isOnSaleCafeDialogOpened.value = false
                }
                isAdPointDialogOpened.value -> {
                    isAdPointDialogOpened.value = false
                }
                isLocationPermissionDialogOpened.value -> {
                    isLocationPermissionDialogOpened.value = false
                }
                bottomSheetScaffoldState.bottomSheetState.isExpanded -> {
                    scope.launch {
                        bottomSheetScaffoldState.bottomSheetState.collapse()
                    }
                }
                isSearchModalOpened.value -> {
                    isSearchModalOpened.value = false
                }
                isPeekExpanded.value -> {
                    isPeekExpanded.value = false
                }
                isMenuOpened.value -> {
                    isMenuOpened.value = false
                }
                !isAppFinishable.value -> {
                    globalState.showSnackBar("뒤로가기를 한번더 하시면 앱이 종료됩니다")
                    isAppFinishable.value = true
                    scope.launch {
                        delay(3000L)
                        isAppFinishable.value = false
                    }
                }
                else -> context.findActivity().finishAffinity()
            }
        }
    )

    BottomSheetScaffold(
        modifier = Modifier.fillMaxSize(),
        scaffoldState = bottomSheetScaffoldState,
        sheetShape = BottomSheetShape(),
        sheetPeekHeight = animateDpAsState(
            targetValue = if (isPeekExpanded.value) 270.dp else 0.dp
        ).value,
        sheetElevation = 5.dp,
        sheetGesturesEnabled = bottomSheetScaffoldState.bottomSheetState.overflow.value == 0f,
        sheetContent = {

            MapBottomSheet(
                globalState = globalState,
                mapViewModel = mapViewModel,
                thumbsUpRecentLogIdState = thumbsUpRecentLogId,
                onCollapseBottomSheet = {
                    scope.launch {
                        bottomSheetScaffoldState.bottomSheetState.collapse()
                        isPeekExpanded.value = false
                    }
                },
                onImageClick = { bitmap ->
                    pickedImage.value = bitmap
                    isImagePopupExpanded.value = true
                }
            )
        }
    ) {

        NaverMapFrame(
            globalState = globalState,
            mapViewModel = mapViewModel,

            menuState = isMenuOpened,
            permissionDialogState = isLocationPermissionDialogOpened,
            onSaleCafeDialogState = isOnSaleCafeDialogOpened,
            peekState = isPeekExpanded,
            searchModalState = isSearchModalOpened,
            bottomSheetScaffoldState = bottomSheetScaffoldState,
        )
    }

    if (isAdLoading.value) {
        FullSizeLoadingScreen(loadingText = "광고 로드중..")
    }

    if (adState.remainSecondsBeforeAdPopUp != 4) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = 16.dp,
                    vertical = 160.dp
                ),
            contentAlignment = Alignment.TopStart,
        ) {

            Text(
                text = "${adState.remainSecondsBeforeAdPopUp}초 후 광고가 표시됩니다",
                style = MaterialTheme.typography.subtitle1,
                color = White,
                modifier = Modifier
                    .background(
                        color = HalfTransparentBlack,
                        shape = MaterialTheme.shapes.medium
                    )
                    .padding(
                        horizontal = 12.dp,
                        vertical = 8.dp
                    )
            )
        }
    }

    AnimatedVisibility(
        visible = isSearchModalOpened.value,
        modifier = Modifier.fillMaxSize(),
        enter = expandIn(
            initialSize = {
                IntSize(
                    width = (it.width * 0.6).toInt(),
                    height = (it.height * 0.6).toInt()
                )
        }) + fadeIn(),
        exit = shrinkOut(
            targetSize = {
                IntSize(
                    width = (it.width * 0.3).toInt(),
                    height = (it.height * 0.3).toInt()
                )
        }) + fadeOut(),
    ) {

        SearchCafeModal(
            globalState = globalState,
            mapViewModel = mapViewModel,
            scope = scope,
            peekState = isPeekExpanded,
            onDismiss = { isSearchModalOpened.value = false },
        )
    }
}