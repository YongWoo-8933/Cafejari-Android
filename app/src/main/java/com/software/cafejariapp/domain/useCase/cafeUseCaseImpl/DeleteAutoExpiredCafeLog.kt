package com.software.cafejariapp.domain.useCase.cafeUseCaseImpl

import com.software.cafejariapp.core.CustomException
import com.software.cafejariapp.core.TokenExpiredException
import com.software.cafejariapp.domain.repository.CafeRepository
import com.software.cafejariapp.domain.entity.AccessToken


class DeleteAutoExpiredCafeLog (
    private val cafeRepository: CafeRepository
){
    suspend operator fun invoke(accessToken: AccessToken, autoExpiredCafeLogId: Int) {
        try{
            cafeRepository.deleteAutoExpiredCafeLog(accessToken.value, autoExpiredCafeLogId)
        } catch (e: TokenExpiredException){
            throw e
        } catch (e: CustomException){
            throw e
        }
    }
}

