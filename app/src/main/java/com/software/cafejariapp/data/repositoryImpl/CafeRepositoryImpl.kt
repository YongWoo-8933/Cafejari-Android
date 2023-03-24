package com.software.cafejariapp.data.repositoryImpl

import com.naver.maps.geometry.LatLng
import com.software.cafejariapp.data.source.remote.HttpRoutes
import com.software.cafejariapp.core.getException
import com.software.cafejariapp.data.source.remote.dto.*
import com.software.cafejariapp.domain.repository.CafeRepository
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

class CafeRepositoryImpl(
    private val client: HttpClient
) : CafeRepository {

    override suspend fun getCafeInfos(
        accessToken: String,
        latLng: LatLng,
        zoomLevel: Int
    ): List<CafeInfoResponse> {
        return try {
            client.get {
                headers { append(HttpHeaders.Authorization, "Bearer $accessToken") }
                url(HttpRoutes.nearbyCafeInfos(latLng, zoomLevel))
            }
        } catch (throwable: Throwable) {
            throw throwable.getException()
        }
    }

    override suspend fun registerMaster(
        accessToken: String, registerMasterRequest: RegisterMasterRequest
    ): CafeLogResponse {
        return try {
            client.post {
                headers { append(HttpHeaders.Authorization, "Bearer $accessToken") }
                url(HttpRoutes.MASTER_REGISTRATION)
                body = registerMasterRequest
            }
        } catch (throwable: Throwable) {
            throw throwable.getException()
        }
    }

    override suspend fun updateCrowded(
        accessToken: String, updateCrowdedRequest: UpdateCrowdedRequest
    ): CafeLogResponse {
        return try {
            client.put {
                headers { append(HttpHeaders.Authorization, "Bearer $accessToken") }
                url(HttpRoutes.CROWDED_UPDATE)
                body = updateCrowdedRequest
            }
        } catch (throwable: Throwable) {
            throw throwable.getException()
        }
    }

    override suspend fun expireMaster(
        accessToken: String, expireMasterRequest: ExpireMasterRequest
    ): CafeLogResponse {
        return try {
            client.post {
                headers { append(HttpHeaders.Authorization, "Bearer $accessToken") }
                url(HttpRoutes.MASTER_EXPIRATION)
                body = expireMasterRequest
            }
        } catch (throwable: Throwable) {
            throw throwable.getException()
        }
    }

    override suspend fun addAdPoint(
        accessToken: String, addAdPointRequest: AddAdPointRequest
    ): UserResponse {
        return try {
            client.post {
                headers { append(HttpHeaders.Authorization, "Bearer $accessToken") }
                url(HttpRoutes.AD_POINT)
                body = addAdPointRequest
            }
        } catch (throwable: Throwable) {
            throw throwable.getException()
        }
    }

    override suspend fun getMyUnExpiredCafeLog(
        accessToken: String, expired: Boolean
    ): List<CafeLogResponse> {
        return try {
            client.get {
                headers { append(HttpHeaders.Authorization, "Bearer $accessToken") }
                url(HttpRoutes.getMyUnExpiredCafeLog(expired))
            }
        } catch (throwable: Throwable) {
            throw throwable.getException()
        }
    }

    override suspend fun getMyCafeLogs(accessToken: String): List<CafeLogResponse> {
        return try {
            client.get {
                headers { append(HttpHeaders.Authorization, "Bearer $accessToken") }
                url(HttpRoutes.GET_MY_CAFE_LOGS)
            }
        } catch (throwable: Throwable) {
            throw throwable.getException()
        }
    }

    override suspend fun deleteCafeDetailLog(accessToken: String, id: Int): CafeLogResponse {
        return try {
            client.delete {
                headers { append(HttpHeaders.Authorization, "Bearer $accessToken") }
                url(HttpRoutes.deleteCafeDetailLog(id))
            }
        } catch (throwable: Throwable) {
            throw throwable.getException()
        }
    }

    override suspend fun getAutoExpiredCafeLog(accessToken: String): AutoExpiredLogResponse {
        return try {
            client.get {
                headers { append(HttpHeaders.Authorization, "Bearer $accessToken") }
                url(HttpRoutes.AUTO_EXPIRED_CAFE_LOG)
            }
        } catch (throwable: Throwable) {
            throw throwable.getException()
        }
    }

    override suspend fun deleteAutoExpiredCafeLog(accessToken: String, id: Int) {
        try {
            client.delete<HttpResponse> {
                headers { append(HttpHeaders.Authorization, "Bearer $accessToken") }
                url(HttpRoutes.deleteAutoExpiredCafeLog(id))
            }
        } catch (throwable: Throwable) {
            throw throwable.getException()
        }
    }

    override suspend fun requestThumbsUp(
        accessToken: String, thumbsUpRequest: ThumbsUpRequest
    ): UserResponse {
        return try {
            client.post {
                headers { append(HttpHeaders.Authorization, "Bearer $accessToken") }
                url(HttpRoutes.THUMBS_UP)
                body = thumbsUpRequest
            }
        } catch (throwable: Throwable) {
            throw throwable.getException()
        }
    }

    override suspend fun searchCafe(query: String): List<CafeInfoResponse> {
        return try {
            client.get {
                url(HttpRoutes.search(query))
            }
        } catch (throwable: Throwable) {
            throw throwable.getException()
        }
    }
}