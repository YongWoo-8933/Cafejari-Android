package com.software.cafejariapp.presentation.feature.main.viewModel

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.himanshoe.kalendar.common.data.KalendarEvent
import com.software.cafejariapp.core.CustomException
import com.software.cafejariapp.core.RefreshTokenExpiredException
import com.software.cafejariapp.core.TokenExpiredException
import com.software.cafejariapp.presentation.util.Screen
import com.software.cafejariapp.domain.useCase.LoginUseCase
import com.software.cafejariapp.domain.useCase.MainUseCase
import com.software.cafejariapp.domain.entity.RefreshToken
import com.software.cafejariapp.domain.useCase.CafeUseCase
import com.software.cafejariapp.domain.useCase.TokenUseCase
import com.software.cafejariapp.presentation.util.AuthResult
import com.software.cafejariapp.presentation.feature.main.event.ProfileEvent
import com.software.cafejariapp.presentation.feature.main.state.ProfileState
import com.software.cafejariapp.presentation.feature.main.util.UriPathHelper
import com.software.cafejariapp.presentation.util.Time
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate

import javax.inject.Inject


@OptIn(ExperimentalPagerApi::class)
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val tokenUseCase: TokenUseCase,
    private val mainUseCase: MainUseCase,
    private val cafeUseCase: CafeUseCase,
    private val application: Application,
    val googleSignInClient: GoogleSignInClient
) : ViewModel() {

    private val _state = mutableStateOf(ProfileState())
    val state: State<ProfileState> = _state

    var authResult: AuthResult = AuthResult(googleSignInClient)

    fun onEvent(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.ProfileScreenInit -> {
                viewModelScope.launch {
                    try {
                        event.globalState.user.value =
                            loginUseCase.getUser(event.globalState.accessToken.value)
                        val events = mainUseCase.getEventList().first
                        if (events.isNotEmpty()) {
                            _state.value = state.value.copy(
                                events = events,
                                pagerState = PagerState(
                                    events.size,
                                    0,
                                    0f,
                                    3,
                                    true
                                ),
                            )
                        }
                    } catch (e: TokenExpiredException) {
                        try {
                            event.globalState.refreshAccessToken {
                                onEvent(ProfileEvent.ProfileScreenInit(event.globalState))
                            }
                        } catch (e: RefreshTokenExpiredException) {
                        }
                    } catch (e: CustomException) {
                        event.globalState.showSnackBar(e.message.toString())
                    } finally {
                        _state.value = state.value.copy(
                            isEventLoaded = true,
                        )
                    }
                }
            }
            is ProfileEvent.ProfileEditScreenInit -> {
                _state.value = state.value.copy(
                    isSocialUserTypeLoading = true,
                    isProfileEditAuthed = false
                )
                viewModelScope.launch {
                    try {
                        _state.value = state.value.copy(
                            socialUserType = loginUseCase.getSocialUserType(event.globalState.accessToken.value)
                        )
                    } catch (e: TokenExpiredException) {
                        try {
                            event.globalState.refreshAccessToken {
                                onEvent(ProfileEvent.ProfileEditScreenInit(event.globalState))
                            }
                        } catch (e: RefreshTokenExpiredException) {
                        }
                    } catch (e: CustomException) {
                        event.globalState.showSnackBar(e.message.toString())
                    } finally {
                        delay(200L)
                        _state.value = state.value.copy(
                            isSocialUserTypeLoading = false
                        )
                    }
                }
            }
            is ProfileEvent.DeleteAccount -> {
                viewModelScope.launch {
                    try {
                        mainUseCase.submitInquiryEtc(
                            event.globalState.accessToken.value,
                            event.globalState.user.value.email,
                            "회원탈퇴요청됨. email: ${event.globalState.user.value.email}, nickname: ${event.globalState.user.value.nickname}"
                        )
                        event.globalState.showSnackBar("회원탈퇴 요청을 완료했습니다")
                        event.globalState.logout()
                    } catch (e: TokenExpiredException) {
                        try {
                            event.globalState.refreshAccessToken {
                                onEvent(ProfileEvent.DeleteAccount(event.globalState))
                            }
                        } catch (e: RefreshTokenExpiredException) {
                        }
                    } catch (e: CustomException) {
                        event.globalState.showSnackBar(e.message.toString())
                    }
                }
            }
            is ProfileEvent.CafejariLoginForAuth -> {
                viewModelScope.launch {
                    try {
                        loginUseCase.cafeJariLogin(
                            email = event.globalState.user.value.email,
                            password = event.password
                        )
                        event.globalState.showSnackBar("비밀번호 일치")
                        _state.value = state.value.copy(
                            isProfileEditAuthed = true,
                        )
                    } catch (e: CustomException) {
                        event.globalState.showSnackBar(e.message.toString())
                    }
                }
            }
            is ProfileEvent.GoogleLoginForAuth -> {
                viewModelScope.launch {
                    try {
                        val googleResponse = loginUseCase.googleLogin(
                            email = event.email,
                            code = event.code
                        )
                        if (googleResponse.first) {
                            val loginFinishResponse = loginUseCase.googleLoginFinish(
                                googleAccessToken = googleResponse.second,
                                serverCode = googleResponse.third,
                            )
                            if (event.globalState.user.value.userId == loginFinishResponse.third.userId) {
                                event.globalState.accessToken.value = loginFinishResponse.first
                                event.globalState.refreshToken.value = loginFinishResponse.second
                                event.globalState.user.value = loginFinishResponse.third
                                event.globalState.showSnackBar("구글 인증 성공")
                                _state.value = state.value.copy(
                                    isProfileEditAuthed = true,
                                )
                            } else {
                                event.globalState.showSnackBar("현재 유저 정보와 일치하지 않습니다")
                            }
                        } else {
                            event.globalState.showSnackBar("유저 정보가 존재하지 않습니다")
                        }
                    } catch (e: CustomException) {
                        event.globalState.showSnackBar(e.message.toString())
                    } finally {
                        event.onFinish()
                    }
                }
            }
            is ProfileEvent.KakaoLoginForAuth -> {
                viewModelScope.launch {
                    try {
                        val kakaoResponse = loginUseCase.kakaoLogin(
                            kakaoAccessToken = event.accessToken
                        )
                        if (kakaoResponse.first) {
                            val loginFinishResponse = loginUseCase.kakaoLoginFinish(
                                kakaoAccessToken = kakaoResponse.second,
                            )
                            if (event.globalState.user.value.userId == loginFinishResponse.third.userId) {
                                event.globalState.accessToken.value = loginFinishResponse.first
                                event.globalState.refreshToken.value = loginFinishResponse.second
                                event.globalState.user.value = loginFinishResponse.third
                                event.globalState.showSnackBar("카카오 인증 성공")
                                _state.value = state.value.copy(
                                    isProfileEditAuthed = true,
                                )
                            } else {
                                event.globalState.showSnackBar("현재 유저 정보와 일치하지 않습니다")
                            }
                        } else {
                            event.globalState.showSnackBar("유저 정보가 존재하지 않습니다")
                        }
                    } catch (e: CustomException) {
                        event.globalState.showSnackBar(e.message.toString())
                    } finally {
                        event.onFinish()
                    }
                }
            }
            is ProfileEvent.UpdateProfile -> {
                viewModelScope.launch {
                    _state.value = state.value.copy(
                        isProfileUpdateLoading = true,
                    )
                    val imagePath = if (event.image == null) {
                        null
                    } else {
                        UriPathHelper().getPath(application.applicationContext, event.image)
                    }
                    val nickname = if (event.globalState.user.value.nickname == event.nickname) {
                        null
                    } else {
                        event.nickname
                    }
                    try {
                        event.globalState.user.value = loginUseCase.updateProfile(
                            accessToken = event.globalState.accessToken.value,
                            profileId = event.globalState.user.value.profileId,
                            imagePath = imagePath,
                            nickname = nickname,
                        )
                        application.applicationContext.cacheDir.deleteRecursively()
                        event.globalState.checkMasterActivity()
                        event.globalState.refreshCafeInfos()
                        delay(1000L)
                        event.globalState.showSnackBar("변경사항이 저장되었습니다. 프로필 사진이 바뀌지 않았을경우, 앱을 재시작하면 제대로 적용됩니다")
                        event.globalState.navController.popBackStack()
                    } catch (e: TokenExpiredException) {
                        try {
                            event.globalState.refreshAccessToken {
                                onEvent(
                                    ProfileEvent.UpdateProfile(
                                        event.globalState, event.nickname, event.image
                                    )
                                )
                            }
                        } catch (e: RefreshTokenExpiredException) {
                        }
                    } catch (e: CustomException) {
                        event.globalState.showSnackBar(e.message.toString())
                    } finally {
                        _state.value = state.value.copy(
                            isProfileUpdateLoading = false,
                        )
                    }
                }
            }
            is ProfileEvent.ProfileKalendarScreenInit -> {
                _state.value = state.value.copy(
                    isKalendarCafeLogLoading = true,
                )
                viewModelScope.launch {
                    try {
                        val cafeLogs = cafeUseCase.getMyCafeLogList(
                            event.globalState.accessToken.value
                        )
                        val kalendarEvents = mutableListOf<KalendarEvent>()
                        cafeLogs.forEach { cafeLog ->
                            val kalendarEvent = KalendarEvent(
                                date = Time.getLocalDate(cafeLog[0].start) ?: LocalDate.of(
                                    2010,
                                    1,
                                    1
                                ),
                                eventName = "test",
                                eventDescription = "test"
                            )
                            kalendarEvents.add(kalendarEvent)
                        }
                        _state.value = state.value.copy(
                            kalendarCafeLogs = cafeLogs,
                            kalendarEvents = kalendarEvents
                        )
                    } catch (e: TokenExpiredException) {
                        try {
                            event.globalState.refreshAccessToken {
                                onEvent(ProfileEvent.ProfileKalendarScreenInit(event.globalState))
                            }
                        } catch (e: RefreshTokenExpiredException) {
                        }
                    } catch (e: CustomException) {
                        event.globalState.showSnackBar(e.message.toString())
                    } finally {
                        _state.value = state.value.copy(
                            isKalendarCafeLogLoading = false,
                        )
                    }
                }
            }
            is ProfileEvent.SelectCafeLogs -> {
                _state.value = state.value.copy(
                    selectedCafeLogs = event.selectedCafeLogs
                )
            }
            is ProfileEvent.PointHistoryScreenInit -> {
                _state.value = state.value.copy(
                    isHistoryCafeLogLoading = true,
                    isEventPointHistoryLoading = true
                )
                viewModelScope.launch {
                    try {
                        _state.value = state.value.copy(
                            eventPointHistories = mainUseCase.getEventPointHistoryList(
                                event.globalState.accessToken.value
                            ),
                            isEventPointHistoryLoading = false
                        )
                        _state.value = state.value.copy(
                            historyCafeLogs = cafeUseCase.getMyCafeLogList(
                                event.globalState.accessToken.value
                            ),
                            isHistoryCafeLogLoading = false
                        )
                    } catch (e: TokenExpiredException){
                        try {
                            event.globalState.refreshAccessToken {
                                onEvent(ProfileEvent.PointHistoryScreenInit(event.globalState))
                            }
                        } catch (e: RefreshTokenExpiredException){

                        }
                    }  catch (e: CustomException){
                        event.globalState.showSnackBar(e.message.toString())
                    } finally {
                        _state.value = state.value.copy(
                            isHistoryCafeLogLoading = false,
                            isEventPointHistoryLoading = false
                        )
                    }
                }
            }
        }
    }
}