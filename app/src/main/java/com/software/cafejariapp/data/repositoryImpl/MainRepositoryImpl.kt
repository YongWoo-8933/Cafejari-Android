package com.software.cafejariapp.data.repositoryImpl

import com.software.cafejariapp.data.source.remote.HttpRoutes
import com.software.cafejariapp.core.getException
import com.software.cafejariapp.data.source.local.dao.DisableDateDao
import com.software.cafejariapp.data.source.remote.dto.*
import com.software.cafejariapp.domain.entity.*
import com.software.cafejariapp.domain.repository.MainRepository
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.client.statement.*

class MainRepositoryImpl(
    private val dao: DisableDateDao, private val client: HttpClient
) : MainRepository {

    override suspend fun getDisabledDates(): List<DisableDate> {
        return dao.getDisabledDates()
    }

    override suspend fun insertDisabledDate(disabledDate: DisableDate) {
        return dao.insertDisabledDate(disabledDate)
    }

    override suspend fun getVersion(): List<Version> {
        return try {
            client.get { url(HttpRoutes.ANDROID_VERSION) }
        } catch (throwable: Throwable) {
            throw throwable.getException()
        }
    }

    override suspend fun getWeekRankingList(accessToken: String): List<RankingResponse> {
        return try {
            client.get {
                headers { append(HttpHeaders.Authorization, "Bearer $accessToken") }
                url(HttpRoutes.WEEK_LEADERS)
            }
        } catch (throwable: Throwable) {
            throw throwable.getException()
        }
    }

    override suspend fun getMonthRankingList(accessToken: String): List<RankingResponse> {
        return try {
            client.get {
                headers { append(HttpHeaders.Authorization, "Bearer $accessToken") }
                url(HttpRoutes.MONTH_LEADERS)
            }
        } catch (throwable: Throwable) {
            throw throwable.getException()
        }
    }

    override suspend fun getTotalRankingList(accessToken: String): List<RankingResponse> {
        return try {
            client.get {
                headers { append(HttpHeaders.Authorization, "Bearer $accessToken") }
                url(HttpRoutes.TOTAL_LEADERS)
            }
        } catch (throwable: Throwable) {
            throw throwable.getException()
        }
    }

    override suspend fun getItems(): List<Item> {
        return try {
            client.get { url(HttpRoutes.ITEMS) }
        } catch (throwable: Throwable) {
            throw throwable.getException()
        }
    }

    override suspend fun getPurchaseRequests(accessToken: String): List<PurchaseResponse> {
        return try {
            client.get {
                headers { append(HttpHeaders.Authorization, "Bearer $accessToken") }
                url(HttpRoutes.PURCHASE_REQUEST)
            }
        } catch (throwable: Throwable) {
            throw throwable.getException()
        }
    }

    override suspend fun getMyInquiryCafes(accessToken: String): List<InquiryCafeResponse> {
        return try {
            client.get {
                headers { append(HttpHeaders.Authorization, "Bearer $accessToken") }
                url(HttpRoutes.INQUIRY_CAFE)
            }
        } catch (throwable: Throwable) {
            throw throwable.getException()
        }
    }

    override suspend fun getMyInquiryEtcs(accessToken: String): List<InquiryEtcResponse> {
        return try {
            client.get {
                headers { append(HttpHeaders.Authorization, "Bearer $accessToken") }
                url(HttpRoutes.INQUIRY_ETC)
            }
        } catch (throwable: Throwable) {
            throw throwable.getException()
        }
    }

    override suspend fun deleteInquiryCafe(
        accessToken: String, id: Int
    ) {
        return try {
            client.delete {
                headers { append(HttpHeaders.Authorization, "Bearer $accessToken") }
                url(HttpRoutes.deleteInquiryCafe(id))
            }
        } catch (throwable: Throwable) {
            throw throwable.getException()
        }
    }

    override suspend fun deleteInquiryEtc(
        accessToken: String, id: Int
    ) {
        return try {
            client.delete {
                headers { append(HttpHeaders.Authorization, "Bearer $accessToken") }
                url(HttpRoutes.deleteInquiryEtc(id))
            }
        } catch (throwable: Throwable) {
            throw throwable.getException()
        }
    }

    override suspend fun submitPurchaseRequest(
        accessToken: String, purchaseRequest: PurchaseRequest
    ) {
        return try {
            client.post {
                headers { append(HttpHeaders.Authorization, "Bearer $accessToken") }
                url(HttpRoutes.PURCHASE_REQUEST)
                body = purchaseRequest
            }
        } catch (throwable: Throwable) {
            throw throwable.getException()
        }
    }

    override suspend fun deletePurchaseRequest(
        accessToken: String, id: Int
    ) {
        return try {
            client.delete {
                headers { append(HttpHeaders.Authorization, "Bearer $accessToken") }
                url(HttpRoutes.deletePurchaseRequest(id))
            }
        } catch (throwable: Throwable) {
            throw throwable.getException()
        }
    }

    override suspend fun getEvents(): List<Event> {
        return try {
            client.get { url(HttpRoutes.EVENTS) }
        } catch (throwable: Throwable) {
            throw throwable.getException()
        }
    }

    override suspend fun getEventPointHistories(accessToken: String): List<EventPointHistory> {
        return try {
            client.get {
                headers { append(HttpHeaders.Authorization, "Bearer $accessToken") }
                url(HttpRoutes.EVENT_POINT_HISTORIES)
            }
        } catch (throwable: Throwable) {
            throw throwable.getException()
        }
    }

    override suspend fun getFAQs(): List<FAQResponse> {
        return try {
            client.get {
                url(HttpRoutes.FAQ)
            }
        } catch (throwable: Throwable) {
            throw throwable.getException()
        }
    }

    override suspend fun submitInquiryEtc(
        accessToken: String, inquiryEtcRequest: InquiryEtcRequest
    ) {
        try {
            client.post<HttpResponse> {
                headers { append(HttpHeaders.Authorization, "Bearer $accessToken") }
                url(HttpRoutes.INQUIRY_ETC)
                body = inquiryEtcRequest
            }
        } catch (throwable: Throwable) {
            throw throwable.getException()
        }
    }

    override suspend fun submitInquiryCafe(
        accessToken: String, inquiryCafeRequest: InquiryCafeRequest
    ) {
        try {
            client.post<HttpResponse> {
                headers { append(HttpHeaders.Authorization, "Bearer $accessToken") }
                url(HttpRoutes.INQUIRY_CAFE)
                body = inquiryCafeRequest
            }
        } catch (throwable: Throwable) {
            throw throwable.getException()
        }
    }

    override suspend fun submitInquiryCafeAdditionalInfo(
        accessToken: String, inquiryCafeAdditionalInfoRequest: InquiryCafeAdditionalInfoRequest
    ) {
        try {
            client.post<HttpResponse> {
                headers { append(HttpHeaders.Authorization, "Bearer $accessToken") }
                url(HttpRoutes.INQUIRY_CAFE_ADDITIONAL_INFO)
                body = inquiryCafeAdditionalInfoRequest
            }
        } catch (throwable: Throwable) {
            throw throwable.getException()
        }
    }

    override suspend fun getPopUpNotificationList(): List<PopUpNotificationResponse> {
        return try {
            client.get {
                url(HttpRoutes.POP_UP_NOTIFICATION_WITH_CAFE_INFO)
            }
        } catch (throwable: Throwable) {
            throw throwable.getException()
        }
    }

    override suspend fun getOnSaleCafeList(): List<OnSaleCafeResponse> {
        return try {
            client.get {
                url(HttpRoutes.ON_SALE_CAFE)
            }
        } catch (throwable: Throwable) {
            throw throwable.getException()
        }
    }
}