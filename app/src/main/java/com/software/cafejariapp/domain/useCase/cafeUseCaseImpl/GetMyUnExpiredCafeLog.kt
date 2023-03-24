package com.software.cafejariapp.domain.useCase.cafeUseCaseImpl

import com.software.cafejariapp.core.CustomException
import com.software.cafejariapp.core.TokenExpiredException
import com.software.cafejariapp.domain.repository.CafeRepository
import com.software.cafejariapp.domain.entity.CafeLog
import com.software.cafejariapp.domain.entity.AccessToken


class GetMyUnExpiredCafeLog(
    private val cafeRepository: CafeRepository
) {
    suspend operator fun invoke(accessToken: AccessToken): CafeLog {
        return try {
            val cafeLogs = cafeRepository.getMyUnExpiredCafeLog(accessToken.value, false)
            if (cafeLogs.isEmpty()) CafeLog.empty else cafeLogs[0].toCafeLog()
        } catch (e: TokenExpiredException) {
            throw e
        } catch (e: CustomException) {
            throw e
        }
    }
}

