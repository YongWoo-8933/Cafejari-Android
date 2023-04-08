package com.software.cafejariapp.domain.repository

import com.software.cafejariapp.data.source.remote.dto.*
import com.software.cafejariapp.domain.entity.*


interface MainRepository {

    suspend fun getDisabledDates(): List<DisableDate>

    suspend fun insertDisabledDate(disabledDate: DisableDate)

    suspend fun getEvents(): List<Event>

    suspend fun getEventPointHistories(accessToken: String): List<EventPointHistory>

    suspend fun getVersion(): List<Version>

    suspend fun getItems(): List<Item>

    suspend fun getPurchaseRequests(accessToken: String): List<PurchaseResponse>

    suspend fun submitPurchaseRequest(accessToken: String, purchaseRequest: PurchaseRequest)

    suspend fun deletePurchaseRequest(accessToken: String, id: Int)

    suspend fun getFAQs(): List<FAQResponse>

    suspend fun getMyInquiryEtcs(accessToken: String): List<InquiryEtcResponse>

    suspend fun getMyInquiryCafes(accessToken: String): List<InquiryCafeResponse>

    suspend fun deleteInquiryEtc(accessToken: String, id: Int)

    suspend fun deleteInquiryCafe(accessToken: String, id: Int)

    suspend fun submitInquiryEtc(accessToken: String, inquiryEtcRequest: InquiryEtcRequest)

    suspend fun submitInquiryCafe(accessToken: String, inquiryCafeRequest: InquiryCafeRequest)

    suspend fun submitInquiryCafeAdditionalInfo(
        accessToken: String,
        inquiryCafeAdditionalInfoRequest: InquiryCafeAdditionalInfoRequest
    )

    suspend fun getWeekRankingList(accessToken: String): List<RankingResponse>

    suspend fun getMonthRankingList(accessToken: String): List<RankingResponse>

    suspend fun getPopUpNotificationList(): List<PopUpNotificationResponse>

    suspend fun getOnSaleCafeList(): List<OnSaleCafeResponse>
}