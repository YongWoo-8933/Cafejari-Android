package com.software.cafejariapp.presentation.feature.login.viewModel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.software.cafejariapp.core.CustomException
import com.software.cafejariapp.core.RefreshTokenExpiredException
import com.software.cafejariapp.core.TokenExpiredException
import com.software.cafejariapp.presentation.util.Screen
import com.software.cafejariapp.core.isLocationTrackingPermitted
import com.software.cafejariapp.core.isReadExternalStoragePermitted
import com.software.cafejariapp.domain.entity.Login
import com.software.cafejariapp.domain.useCase.LoginUseCase
import com.software.cafejariapp.domain.util.SocialUserType
import com.software.cafejariapp.presentation.feature.login.event.LoginEvent
import com.software.cafejariapp.presentation.util.AuthResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    val googleSignInClient: GoogleSignInClient
) : ViewModel() {

    val predictions: MutableState<List<Login>> = mutableStateOf(emptyList())

    var authResult: AuthResult = AuthResult(googleSignInClient)
    var socialUserType: SocialUserType = SocialUserType.CafeJari
    var fcmToken: String = "_none"

    private var googleAccessToken = ""
    private var googleServerCode = ""
    private var kakaoAccessToken = ""
    private var cafejariEmail = ""
    private var cafejariPassword = ""
    private var resetToken = ""
    private var resetUserId = 0

    init {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (task.isSuccessful) {
                fcmToken = task.result
                return@OnCompleteListener
            }
        })
    }

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.ChangePredictions -> {
                viewModelScope.launch {
                    predictions.value = loginUseCase.getPredictions(event.email)
                }
            }
            is LoginEvent.ClearPredictions -> {
                predictions.value = emptyList()
            }
            is LoginEvent.DeletePrediction -> {
                viewModelScope.launch {
                    loginUseCase.deleteLoginInfo(event.login)
                }
            }
            is LoginEvent.CafejariLogin -> {
                viewModelScope.launch {
                    try {
                        val loginResponse = loginUseCase.cafeJariLogin(
                            email = event.email,
                            password = event.password
                        )
                        event.globalState.accessToken.value = loginResponse.first
                        event.globalState.refreshToken.value = loginResponse.second
                        event.globalState.user.value = loginResponse.third
                        event.globalState.isLoggedIn.value = true
                        event.globalState.updateUserFcmToken()
                        event.globalState.checkMasterActivity()
                        event.globalState.navController.navigate(Screen.MapScreen.route) {
                            popUpTo(Screen.CheckLoginScreen.route) { inclusive = true }
                        }
                        event.globalState.showSnackBar("카페자리 로그인 성공")
                    } catch (e: CustomException) {
                        event.globalState.showSnackBar(e.message.toString())
                    } finally {
                        event.onFinish()
                    }
                }
            }
            is LoginEvent.GoogleLogin -> {
                viewModelScope.launch {
                    try {
                        val loginResponse = loginUseCase.googleLogin(
                            email = event.email,
                            code = event.code,
                        )
                        when (loginResponse.first) {
                            true -> { // 회원 o, 로그인하는 유저의 경우
                                val loginFinishResponse = loginUseCase.googleLoginFinish(
                                    googleAccessToken = loginResponse.second,
                                    serverCode = loginResponse.third,
                                )
                                event.globalState.accessToken.value = loginFinishResponse.first
                                event.globalState.refreshToken.value = loginFinishResponse.second
                                event.globalState.user.value = loginFinishResponse.third
                                event.globalState.isLoggedIn.value = true
                                event.globalState.updateUserFcmToken()
                                event.globalState.checkMasterActivity()
                                event.globalState.navController.navigate(Screen.MapScreen.route) {
                                    popUpTo(Screen.CheckLoginScreen.route) { inclusive = true }
                                }
                                event.globalState.showSnackBar("구글 로그인 성공")
                            }
                            false -> { // 회원 x, 가입하는 유저의 경우
                                socialUserType = SocialUserType.Google
                                googleAccessToken = loginResponse.second
                                googleServerCode = loginResponse.third
                                event.globalState.navController.navigate(Screen.AuthScreen.route)
                                event.globalState.showSnackBar("구글 인증 완료, 닉네임과 번호를 등록해주세요!")
                            }
                        }
                    } catch (e: CustomException) {
                        event.globalState.showSnackBar(e.message.toString())
                    } finally {
                        event.onFinish()
                    }
                }
            }
            is LoginEvent.KakaoLogin -> {
                viewModelScope.launch {
                    try {
                        val loginResponse = loginUseCase.kakaoLogin(
                            kakaoAccessToken = event.accessToken
                        )
                        when (loginResponse.first) {
                            true -> { // 회원 o, 로그인하는 유저의 경우
                                val loginFinishResponse = loginUseCase.kakaoLoginFinish(
                                    kakaoAccessToken = loginResponse.second
                                )
                                event.globalState.accessToken.value = loginFinishResponse.first
                                event.globalState.refreshToken.value = loginFinishResponse.second
                                event.globalState.user.value = loginFinishResponse.third
                                event.globalState.isLoggedIn.value = true
                                event.globalState.updateUserFcmToken()
                                event.globalState.checkMasterActivity()
                                event.globalState.navController.navigate(Screen.MapScreen.route) {
                                    popUpTo(Screen.CheckLoginScreen.route) { inclusive = true }
                                }
                                event.globalState.showSnackBar("카카오 로그인 성공")
                            }
                            false -> { // 회원 x, 가입하는 유저의 경우
                                socialUserType = SocialUserType.Kakao
                                kakaoAccessToken = loginResponse.second
                                event.globalState.navController.navigate(Screen.AuthScreen.route)
                                event.globalState.showSnackBar("카카오 인증 완료, 닉네임과 번호를 등록해주세요!")
                            }
                        }
                    } catch (e: CustomException) {
                        event.globalState.showSnackBar(e.message.toString())
                    } finally {
                        event.onFinish()
                    }
                }
            }
            is LoginEvent.VerifyEmailPassword -> {
                viewModelScope.launch {
                    try {
                        val verifyResponse = loginUseCase.verifyEmailPassword(
                            email = event.email,
                            password1 = event.password1,
                            password2 = event.password2
                        )
                        socialUserType = SocialUserType.CafeJari
                        cafejariEmail = verifyResponse.first
                        cafejariPassword = verifyResponse.second
                        event.globalState.navController.navigate(Screen.AuthScreen.route)
                        event.globalState.showSnackBar("이메일과 비밀번호가 유효합니다. 닉네임, 번호를 등록해주세요!")
                    } catch (e: CustomException) {
                        event.globalState.showSnackBar(e.message.toString())
                    } finally {
                        event.onFinish()
                    }
                }
            }
            is LoginEvent.SendSms -> {
                viewModelScope.launch {
                    try {
                        loginUseCase.smsSend(event.phoneNumber)
                        event.onSuccess()
                        event.globalState.showSnackBar("메세지 전송 성공")
                    } catch (e: CustomException) {
                        event.globalState.showSnackBar(e.message.toString())
                    }
                }
            }
            is LoginEvent.AuthSms -> {
                viewModelScope.launch {
                    try {
                        loginUseCase.smsAuth(event.phoneNumber, event.authNumber)
                        event.onSuccess()
                        event.globalState.showSnackBar("인증번호 일치")
                    } catch (e: CustomException) {
                        event.globalState.showSnackBar(e.message.toString())
                    } finally {
                        event.onFinish()
                    }
                }
            }
            is LoginEvent.VerifyRecommendationNickname -> {
                viewModelScope.launch {
                    try {
                        loginUseCase.verifyRecommendationNickname(
                            nickname = event.nickname
                        )
                        event.globalState.showSnackBar("${event.nickname}님은 추천 가능합니다")
                    } catch (e: CustomException) {
                        event.globalState.showSnackBar(e.message.toString())
                    }
                }
            }
            is LoginEvent.Authorize -> {
                viewModelScope.launch {
                    try {
                        val verifyResponse = loginUseCase.verifyNicknamePhoneNumber(
                            event.nickname, event.phoneNumber
                        )
                        when (socialUserType) {
                            SocialUserType.CafeJari -> {
                                val loginResponse = loginUseCase.authorize(
                                    email = cafejariEmail,
                                    password = cafejariPassword,
                                    nickname = verifyResponse.first,
                                    phoneNumber = verifyResponse.second
                                )
                                event.globalState.accessToken.value = loginResponse.first
                                event.globalState.refreshToken.value = loginResponse.second
                                event.globalState.user.value = loginResponse.third
                                event.globalState.isLoggedIn.value = true
                                event.globalState.updateUserFcmToken()
                                event.onSuccess()

                                if (event.context.isLocationTrackingPermitted() && event.context.isReadExternalStoragePermitted()) {
                                    event.globalState.navController.navigate(Screen.MapScreen.route) {
                                        popUpTo(Screen.CheckLoginScreen.route) { inclusive = true }
                                    }
                                    event.globalState.showSnackBar("회원가입 성공! 카페자리의 모든 서비스를 활용해보세요")
                                } else {
                                    event.globalState.navController.navigate(Screen.PermissionScreen.route) {
                                        popUpTo(Screen.CheckLoginScreen.route) { inclusive = true }
                                    }
                                    event.globalState.showSnackBar("회원가입 성공! 서비스 이용을위해 앱권한을 허용해주세요")
                                }
                            }
                            SocialUserType.Google -> {
                                val socialLoginResponse = loginUseCase.googleLoginFinish(
                                    googleAccessToken = googleAccessToken,
                                    serverCode = googleServerCode
                                )
                                event.globalState.user.value = loginUseCase.makeNewProfile(
                                    accessToken = socialLoginResponse.first,
                                    userId = socialLoginResponse.third.userId,
                                    nickname = verifyResponse.first,
                                    phoneNumber = verifyResponse.second,
                                    fcmToken = fcmToken
                                )
                                event.globalState.accessToken.value = socialLoginResponse.first
                                event.globalState.refreshToken.value = socialLoginResponse.second
                                event.globalState.isLoggedIn.value = true
                                event.onSuccess()
                                if (event.context.isLocationTrackingPermitted() && event.context.isReadExternalStoragePermitted()) {
                                    event.globalState.navController.navigate(Screen.MapScreen.route) {
                                        popUpTo(Screen.CheckLoginScreen.route) { inclusive = true }
                                    }
                                    event.globalState.showSnackBar("회원가입 성공! 카페자리의 모든 서비스를 활용해보세요")
                                } else {
                                    event.globalState.navController.navigate(Screen.PermissionScreen.route) {
                                        popUpTo(Screen.CheckLoginScreen.route) { inclusive = true }
                                    }
                                    event.globalState.showSnackBar("회원가입 성공! 서비스 이용을위해 앱권한을 허용해주세요")
                                }
                            }
                            SocialUserType.Kakao -> {
                                val socialLoginResponse = loginUseCase.kakaoLoginFinish(
                                    kakaoAccessToken = kakaoAccessToken
                                )
                                event.globalState.user.value = loginUseCase.makeNewProfile(
                                    accessToken = socialLoginResponse.first,
                                    userId = socialLoginResponse.third.userId,
                                    nickname = verifyResponse.first,
                                    phoneNumber = verifyResponse.second,
                                    fcmToken = fcmToken
                                )
                                event.globalState.accessToken.value = socialLoginResponse.first
                                event.globalState.refreshToken.value = socialLoginResponse.second
                                event.globalState.isLoggedIn.value = true
                                event.onSuccess()
                                if (event.context.isLocationTrackingPermitted() && event.context.isReadExternalStoragePermitted()) {
                                    event.globalState.navController.navigate(Screen.MapScreen.route) {
                                        popUpTo(Screen.CheckLoginScreen.route) { inclusive = true }
                                    }
                                    event.globalState.showSnackBar("회원가입 성공! 카페자리의 모든 서비스를 활용해보세요")
                                } else {
                                    event.globalState.navController.navigate(Screen.PermissionScreen.route) {
                                        popUpTo(Screen.CheckLoginScreen.route) { inclusive = true }
                                    }
                                    event.globalState.showSnackBar("회원가입 성공! 서비스 이용을위해 앱권한을 허용해주세요")
                                }
                            }
                        }
                    } catch (e: CustomException) {
                        event.globalState.showSnackBar(e.message.toString())
                    } finally {
                        event.onFinish()
                    }
                }
            }
            is LoginEvent.Recommend -> {
                viewModelScope.launch {
                    try {
                        if (event.nickname.isNotBlank()) {
                            event.globalState.user.value = loginUseCase.recommend(
                                accessToken = event.globalState.accessToken.value,
                                nickname = event.nickname
                            )
                        }
                    } catch (e: TokenExpiredException) {
                        try {
                            event.globalState.refreshAccessToken {
                                onEvent(LoginEvent.Recommend(event.globalState, event.nickname))
                            }
                        } catch (e: RefreshTokenExpiredException) {
                        }
                    } catch (e: CustomException) {
                        event.globalState.showSnackBar(e.message.toString())
                    }
                }
            }
            is LoginEvent.SendSmsForReset -> {
                viewModelScope.launch {
                    try {
                        loginUseCase.resetSmsSend(event.phoneNumber)
                        event.onSuccess()
                        event.globalState.showSnackBar("메세지 전송 성공")
                    } catch (e: CustomException) {
                        event.globalState.showSnackBar(e.message.toString())
                    }
                }
            }
            is LoginEvent.AuthSmsForReset -> {
                viewModelScope.launch {
                    try {
                        val response =
                            loginUseCase.resetSmsAuth(event.phoneNumber, event.authNumber)
                        resetToken = response.first
                        resetUserId = response.second
                        event.onSuccess(response.third)
                        event.globalState.showSnackBar("인증번호 일치. 비밀번호를 변경하려면 계속 진행해주세요!")
                    } catch (e: CustomException) {
                        event.globalState.showSnackBar(e.message.toString())
                    }
                }
            }
            is LoginEvent.ResetPassword -> {
                if (resetToken.isBlank() || resetUserId == 0) {
                    event.globalState.showSnackBar("번호인증을 먼저 진행해주세요")
                } else {
                    viewModelScope.launch {
                        try {
                            loginUseCase.resetPassword(
                                resetToken, resetUserId, event.password1, event.password2
                            )
                            event.globalState.navController.popBackStack()
                            event.globalState.showSnackBar("비밀번호가 변경되었습니다. 바뀐 비밀번호로 로그인해주세요!")
                        } catch (e: CustomException) {
                            event.globalState.showSnackBar(e.message.toString())
                        }
                    }
                }
            }
        }
    }
}