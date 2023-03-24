package com.software.cafejariapp.domain.useCase.cafeUseCaseImpl

import com.naver.maps.geometry.LatLng
import com.software.cafejariapp.core.CustomException
import com.software.cafejariapp.core.TokenExpiredException
import com.software.cafejariapp.domain.entity.*
import com.software.cafejariapp.domain.repository.CafeRepository
import com.software.cafejariapp.domain.entity.AccessToken


class GetCafeInfoList(
    private val cafeRepository: CafeRepository
) {
    suspend operator fun invoke(
        accessToken: AccessToken,
        latLng: LatLng,
        zoom: Double
    ): List<CafeInfo> {
        return try {
            val cafeInfoResponseList = cafeRepository.getCafeInfos(
                accessToken = accessToken.value, latLng = latLng, zoomLevel = when {
                    zoom >= 14.0 -> 1
                    zoom >= 12.0 -> 2
                    zoom >= 11.0 -> 4
                    zoom >= 10.0 -> 8
                    else -> 16
                }
            )
            val returnCafeInfoList = mutableListOf<CafeInfo>()
            cafeInfoResponseList.forEach { cafeInfoResponse ->
                val returnCafeList = mutableListOf<Cafe>()
                cafeInfoResponse.cafe.forEach { cafeInfoCafeResponse ->
                    val returnCafeRecentLogList = mutableListOf<CafeRecentLog>()
                    cafeInfoCafeResponse.recent_updated_log.forEach { cafeInfoCafeRecentLogResponse ->
                        returnCafeRecentLogList.add(
                            CafeRecentLog(
                                id = cafeInfoCafeRecentLogResponse.id,
                                master = CafeMaster(
                                    userId = cafeInfoCafeRecentLogResponse.cafe_detail_log.cafe_log.master!!.id,
                                    nickname = cafeInfoCafeRecentLogResponse.cafe_detail_log.cafe_log.master.profile.nickname,
                                    grade = cafeInfoCafeRecentLogResponse.cafe_detail_log.cafe_log.master.profile.grade,
                                ),
                                update = cafeInfoCafeRecentLogResponse.update,
                                crowded = cafeInfoCafeRecentLogResponse.cafe_detail_log.crowded
                            )
                        )
                    }
                    returnCafeList.add(
                        Cafe(
                            id = cafeInfoCafeResponse.id,
                            crowded = if (returnCafeRecentLogList.isEmpty()) -1 else returnCafeRecentLogList[0].crowded,
                            master = CafeMaster(
                                userId = cafeInfoCafeResponse.master?.id ?: 0,
                                nickname = cafeInfoCafeResponse.master?.profile?.nickname ?: "",
                                grade = cafeInfoCafeResponse.master?.profile?.grade ?: 0
                            ),
                            floor = cafeInfoCafeResponse.floor,
                            wallSocket = cafeInfoCafeResponse.wall_socket ?: "",
                            restroom = cafeInfoCafeResponse.restroom ?: "",
                            recentUpdatedLogs = returnCafeRecentLogList
                        )
                    )
                }
                returnCafeInfoList.add(
                    CafeInfo(
                        id = cafeInfoResponse.id,
                        name = cafeInfoResponse.name,
                        city = cafeInfoResponse.city,
                        gu = cafeInfoResponse.gu,
                        address = cafeInfoResponse.address,
                        totalFloor = cafeInfoResponse.total_floor,
                        firstFloor = cafeInfoResponse.floor,
                        latitude = cafeInfoResponse.latitude,
                        longitude = cafeInfoResponse.longitude,
                        googlePlaceId = cafeInfoResponse.google_place_id,
                        cafes = returnCafeList,
                        moreInfo = if (cafeInfoResponse.more_info.isNotEmpty()) cafeInfoResponse.more_info.first() else MoreInfo.empty
                    )
                )
            }
            returnCafeInfoList
        } catch (e: TokenExpiredException) {
            throw e
        } catch (e: CustomException) {
            throw e
        }
    }
}

