package com.software.cafejariapp.domain.useCase

import com.software.cafejariapp.domain.useCase.mainUseCaseImpl.*

data class MainUseCase(
    val getVersion: GetVersion,
    val getEventList: GetEventList,
    val getEventPointHistoryList: GetEventPointHistoryList,
    val getItemList: GetItemList,
    val getMyInquiryCafes: GetMyInquiryCafes,
    val getMyInquiryEtcs: GetMyInquiryEtcs,
    val getPurchaseRequestList: GetPurchaseRequestList,
    val getMonthLeaders: GetMonthLeaders,
    val getTotalLeaders: GetTotalLeaders,
    val getWeekLeaders: GetWeekLeaders,
    val getFAQList: GetFAQList,
    val getPopUpNotificationList: GetPopUpNotificationList,
    val getOnSaleCafeList: GetOnSaleCafeList,
    val isTodayExecutable: IsTodayExecutable,
    val isOnboardingWatched: IsOnboardingWatched,
    val setTodayDisable: SetTodayDisable,
    val submitInquiryCafe: SubmitInquiryCafe,
    val submitInquiryEtc: SubmitInquiryEtc,
    val submitInquiryCafeAdditionalInfo: SubmitInquiryCafeAdditionalInfo,
    val submitPurchaseRequest: SubmitPurchaseRequest,
    val deletePurchaseRequest: DeletePurchaseRequest,
    val deleteInquiryCafe: DeleteInquiryCafe,
    val deleteInquiryEtc: DeleteInquiryEtc
)
