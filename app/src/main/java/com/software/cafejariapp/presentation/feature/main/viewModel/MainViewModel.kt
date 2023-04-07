package com.software.cafejariapp.presentation.feature.main.viewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.software.cafejariapp.core.CustomException
import com.software.cafejariapp.core.RefreshTokenExpiredException
import com.software.cafejariapp.core.TokenExpiredException
import com.software.cafejariapp.domain.useCase.MainUseCase
import com.software.cafejariapp.presentation.feature.main.event.MainEvent
import com.software.cafejariapp.presentation.feature.main.state.MainState
import com.software.cafejariapp.presentation.util.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainUseCase: MainUseCase
) : ViewModel() {

    private val _state = mutableStateOf(MainState())
    val state: State<MainState> = _state

    init {
        viewModelScope.launch {

        }
    }

    fun onEvent(event: MainEvent) {
        when (event) {
            is MainEvent.GetFAQs -> {
                _state.value = state.value.copy(
                    isFaqsLoading = true
                )
                viewModelScope.launch {
                    try {
                        _state.value =
                            state.value.copy(
                                faqs = mainUseCase.getFAQList().sortedBy { it.order }
                            )
                    } catch (e: CustomException) {
                        event.globalState.showSnackBar(e.message.toString())
                    } finally {
                        delay(600L)
                        _state.value = state.value.copy(
                            isFaqsLoading = false
                        )
                    }
                }
            }
            is MainEvent.SubmitAdditionalCafeInfo -> {
                viewModelScope.launch {
                    try {
                        mainUseCase.submitInquiryCafeAdditionalInfo(
                            accessToken = event.globalState.accessToken.value,
                            cafeInfoId = event.globalState.modalCafeInfo.value.id,
                            storeInformation = event.storeInfoContent.ifBlank { "_none" },
                            openingHour = event.openingHourContent.ifBlank { "_none" },
                            wallSocket = event.wallSocketContent.ifBlank { "_none" },
                            restroom = event.restroomContent.ifBlank { "_none" })
                        event.globalState.showSnackBar("카페 정보를 제보했습니다")
                        event.globalState.navController.popBackStack()
                    } catch (e: TokenExpiredException) {
                        try {
                            event.globalState.refreshAccessToken {
                                onEvent(
                                    MainEvent.SubmitAdditionalCafeInfo(
                                        globalState = event.globalState,
                                        storeInfoContent = event.storeInfoContent,
                                        openingHourContent = event.openingHourContent,
                                        wallSocketContent = event.wallSocketContent,
                                        restroomContent = event.restroomContent
                                    )
                                )
                            }
                        } catch (e: RefreshTokenExpiredException) {
                        }
                    } catch (e: CustomException) {
                        event.globalState.showSnackBar(e.message.toString())
                    }
                }
            }
            is MainEvent.GetInquiryCafes -> {
                _state.value = state.value.copy(
                    isInquiryCafeLoading = true
                )
                viewModelScope.launch {
                    try {
                        _state.value = state.value.copy(
                            inquiryCafes = mainUseCase.getMyInquiryCafes(event.globalState.accessToken.value)
                        )
                    } catch (e: TokenExpiredException) {
                        try {
                            event.globalState.refreshAccessToken {
                                onEvent(MainEvent.GetInquiryCafes(event.globalState))
                            }
                        } catch (e: RefreshTokenExpiredException) {

                        }
                    } catch (e: CustomException) {
                        event.globalState.showSnackBar(e.message.toString())
                    } finally {
                        delay(600L)
                        _state.value = state.value.copy(
                            isInquiryCafeLoading = false
                        )
                    }
                }
            }
            is MainEvent.SubmitInquiryCafe -> {
                viewModelScope.launch {
                    try {
                        mainUseCase.submitInquiryCafe(
                            accessToken = event.globalState.accessToken.value,
                            email = event.globalState.user.value.email,
                            cafeName = event.cafeName,
                            cafeAddress = event.cafeAddress.ifBlank { "주소 생략됨" }
                        )
                        delay(500L)
                        event.globalState.showSnackBar("카페 등록요청을 완료하였습니다")
                        event.globalState.navController.navigate(Screen.RegisterCafeResultScreen.route)
                    } catch (e: TokenExpiredException) {
                        try {
                            event.globalState.refreshAccessToken {
                                onEvent(
                                    MainEvent.SubmitInquiryCafe(
                                        globalState = event.globalState,
                                        cafeName = event.cafeName,
                                        cafeAddress = event.cafeAddress
                                    )
                                )
                            }
                        } catch (e: RefreshTokenExpiredException) {
                        }
                    } catch (e: CustomException) {
                        event.globalState.showSnackBar(e.message.toString())
                    }
                }
            }
            is MainEvent.DeleteInquiryCafe -> {
                viewModelScope.launch {
                    try {
                        mainUseCase.deleteInquiryCafe(
                            event.globalState.accessToken.value,
                            event.inquiryCafeId
                        )
                        delay(500L)
                        onEvent(MainEvent.GetInquiryCafes(event.globalState))
                        event.globalState.showSnackBar("삭제 성공")
                    } catch (e: TokenExpiredException) {
                        try {
                            event.globalState.refreshAccessToken {
                                onEvent(
                                    MainEvent.DeleteInquiryCafe(
                                        globalState = event.globalState,
                                        inquiryCafeId = event.inquiryCafeId
                                    )
                                )
                            }
                        } catch (e: RefreshTokenExpiredException) {

                        }
                    } catch (e: CustomException) {
                        event.globalState.showSnackBar(e.message.toString())
                    }
                }
            }
            is MainEvent.GetInquiryEtcs -> {
                _state.value = state.value.copy(
                    isInquiryEtcLoading = true
                )
                viewModelScope.launch {
                    try {
                        _state.value = state.value.copy(
                            inquiryEtcs = mainUseCase.getMyInquiryEtcs(event.globalState.accessToken.value)
                        )
                    } catch (e: TokenExpiredException) {
                        try {
                            event.globalState.refreshAccessToken {
                                onEvent(MainEvent.GetInquiryEtcs(event.globalState))
                            }
                        } catch (e: RefreshTokenExpiredException) {

                        }
                    } catch (e: CustomException) {
                        event.globalState.showSnackBar(e.message.toString())
                    } finally {
                        delay(600L)
                        _state.value = state.value.copy(
                            isInquiryEtcLoading = false
                        )
                    }
                }
            }
            is MainEvent.SubmitInquiryEtc -> {
                viewModelScope.launch {
                    try {
                        mainUseCase.submitInquiryEtc(
                            accessToken = event.globalState.accessToken.value,
                            email = event.globalState.user.value.email,
                            content = event.content
                        )
                        delay(500L)
                        event.globalState.showSnackBar("문의를 등록했습니다")
                        event.globalState.navController.navigate(Screen.InquiryAnswerScreen.route)
                    } catch (e: TokenExpiredException) {
                        try {
                            event.globalState.refreshAccessToken {
                                onEvent(
                                    MainEvent.SubmitInquiryEtc(
                                        globalState = event.globalState,
                                        content = event.content
                                    )
                                )
                            }
                        } catch (e: RefreshTokenExpiredException) {
                        }
                    } catch (e: CustomException) {
                        event.globalState.showSnackBar(e.message.toString())
                    }
                }
            }
            is MainEvent.DeleteInquiryEtc -> {
                viewModelScope.launch {
                    try {
                        mainUseCase.deleteInquiryEtc(
                            event.globalState.accessToken.value, event.inquiryEtcId
                        )
                        delay(500L)
                        onEvent(MainEvent.GetInquiryEtcs(event.globalState))
                        event.globalState.showSnackBar("삭제 성공")
                    } catch (e: TokenExpiredException) {
                        try {
                            event.globalState.refreshAccessToken {
                                onEvent(
                                    MainEvent.DeleteInquiryEtc(
                                        globalState = event.globalState,
                                        inquiryEtcId = event.inquiryEtcId
                                    )
                                )
                            }
                        } catch (e: RefreshTokenExpiredException) {

                        }
                    } catch (e: CustomException) {
                        event.globalState.showSnackBar(e.message.toString())
                    }
                }
            }
            is MainEvent.GetEvents -> {
                _state.value = state.value.copy(
                    isEventsLoading = true
                )
                viewModelScope.launch {
                    try {
                        val pair = mainUseCase.getEventList()
                        _state.value = state.value.copy(
                            events = pair.first,
                            expiredEvents = pair.second
                        )
                    } catch (e: CustomException) {
                    } finally {
                        delay(600L)
                        _state.value = state.value.copy(
                            isEventsLoading = false
                        )
                    }
                }
            }
        }
    }
}