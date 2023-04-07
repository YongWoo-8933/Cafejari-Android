package com.software.cafejariapp.presentation.feature.main.viewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.software.cafejariapp.core.CustomException
import com.software.cafejariapp.core.RefreshTokenExpiredException
import com.software.cafejariapp.core.TokenExpiredException
import com.software.cafejariapp.presentation.GlobalState
import com.software.cafejariapp.presentation.util.Screen
import com.software.cafejariapp.domain.entity.Item
import com.software.cafejariapp.domain.useCase.MainUseCase
import com.software.cafejariapp.presentation.feature.main.event.ProfileEvent
import com.software.cafejariapp.presentation.feature.main.event.ShopEvent
import com.software.cafejariapp.presentation.feature.main.state.ShopState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

import javax.inject.Inject


@HiltViewModel
class ShopViewModel @Inject constructor(
    private val mainUseCase: MainUseCase
) : ViewModel() {

    private val _state = mutableStateOf(ShopState())
    val state: State<ShopState> = _state

    fun onEvent(event: ShopEvent) {
        when (event) {
            is ShopEvent.GetItems -> {
                _state.value = state.value.copy(
                    isItemLoading = true
                )
                viewModelScope.launch {
                    try {
                        _state.value = state.value.copy(
                            items = mainUseCase.getItemList()
                        )
                    } catch (e: CustomException) {

                    } finally {
                        delay(600L)
                        _state.value = state.value.copy(
                            isItemLoading = false
                        )
                    }
                }
            }
            is ShopEvent.RequestPurchase -> {
                if (event.globalState.user.value.point < (event.item.price)) {
                    event.globalState.showSnackBar("포인트가 부족합니다")
                } else {
                    viewModelScope.launch {
                        try {
                            mainUseCase.submitPurchaseRequest(
                                accessToken = event.globalState.accessToken.value,
                                itemId = event.item.id
                            )
                            event.globalState.showSnackBar("성공적으로 요청되었습니다")
                            event.globalState.navController.navigate(Screen.ShoppingBagScreen.route)
                        } catch (e: TokenExpiredException) {
                            try {
                                event.globalState.refreshAccessToken {
                                    onEvent(
                                        ShopEvent.RequestPurchase(
                                            globalState = event.globalState,
                                            item = event.item
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
            }
            is ShopEvent.GetPurchaseHistories -> {
                _state.value = state.value.copy(
                    isPurchaseHistoryLoading = true
                )
                viewModelScope.launch {
                    try {
                        _state.value = state.value.copy(
                            purchaseHistories = mainUseCase.getPurchaseRequestList(
                                event.globalState.accessToken.value
                            )
                        )
                    } catch (e: TokenExpiredException){
                        try {
                            event.globalState.refreshAccessToken{
                                onEvent(ShopEvent.GetPurchaseHistories(event.globalState))
                            }
                        } catch (e: RefreshTokenExpiredException){}
                    } catch (e: CustomException){
                        event.globalState.showSnackBar(e.message.toString())
                    } finally {
                        delay(600L)
                        _state.value = state.value.copy(
                            isPurchaseHistoryLoading = false
                        )
                    }
                }
            }
            is ShopEvent.DeletePurchaseHistory -> {
                viewModelScope.launch {
                    try {
                        mainUseCase.deletePurchaseRequest(
                            event.globalState.accessToken.value, event.history.id
                        )
                        event.globalState.showSnackBar("삭제 성공")
                    } catch (e: TokenExpiredException){
                        try {
                            event.globalState.refreshAccessToken{
                                onEvent(
                                    ShopEvent.DeletePurchaseHistory(
                                        event.globalState,
                                        event.history
                                    )
                                )
                            }
                        } catch (e: RefreshTokenExpiredException){}
                    } catch (e: CustomException){
                        event.globalState.showSnackBar(e.message.toString())
                    } finally {
                        onEvent(ShopEvent.GetPurchaseHistories(event.globalState))
                    }
                }
            }
        }
    }
}