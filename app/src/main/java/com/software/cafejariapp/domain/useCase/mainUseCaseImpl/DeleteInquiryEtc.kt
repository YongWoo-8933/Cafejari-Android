package com.software.cafejariapp.domain.useCase.mainUseCaseImpl

import com.software.cafejariapp.core.CustomException
import com.software.cafejariapp.core.TokenExpiredException
import com.software.cafejariapp.domain.repository.MainRepository
import com.software.cafejariapp.domain.entity.AccessToken


class DeleteInquiryEtc(
    private val mainRepository: MainRepository
) {

    suspend operator fun invoke(accessToken: AccessToken, inquiryEtcId: Int) {
        try {
            mainRepository.deleteInquiryEtc(accessToken.value, inquiryEtcId)
        } catch (e: TokenExpiredException) {
            throw e
        } catch (e: CustomException) {
            throw e
        }
    }
}