package com.software.cafejariapp.presentation

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarResult
import androidx.compose.runtime.MutableState
import androidx.core.app.ActivityCompat
import androidx.navigation.NavHostController
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.compose.CameraPositionState
import com.software.cafejariapp.core.CustomException
import com.software.cafejariapp.core.LocationTrackingNotPermitted
import com.software.cafejariapp.core.RefreshTokenExpiredException
import com.software.cafejariapp.core.TokenExpiredException
import com.software.cafejariapp.presentation.util.Screen
import com.software.cafejariapp.presentation.util.Time
import com.software.cafejariapp.domain.entity.User
import com.software.cafejariapp.domain.useCase.LoginUseCase
import com.software.cafejariapp.domain.entity.CafeInfo
import com.software.cafejariapp.domain.entity.CafeLog
import com.software.cafejariapp.domain.useCase.CafeUseCase
import com.software.cafejariapp.presentation.feature.map.util.Zoom
import com.software.cafejariapp.domain.entity.AutoExpiredCafeLog
import com.software.cafejariapp.domain.entity.Cafe
import com.software.cafejariapp.domain.entity.AccessToken
import com.software.cafejariapp.domain.entity.RefreshToken
import com.software.cafejariapp.domain.useCase.MainUseCase
import com.software.cafejariapp.domain.useCase.TokenUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GlobalState(
    val user: MutableState<User>,
    val userLocation: MutableState<Location?>,

    val cafeInfos: MutableState<List<CafeInfo>>,
    val isCafeInfoLoading: MutableState<Boolean>,
    val isMasterActivated: MutableState<Boolean>,
    val masterCafeLog: MutableState<CafeLog>,
    val modalCafeInfo: MutableState<CafeInfo>,
    val modalCafe: MutableState<Cafe>,
    val autoExpiredCafeLog: MutableState<AutoExpiredCafeLog>,

    val webViewTitle: MutableState<String>,
    val webViewUrl: MutableState<String>,

    val accessToken: MutableState<AccessToken>,
    val refreshToken: MutableState<RefreshToken>,
    val isLoggedIn: MutableState<Boolean>,

    val initiated: MutableState<Boolean>,
    val isBottomBarVisible: MutableState<Boolean>,
    val navController: NavHostController,
    val scaffoldState: ScaffoldState,
    var cameraPositionState: CameraPositionState,
    val globalScope: CoroutineScope,

    private val loginUseCase: LoginUseCase,
    private val tokenUseCase: TokenUseCase,
    private val cafeUseCase: CafeUseCase,
    val mainUseCase: MainUseCase,
) {

    val EMPTY = "_none"

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            if (p0.lastLocation != null) {
                if (userLocation.value == null) {
                    cameraPositionState = CameraPositionState(
                        position = CameraPosition(
                            LatLng(
                                p0.lastLocation!!.latitude,
                                p0.lastLocation!!.longitude
                            ), Zoom.MEDIUM
                        )
                    )
                    refreshCafeInfos(LatLng(
                        p0.lastLocation!!.latitude,
                        p0.lastLocation!!.longitude
                    ))
                }
                userLocation.value = p0.lastLocation
            }
        }
    }

    fun clearGlobalState() {
        user.value = User.empty
        userLocation.value = null

        cafeInfos.value = emptyList()
        isCafeInfoLoading.value = false
        isMasterActivated.value = false
        masterCafeLog.value = CafeLog.empty
        modalCafeInfo.value = CafeInfo.empty
        modalCafe.value = Cafe.empty
        autoExpiredCafeLog.value = AutoExpiredCafeLog.empty

        webViewTitle.value = ""
        webViewUrl.value = "https://google.com"

        accessToken.value = AccessToken.empty
        refreshToken.value = RefreshToken.empty
        isLoggedIn.value = false
    }

    fun init() {
        globalScope.launch {
            try {
                refreshToken.value = tokenUseCase.getSavedRefreshToken()

                if (refreshToken.value.value.isNotBlank()) {
                    accessToken.value = tokenUseCase.getAccessToken(refreshToken.value)
                    isLoggedIn.value = true

                    user.value = loginUseCase.getUser(accessToken.value)
                    delay(200L)

                    updateUserFcmToken()
                    delay(200L)

                    checkMasterActivity()
                }
            } catch (e: TokenExpiredException) {
                showSnackBar("저장된 로그인 정보의 유효기간이 만료되었습니다. 다시 로그인해주세요")
                tokenUseCase.updateSavedRefreshToken(RefreshToken.empty)
            } catch (e: CustomException) {
                showSnackBar(e.message.toString())
            } catch (throwable: Throwable) {

            } finally {
                initiated.value = true
            }
        }
    }

    fun startLocationTracking(context: Context) {
        if (ActivityCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val fusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(context)
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
            val locationRequest = LocationRequest()
            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            locationRequest.interval = 4000
            locationRequest.fastestInterval = 5000
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.myLooper()
            )
        } else {
            throw LocationTrackingNotPermitted()
        }
    }

    suspend fun refreshAccessToken(afterAction: suspend () -> Unit) {
        try {
            accessToken.value = tokenUseCase.getAccessToken(refreshToken.value)
            delay(200L)
            afterAction()
        } catch (e: TokenExpiredException) {
            showSnackBar("저장된 로그인 정보의 유효기간이 만료되었습니다. 다시 로그인해주세요")
            tokenUseCase.updateSavedRefreshToken(RefreshToken.empty)
            navController.navigate(Screen.CheckLoginScreen.route) {
                popUpTo(Screen.MapScreen.route) { inclusive = true }
            }
            clearGlobalState()
            throw RefreshTokenExpiredException()
        } catch (e: CustomException) {
            showSnackBar(e.message.toString())
        }
    }

    fun showSnackBar(
        message: String, actionLabelText: String = "닫기", onActionLabelClicked: () -> Unit = {
            scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
        }
    ) {
        globalScope.launch {
            scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
            val result = scaffoldState.snackbarHostState.showSnackbar(
                message = message, actionLabel = actionLabelText
            )
            if (result == SnackbarResult.ActionPerformed) {
                onActionLabelClicked()
            }
        }
    }

    suspend fun logout() {
        if (!isLoggedIn.value) {
            showSnackBar("이미 로그아웃 상태입니다")
        } else {
            try {
                val profileId = user.value.profile_id
                loginUseCase.updateFcmToken(accessToken.value, profileId, EMPTY)
                loginUseCase.logout(refreshToken.value)
                tokenUseCase.updateSavedRefreshToken(RefreshToken.empty)
                navController.navigate(Screen.CheckLoginScreen.route) {
                    popUpTo(Screen.MapScreen.route) { inclusive = true }
                }
                clearGlobalState()
            } catch (e: CustomException) {
                showSnackBar(e.message.toString())
            }
        }
    }

    fun refreshCafeInfos(
        latLng: LatLng = cameraPositionState.position.target,
        onSuccess: (List<CafeInfo>) -> Unit = {}
    ) {
        isCafeInfoLoading.value = true
        cafeInfos.value = emptyList()
        globalScope.launch {
            try {
                cafeInfos.value = emptyList()
                delay(100L)
                val responseCafeInfos = cafeUseCase.getCafeInfoList(
                    accessToken = accessToken.value,
                    latLng = latLng,
                    zoom = cameraPositionState.position.zoom
                )
                cafeInfos.value = responseCafeInfos
                onSuccess(responseCafeInfos)
            } catch (e: TokenExpiredException) {
                try {
                    refreshAccessToken {
                        refreshCafeInfos()
                    }
                } catch (e: RefreshTokenExpiredException) {
                }
            } catch (e: CustomException) {
                showSnackBar(e.message.toString())
            } finally {
                isCafeInfoLoading.value = false
            }
        }
    }

    fun checkMasterActivity() {
        globalScope.launch {
            try {
                val cafeLog = cafeUseCase.getMyUnExpiredCafeLog(accessToken.value)

                if (cafeLog.id == 0 && isMasterActivated.value) {
                    showSnackBar("혼잡도 공유활동이 자동으로 종료되었습니다")
                }
                if (cafeLog.id == 0) {
                    masterCafeLog.value = masterCafeLog.value.copy(
                        id = 0,
                        expired = true,
                        cafeDetailLogs = emptyList()
                    )
                    isMasterActivated.value = false
                } else {
                    masterCafeLog.value = cafeLog
                    isMasterActivated.value = true
                }

                val autoExpiredCafeLogRes = cafeUseCase.getAutoExpiredCafeLog(accessToken.value)

                if (autoExpiredCafeLogRes.id != 0) {
                    if (Time.getPassingDayFrom(autoExpiredCafeLogRes.finish) > 1) {
                        cafeUseCase.deleteAutoExpiredCafeLog(
                            accessToken = accessToken.value,
                            autoExpiredCafeLogId = autoExpiredCafeLogRes.id
                        )
                        autoExpiredCafeLog.value = AutoExpiredCafeLog.empty
                    } else {
                        autoExpiredCafeLog.value = autoExpiredCafeLogRes
                    }
                }
            } catch (e: TokenExpiredException) {
                refreshAccessToken { checkMasterActivity() }
            } catch (e: RefreshTokenExpiredException) {
            } catch (e: CustomException) {
                showSnackBar(e.message.toString())
            }
        }
    }

    fun updateUserFcmToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (task.isSuccessful) {
                if (user.value.fcm_token != task.result) {
                    globalScope.launch {
                        try {
                            loginUseCase.updateFcmToken(
                                accessToken.value, user.value.profile_id, task.result
                            ).apply {
                                user.value = this
                            }
                        } catch (e: Throwable) {
                        }
                    }
                }
                return@OnCompleteListener
            }
        })
    }

    fun navigateToWebView(topAppBarTitle: String, url: String) {
        webViewUrl.value = url
        webViewTitle.value = topAppBarTitle
        navController.navigate(Screen.WebViewScreen.route)
    }
}
