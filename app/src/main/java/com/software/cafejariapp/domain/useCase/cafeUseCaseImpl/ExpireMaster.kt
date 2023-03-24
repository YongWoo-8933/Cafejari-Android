package com.software.cafejariapp.domain.useCase.cafeUseCaseImpl

import com.software.cafejariapp.core.CustomException
import com.software.cafejariapp.core.MasterExpiredException
import com.software.cafejariapp.core.TokenExpiredException
import com.software.cafejariapp.data.source.remote.dto.ExpireMasterRequest
import com.software.cafejariapp.domain.repository.CafeRepository
import com.software.cafejariapp.domain.entity.CafeLog
import com.software.cafejariapp.domain.entity.AccessToken

class ExpireMaster (
    private val cafeRepository: CafeRepository
){
    suspend operator fun invoke(accessToken: AccessToken, cafeLogId: Int, adMultipleApplied: Boolean): CafeLog {
        return try{
            cafeRepository.expireMaster(
                accessToken.value, ExpireMasterRequest(cafeLogId, adMultipleApplied)
            ).toCafeLog()
        } catch (e: TokenExpiredException){
            throw e
        } catch (e: MasterExpiredException){
            throw e
        } catch (e: CustomException){
            throw e
        }
    }
}

