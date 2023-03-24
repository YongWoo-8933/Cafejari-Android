package com.software.cafejariapp.domain.repository

import com.naver.maps.geometry.LatLng
import com.software.cafejariapp.data.source.remote.dto.*

interface CafeRepository {

    suspend fun getCafeInfos(
        accessToken: String,
        latLng: LatLng,
        zoomLevel: Int
    ): List<CafeInfoResponse>

    suspend fun registerMaster(
        accessToken: String,
        registerMasterRequest: RegisterMasterRequest
    ): CafeLogResponse

    suspend fun updateCrowded(
        accessToken: String,
        updateCrowdedRequest: UpdateCrowdedRequest
    ): CafeLogResponse

    suspend fun expireMaster(
        accessToken: String,
        expireMasterRequest: ExpireMasterRequest
    ): CafeLogResponse

    suspend fun addAdPoint(
        accessToken: String,
        addAdPointRequest:
        AddAdPointRequest
    ): UserResponse

    suspend fun getMyCafeLogs(accessToken: String): List<CafeLogResponse>

    suspend fun getMyUnExpiredCafeLog(accessToken: String, expired: Boolean): List<CafeLogResponse>

    suspend fun deleteCafeDetailLog(accessToken: String, id: Int): CafeLogResponse

    suspend fun getAutoExpiredCafeLog(accessToken: String): AutoExpiredLogResponse

    suspend fun deleteAutoExpiredCafeLog(accessToken: String, id: Int)

    suspend fun requestThumbsUp(accessToken: String, thumbsUpRequest: ThumbsUpRequest): UserResponse

    suspend fun searchCafe(query: String): List<CafeInfoResponse>
}