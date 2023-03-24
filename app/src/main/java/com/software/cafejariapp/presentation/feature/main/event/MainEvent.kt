package com.software.cafejariapp.presentation.feature.main.event

import com.software.cafejariapp.presentation.GlobalState

sealed class MainEvent {
    data class GetFAQs(val globalState: GlobalState) : MainEvent()

    data class SubmitAdditionalCafeInfo(
        val globalState: GlobalState,
        val storeInfoContent: String,
        val openingHourContent: String,
        val wallSocketContent: String,
        val restroomContent: String
    ) : MainEvent()

    data class GetInquiryCafes(val globalState: GlobalState) : MainEvent()

    data class SubmitInquiryCafe(
        val globalState: GlobalState,
        val cafeName: String,
        val cafeAddress: String,
    ) : MainEvent()

    data class DeleteInquiryCafe(
        val globalState: GlobalState,
        val inquiryCafeId: Int
    ) : MainEvent()

    data class GetInquiryEtcs(val globalState: GlobalState) : MainEvent()

    data class SubmitInquiryEtc(
        val globalState: GlobalState,
        val content: String
    ) : MainEvent()

    data class DeleteInquiryEtc(
        val globalState: GlobalState,
        val inquiryEtcId: Int
    ) : MainEvent()

    data class GetEvents(val globalState: GlobalState) : MainEvent()


}