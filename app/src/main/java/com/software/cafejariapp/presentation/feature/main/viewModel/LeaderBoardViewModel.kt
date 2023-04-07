package com.software.cafejariapp.presentation.feature.main.viewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.software.cafejariapp.core.CustomException
import com.software.cafejariapp.core.RefreshTokenExpiredException
import com.software.cafejariapp.core.TokenExpiredException
import com.software.cafejariapp.domain.useCase.MainUseCase
import com.software.cafejariapp.presentation.feature.main.event.LeaderBoardEvent
import com.software.cafejariapp.presentation.feature.main.state.LeaderBoardState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

import javax.inject.Inject


@HiltViewModel
class LeaderBoardViewModel @Inject constructor(
    private val mainUseCase: MainUseCase,
) : ViewModel() {

    private val _state = mutableStateOf(LeaderBoardState())
    val state: State<LeaderBoardState> = _state

    fun onEvent(event: LeaderBoardEvent) {
        when (event) {
            is LeaderBoardEvent.GetLeaders -> {
                _state.value = state.value.copy(
                    isLeadersLoading = true
                )
                viewModelScope.launch {
                    try {
                        _state.value = state.value.copy(
                            totalLeaders = mainUseCase.getTotalLeaders(
                                event.globalState.accessToken.value
                            ),
                            monthLeaders = mainUseCase.getMonthLeaders(
                                event.globalState.accessToken.value
                            ),
                            weekLeaders = mainUseCase.getWeekLeaders(
                                event.globalState.accessToken.value
                            )
                        )
                    } catch (e: TokenExpiredException) {
                        try {
                            event.globalState.refreshAccessToken {
                                onEvent(LeaderBoardEvent.GetLeaders(event.globalState))
                            }
                        } catch (e: RefreshTokenExpiredException) {
                        }
                    } catch (e: CustomException) {
                        event.globalState.showSnackBar(e.message.toString())
                    } finally {
                        delay(600L)
                        _state.value = state.value.copy(
                            isLeadersLoading = false
                        )
                    }
                }
            }
        }
    }
}