package com.software.cafejariapp

import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.core.view.WindowCompat
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.naver.maps.map.compose.rememberCameraPositionState
import com.software.cafejariapp.core.CustomException
import com.software.cafejariapp.core.LocationTrackingNotPermitted
import com.software.cafejariapp.core.addFocusCleaner
import com.software.cafejariapp.core.isNetworkAvailable
import com.software.cafejariapp.presentation.GlobalState
import com.software.cafejariapp.data.source.remote.HttpRoutes
import com.software.cafejariapp.domain.entity.*
import com.software.cafejariapp.domain.useCase.LoginUseCase
import com.software.cafejariapp.domain.useCase.MainUseCase
import com.software.cafejariapp.domain.useCase.CafeUseCase
import com.software.cafejariapp.presentation.feature.map.util.Locations
import com.software.cafejariapp.domain.entity.AccessToken
import com.software.cafejariapp.domain.entity.RefreshToken
import com.software.cafejariapp.domain.useCase.TokenUseCase
import com.software.cafejariapp.domain.util.DisableDateId
import com.software.cafejariapp.presentation.component.*
import com.software.cafejariapp.presentation.theme.CafeJariAppTheme
import com.software.cafejariapp.presentation.theme.HeavyGray
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@ExperimentalMaterialApi
@ExperimentalPagerApi
@AndroidEntryPoint
@ExperimentalAnimationApi
@ExperimentalPermissionsApi
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var tokenUseCase: TokenUseCase
    @Inject
    lateinit var mainUseCase: MainUseCase
    @Inject
    lateinit var loginUseCase: LoginUseCase
    @Inject
    lateinit var cafeUseCase: CafeUseCase

    lateinit var globalState: GlobalState

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            CafeJariAppTheme {
                Surface {
                    rememberSystemUiController().setStatusBarColor(
                        color = Color.Transparent, darkIcons = true
                    )

                    globalState = GlobalState(
                        user = rememberSaveable { mutableStateOf(User.empty) },
                        userLocation = rememberSaveable { mutableStateOf(null) },

                        cafeInfos = rememberSaveable { mutableStateOf(emptyList()) },
                        isCafeInfoLoading = rememberSaveable { mutableStateOf(false) },
                        isMasterActivated = rememberSaveable { mutableStateOf(false) },
                        masterCafeLog = rememberSaveable { mutableStateOf(CafeLog.empty) },
                        modalCafeInfo = rememberSaveable { mutableStateOf(CafeInfo.empty) },
                        modalCafe = rememberSaveable { mutableStateOf(Cafe.empty) },
                        autoExpiredCafeLog = rememberSaveable { mutableStateOf(AutoExpiredCafeLog.empty) },

                        webViewTitle = rememberSaveable { mutableStateOf("") },
                        webViewUrl = rememberSaveable { mutableStateOf(HttpRoutes.BASE_SERVER_URL) },

                        accessToken = rememberSaveable { mutableStateOf(AccessToken.empty) },
                        refreshToken = rememberSaveable { mutableStateOf(RefreshToken.empty) },
                        isLoggedIn = rememberSaveable { mutableStateOf(false) },

                        initiated = rememberSaveable { mutableStateOf(false) },
                        isBottomBarVisible = rememberSaveable { mutableStateOf(false) },
                        navController = rememberAnimatedNavController(),
                        scaffoldState = rememberScaffoldState(),
                        cameraPositionState = rememberCameraPositionState {
                            this.position = Locations.sinchon.cameraPosition
                        },
                        globalScope = rememberCoroutineScope(),

                        loginUseCase = loginUseCase,
                        tokenUseCase = tokenUseCase,
                        cafeUseCase = cafeUseCase,
                        mainUseCase = mainUseCase
                    )

                    TopLevelScreen(globalState = globalState)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        // 알림 클리어
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()

        if (this::globalState.isInitialized) {
            // 위치 트래킹
            try {
                globalState.startLocationTracking(this)
            } catch (e: LocationTrackingNotPermitted) {
            }

            // 마스터 활동 체크
            if (globalState.isLoggedIn.value) {
                try {
                    globalState.checkMasterActivity()
                    globalState.refreshCafeInfos()
                } catch (throwable: Throwable) {
                }
            }
        }
    }
}


