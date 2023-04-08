package com.software.cafejariapp.presentation.feature.main.viewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.software.cafejariapp.core.CustomException
import com.software.cafejariapp.core.RefreshTokenExpiredException
import com.software.cafejariapp.core.TokenExpiredException
import com.software.cafejariapp.presentation.GlobalState
import com.software.cafejariapp.domain.useCase.MainUseCase
import com.software.cafejariapp.presentation.feature.main.event.LeaderBoardEvent
import com.software.cafejariapp.presentation.feature.main.state.LeaderBoardState
import dagger.hilt.android.lifecycle.HiltViewModel
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
            is LeaderBoardEvent.Init -> {
                _state.value = state.value.copy(
                    isRankingListLoading = true
                )
                viewModelScope.launch {
                    try {
                        val rankingMonthList = mainUseCase.getMonthRankingList(
                            event.globalState.accessToken.value
                        )
                        val rankingWeekList = mainUseCase.getWeekRankingList(
                            event.globalState.accessToken.value
                        )
                        _state.value = state.value.copy(
                            rankingMonthList = rankingMonthList,
                            rankingWeekList = rankingWeekList
                        )
                    } catch (e: TokenExpiredException) {
                        try {
                            event.globalState.refreshAccessToken {
                                onEvent(LeaderBoardEvent.Init(event.globalState))
                            }
                        } catch (e: RefreshTokenExpiredException) {
                        }
                    } catch (e: CustomException) {
                        event.globalState.showSnackBar(e.message.toString())
                    } finally {
                        _state.value = state.value.copy(
                            isRankingListLoading = false
                        )
                    }
                }
            }
        }
    }

    fun init(globalState: GlobalState) {
        viewModelScope.launch {
            try {
                val rankingMonthList = mainUseCase.getMonthRankingList(
                    globalState.accessToken.value
                )
                val rankingWeekList = mainUseCase.getWeekRankingList(
                    globalState.accessToken.value
                )
                _state.value = state.value.copy(
                    rankingMonthList = rankingMonthList, rankingWeekList = rankingWeekList
                )
            } catch (e: TokenExpiredException) {
                try {
                    globalState.refreshAccessToken {
                        init(globalState)
                    }
                } catch (e: RefreshTokenExpiredException) {
                }
            } catch (e: CustomException) {
                globalState.showSnackBar(e.message.toString())
            } finally {
                _state.value = state.value.copy(
                    isRankingListLoading = true
                )
            }
        }
    }
}