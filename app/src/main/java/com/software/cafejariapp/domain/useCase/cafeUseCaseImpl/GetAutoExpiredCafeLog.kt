package com.software.cafejariapp.domain.useCase.cafeUseCaseImpl

import com.software.cafejariapp.core.CustomException
import com.software.cafejariapp.core.TokenExpiredException
import com.software.cafejariapp.domain.repository.CafeRepository
import com.software.cafejariapp.domain.entity.AutoExpiredCafeLog
import com.software.cafejariapp.domain.entity.AccessToken

class GetAutoExpiredCafeLog(
    private val cafeRepository: CafeRepository
) {
    suspend operator fun invoke(accessToken: AccessToken): AutoExpiredCafeLog {
        return try {
            val autoExpiredCafeLogResponse = cafeRepository.getAutoExpiredCafeLog(accessToken.value)
            AutoExpiredCafeLog(
                id = autoExpiredCafeLogResponse.id,
                cafeLogId = autoExpiredCafeLogResponse.cafe_log.id,
                cafeId = autoExpiredCafeLogResponse.cafe_log.cafe.id,
                time = autoExpiredCafeLogResponse.cafe_log.finish,
                name = autoExpiredCafeLogResponse.cafe_log.cafe.cafe_info.name,
                floor = autoExpiredCafeLogResponse.cafe_log.cafe.floor,
                start = autoExpiredCafeLogResponse.cafe_log.start,
                finish = autoExpiredCafeLogResponse.cafe_log.finish,
                latitude = autoExpiredCafeLogResponse.cafe_log.cafe.cafe_info.latitude,
                longitude = autoExpiredCafeLogResponse.cafe_log.cafe.cafe_info.longitude,
                point = autoExpiredCafeLogResponse.cafe_log.point,
            )
        } catch (e: TokenExpiredException) {
            throw e
        } catch (e: CustomException) {
            throw e
        }
    }
}