@OptIn(
    ExperimentalPagerApi::class,
    ExperimentalAnimationApi::class,
    ExperimentalPermissionsApi::class,
    ExperimentalMaterialApi::class
)
@Composable
fun TopLevelScreen(
    globalState: GlobalState,

    context: Context = LocalContext.current
) {

    val isMajorUpdateDialogOpened = rememberSaveable { mutableStateOf(false) }
    val isMinorUpdateDialogOpened = rememberSaveable { mutableStateOf(false) }
    val uriHandler = LocalUriHandler.current

    Scaffold(
        scaffoldState = globalState.scaffoldState,
        modifier = Modifier
            .addFocusCleaner(LocalFocusManager.current)
            .navigationBarsPadding(),
        snackbarHost = {
            SnackbarHost(
                modifier = Modifier
                    .zIndex(6f)
                    .padding(vertical = if (globalState.isBottomBarVisible.value) 0.dp else 60.dp),
                hostState = globalState.scaffoldState.snackbarHostState
            ) {
                it.actionLabel
                CustomSnackbar(snackbarData = it)
            }
        },
        bottomBar = {
            BottomNavigationBar(globalState)
        }
    ) {

        // 강제 업뎃
        if (isMajorUpdateDialogOpened.value) {
            CustomAlertDialog(
                onDismiss = { },
                positiveButtonText = "업데이트",
                onPositiveButtonClick = { uriHandler.openUri(HttpRoutes.PLAY_STORE) },
                negativeButtonText = "",
                onNegativeButtonClick = { },
                isNegativeButtonEnabled = false,
                content = {
                    Row {
                        Text(
                            text = "앱 실행에 ",
                            style = MaterialTheme.typography.subtitle1
                        )
                        Text(
                            text = "필수적인 업데이트",
                            style = MaterialTheme.typography.subtitle2
                        )
                        Text(
                            text = "가 있습니다",
                            style = MaterialTheme.typography.subtitle1
                        )
                    }
                    Text(
                        text = "지금 바로 업데이트를 진행해주세요",
                        style = MaterialTheme.typography.subtitle1
                    )
                }
            )
        }

        // 선택 업뎃
        if (isMinorUpdateDialogOpened.value) {
            CustomAlertDialog(
                onDismiss = { isMinorUpdateDialogOpened.value = false },
                positiveButtonText = "지금업데이트",
                onPositiveButtonClick = { uriHandler.openUri(HttpRoutes.PLAY_STORE) },
                negativeButtonText = "오늘하루보지않기",
                onNegativeButtonClick = {
                    globalState.globalScope.launch {
                        globalState.mainUseCase.setTodayDisable(DisableDateId.update)
                    }
                },
                content = {
                    Text(
                        text = "업데이트가 있습니다",
                        style = MaterialTheme.typography.subtitle1
                    )
                    Text(
                        text = "지금 바로 진행하시겠습니까?",
                        style = MaterialTheme.typography.subtitle1
                    )
                    VerticalSpacer(height = 8.dp)
                    Text(
                        text = "* 업데이트하지 않아도 앱 사용가능",
                        style = MaterialTheme.typography.body1,
                        color = HeavyGray
                    )
                }
            )
        }

        // init
        LaunchedEffect(Unit) {
            if (!context.isNetworkAvailable()) {
                globalState.showSnackBar("원활한 앱 사용을 위해 네트워크 연결을 확인해주세요")
            }

            globalState.globalScope.launch {
                val serverVersion = try {
                    globalState.mainUseCase.getVersion()
                } catch (e: CustomException) {
                    Version.empty
                } catch (throwable: Throwable) {
                    Version.empty
                }

                when {
                    serverVersion.release > Version.current.release -> {
                        isMajorUpdateDialogOpened.value = true
                    }
                    serverVersion.release == Version.current.release && serverVersion.major > Version.current.major -> {
                        isMajorUpdateDialogOpened.value = true
                    }
                    serverVersion.release == Version.current.release && serverVersion.major == Version.current.major && serverVersion.minor > Version.current.minor -> {
                        isMinorUpdateDialogOpened.value =
                            globalState.mainUseCase.isTodayExecutable(DisableDateId.update)
                    }
                    else -> {}
                }
            }

            globalState.init()

            try {
                globalState.startLocationTracking(context)
            } catch (e: LocationTrackingNotPermitted) {
            }
        }

        // 스크린
        Navigation(
            globalState = globalState,
            bottomPaddingValue = it.calculateBottomPadding(),
        )
    }
}



// this code for test