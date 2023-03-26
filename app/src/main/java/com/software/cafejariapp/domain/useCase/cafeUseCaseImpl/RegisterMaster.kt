package com.software.cafejariapp.domain.useCase.cafeUseCaseImpl

import com.software.cafejariapp.core.CustomException
import com.software.cafejariapp.core.TokenExpiredException
import com.software.cafejariapp.data.source.remote.dto.RegisterMasterRequest
import com.software.cafejariapp.domain.repository.CafeRepository
import com.software.cafejariapp.domain.entity.CafeLog
import com.software.cafejariapp.domain.entity.AccessToken

class RegisterMaster(
    private val cafeRepository: CafeRepository
) {
    suspend operator fun invoke(
        accessToken: AccessToken,
        cafeId: Int,
        updatePeriod: Int,
        crowded: Int
    ): CafeLog {
        return try {
            cafeRepository.registerMaster(
                accessToken.value,
                RegisterMasterRequest(
                    cafeId,
                    updatePeriod,
                    crowded
                )
            ).toCafeLog()
        } catch (e: TokenExpiredException) {
            throw e
        } catch (e: CustomException) {
            throw e
        }
    }
}

