package com.software.cafejariapp.domain.useCase.cafeUseCaseImpl

import android.location.Location
import com.software.cafejariapp.core.CustomException
import com.software.cafejariapp.core.MasterExpiredException
import com.software.cafejariapp.core.TokenExpiredException
import com.software.cafejariapp.core.getDistance
import com.software.cafejariapp.domain.repository.CafeRepository
import com.software.cafejariapp.domain.entity.CafeInfo
import com.software.cafejariapp.domain.entity.MoreInfo

class Search(
    private val cafeRepository: CafeRepository
) {
    suspend operator fun invoke(query: String, userLocation: Location?): List<CafeInfo> {
        return try {
            val cafeInfoResponses = cafeRepository.searchCafe(query)
            val cafeInfos = mutableListOf<CafeInfo>()
            cafeInfoResponses.forEach { cafeInfoResponse ->
                cafeInfos.add(
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
                        cafes = emptyList(),
                        moreInfo = MoreInfo.empty
                    )
                )
            }
            if (userLocation != null) {
                cafeInfos.sortedBy { cafeInfo ->
                    userLocation.getDistance(cafeInfo.latitude, cafeInfo.longitude)
                }
            } else {
                cafeInfos
            }
        } catch (e: TokenExpiredException) {
            throw e
        } catch (e: MasterExpiredException) {
            throw e
        } catch (e: CustomException) {
            throw e
        }
    }
}

