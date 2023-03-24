package com.software.cafejariapp.presentation.feature.map.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.software.cafejariapp.core.CustomException
import com.software.cafejariapp.core.MasterExpiredException
import com.software.cafejariapp.core.RefreshTokenExpiredException
import com.software.cafejariapp.core.TokenExpiredException
import com.software.cafejariapp.presentation.util.Time
import com.software.cafejariapp.domain.useCase.CafeUseCase
import com.software.cafejariapp.presentation.feature.map.event.MasterRoomEvent
import com.software.cafejariapp.presentation.feature.map.util.PointResultType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MasterRoomViewModel @Inject constructor(
    private val cafeUseCase: CafeUseCase
) : ViewModel() {

    @OptIn(ExperimentalNaverMapApi::class)
    fun onEvent(event: MasterRoomEvent) {
        when (event) {
            is MasterRoomEvent.RegisterMaster -> {
                viewModelScope.launch {
                    try {
                        event.globalState.masterCafeLog.value = cafeUseCase.registerMaster(
                            accessToken = event.globalState.accessToken.value,
                            cafeId = event.globalState.modalCafe.value.id,
                            crowded = event.initialCrowdedInt
                        )
                        event.globalState.isMasterActivated.value = true
                        event.globalState.showSnackBar("마스터 등록 성공! 주기적으로 혼잡도를 업데이트 해주세요")
                        event.globalState.cameraPositionState.move(
                            CameraUpdate.scrollTo(
                                LatLng(
                                    event.globalState.masterCafeLog.value.latitude,
                                    event.globalState.masterCafeLog.value.longitude
                                )
                            )
                        )
                        event.globalState.refreshCafeInfos(
                            LatLng(
                                event.globalState.masterCafeLog.value.latitude,
                                event.globalState.masterCafeLog.value.longitude
                            )
                        )
                    } catch (e: TokenExpiredException) {
                        try {
                            event.globalState.refreshAccessToken {
                                onEvent(
                                    MasterRoomEvent.RegisterMaster(
                                        event.globalState, event.initialCrowdedInt
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
            is MasterRoomEvent.UpdateCrowded -> {
                if (event.globalState.masterCafeLog.value.cafeDetailLogs.isNotEmpty() && Time.getSecondFrom(
                        event.globalState.masterCafeLog.value.cafeDetailLogs[0].update
                    ) < 30
                ) {
                    event.globalState.showSnackBar("업데이트한지 30초가 지나지 않았습니다. 정보를 수정하시려면 기존 로그를 눌러 삭제후 다시 시도해주세요")
                } else {
                    viewModelScope.launch {
                        try {
                            event.globalState.masterCafeLog.value = cafeUseCase.updateCrowded(
                                accessToken = event.globalState.accessToken.value,
                                cafeLogId = event.globalState.masterCafeLog.value.id,
                                crowded = event.newCrowdedInt
                            )
                            event.globalState.showSnackBar("혼잡도가 성공적으로 업데이트 됐습니다")
                            event.globalState.cameraPositionState.move(
                                CameraUpdate.scrollTo(
                                    LatLng(
                                        event.globalState.masterCafeLog.value.latitude,
                                        event.globalState.masterCafeLog.value.longitude
                                    )
                                )
                            )
                            event.globalState.refreshCafeInfos(
                                LatLng(
                                    event.globalState.masterCafeLog.value.latitude,
                                    event.globalState.masterCafeLog.value.longitude
                                )
                            )
                        } catch (e: TokenExpiredException) {
                            try {
                                event.globalState.refreshAccessToken {
                                    onEvent(
                                        MasterRoomEvent.UpdateCrowded(
                                            event.globalState, event.newCrowdedInt
                                        )
                                    )
                                }
                            } catch (e: RefreshTokenExpiredException) {
                            }
                        } catch (e: MasterExpiredException) {
                            event.globalState.isMasterActivated.value = false
                            event.globalState.masterCafeLog.value =
                                event.globalState.masterCafeLog.value.copy(
                                    id = 0, expired = true, cafeDetailLogs = emptyList()
                                )
                            event.globalState.showSnackBar("마스터 활동이 자동 종료되었습니다. 자세한 정보는 프로필에서 확인하세요!")
                            event.globalState.navController.popBackStack()
                        } catch (e: CustomException) {
                            event.globalState.showSnackBar(e.message.toString())
                        }
                    }
                }
            }
            is MasterRoomEvent.DeleteCafeDetailLog -> {
                viewModelScope.launch {
                    try {
                        event.globalState.masterCafeLog.value = cafeUseCase.deleteCafeDetailLog(
                            accessToken = event.globalState.accessToken.value,
                            cafeDetailLogId = event.selectedDetailLogId
                        )
                        event.globalState.cameraPositionState.move(
                            CameraUpdate.scrollTo(
                                LatLng(
                                    event.globalState.masterCafeLog.value.latitude,
                                    event.globalState.masterCafeLog.value.longitude
                                )
                            )
                        )
                        event.globalState.refreshCafeInfos(
                            LatLng(
                                event.globalState.masterCafeLog.value.latitude,
                                event.globalState.masterCafeLog.value.longitude
                            )
                        )
                    } catch (e: TokenExpiredException) {
                        try {
                            event.globalState.refreshAccessToken {
                                onEvent(
                                    MasterRoomEvent.DeleteCafeDetailLog(
                                        event.globalState, event.selectedDetailLogId
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
            is MasterRoomEvent.ExpireMaster -> {
                viewModelScope.launch {
                    try {
                        event.globalState.masterCafeLog.value = cafeUseCase.expireMaster(
                            event.globalState.accessToken.value,
                            event.globalState.masterCafeLog.value.id,
                            event.adMultipleApplied
                        ).copy(
                            id = 0,
                            expired = true,
                            cafeDetailLogs = emptyList()
                        )
                        event.onSuccess(
                            event.globalState.masterCafeLog.value.point,
                            if (event.adMultipleApplied) {
                                PointResultType.MasterExpiredWithAd
                            } else {
                                PointResultType.MasterExpired
                            }
                        )
                        event.globalState.isMasterActivated.value = false
                        event.globalState.cameraPositionState.move(
                            CameraUpdate.scrollTo(
                                LatLng(
                                    event.globalState.masterCafeLog.value.latitude,
                                    event.globalState.masterCafeLog.value.longitude
                                )
                            )
                        )
                        event.globalState.refreshCafeInfos(
                            LatLng(
                                event.globalState.masterCafeLog.value.latitude,
                                event.globalState.masterCafeLog.value.longitude
                            )
                        )
                    } catch (e: TokenExpiredException) {
                        try {
                            event.globalState.refreshAccessToken {
                                onEvent(
                                    MasterRoomEvent.ExpireMaster(
                                        event.globalState,
                                        event.adMultipleApplied,
                                        event.onSuccess
                                    )
                                )
                            }
                        } catch (e: RefreshTokenExpiredException) {
                        }
                    } catch (e: MasterExpiredException) {
                        event.globalState.isMasterActivated.value = false
                        event.globalState.masterCafeLog.value =
                            event.globalState.masterCafeLog.value.copy(
                                id = 0,
                                expired = true,
                                cafeDetailLogs = emptyList()
                            )
                        event.globalState.showSnackBar("마스터 활동이 자동 종료되었습니다. 자세한 정보는 프로필에서 확인하세요!")
                    } catch (e: CustomException) {
                        event.globalState.showSnackBar(e.message.toString())
                    }
                }
            }
        }
    }
}

