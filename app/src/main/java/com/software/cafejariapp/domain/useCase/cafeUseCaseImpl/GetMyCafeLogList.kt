package com.software.cafejariapp.domain.useCase.cafeUseCaseImpl

import com.software.cafejariapp.core.CustomException
import com.software.cafejariapp.core.TokenExpiredException
import com.software.cafejariapp.domain.repository.CafeRepository
import com.software.cafejariapp.domain.entity.CafeLog
import com.software.cafejariapp.domain.entity.AccessToken

class GetMyCafeLogList(
    private val cafeRepository: CafeRepository
) {
    suspend operator fun invoke(accessToken: AccessToken): List<List<CafeLog>> {
        return try {
            val cafeLogResponses = cafeRepository.getMyCafeLogs(accessToken.value)
            val responseList = mutableListOf<List<CafeLog>>()
            var dateCafeLogs = mutableListOf<CafeLog>()
            val lastIndex = cafeLogResponses.lastIndex
            cafeLogResponses.forEachIndexed { index, cafeLogResponse ->
                val cafeLog = cafeLogResponse.toCafeLog()
                if (index != lastIndex) {
                    dateCafeLogs.add(cafeLog)
                    if (cafeLog.start.substring(8..9) != cafeLogResponses[index + 1].start.substring(
                            8..9
                        )
                    ) {
                        responseList.add(dateCafeLogs)
                        dateCafeLogs = mutableListOf()
                    }
                } else {
                    dateCafeLogs.add(cafeLog)
                    responseList.add(dateCafeLogs)
                }
            }
            responseList
        } catch (e: TokenExpiredException) {
            throw e
        } catch (e: CustomException) {
            throw e
        }
    }
}

