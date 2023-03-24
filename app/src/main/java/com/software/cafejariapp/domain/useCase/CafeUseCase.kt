package com.software.cafejariapp.domain.useCase

import com.software.cafejariapp.domain.useCase.cafeUseCaseImpl.*


data class CafeUseCase(
    val getCafeInfoList: GetCafeInfoList,
    val registerMaster: RegisterMaster,
    val updateCrowded: UpdateCrowded,
    val expireMaster: ExpireMaster,
    val addAdPoint: AddAdPoint,
    val deleteCafeDetailLog: DeleteCafeDetailLog,
    val getMyUnExpiredCafeLog: GetMyUnExpiredCafeLog,
    val getMyCafeLogList: GetMyCafeLogList,
    val getAutoExpiredCafeLog: GetAutoExpiredCafeLog,
    val deleteAutoExpiredCafeLog: DeleteAutoExpiredCafeLog,
    val requestThumbsUp: RequestThumbsUp,
    val search: Search,
)
