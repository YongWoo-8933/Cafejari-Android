package com.software.cafejariapp.domain.useCase.mainUseCaseImpl

import com.software.cafejariapp.core.CustomException
import com.software.cafejariapp.core.TokenExpiredException
import com.software.cafejariapp.data.source.remote.dto.PurchaseRequest
import com.software.cafejariapp.domain.repository.MainRepository
import com.software.cafejariapp.domain.entity.AccessToken

class SubmitPurchaseRequest (
    private val mainRepository: MainRepository
){
    
    suspend operator fun invoke(
        accessToken: AccessToken,
        itemId: Int,
    ){
        try {
            mainRepository.submitPurchaseRequest(
                accessToken.value,
                PurchaseRequest(itemId)
            )
        } catch (e: TokenExpiredException){
            throw e
        } catch (e: CustomException) {
            throw e
        }
    }
}