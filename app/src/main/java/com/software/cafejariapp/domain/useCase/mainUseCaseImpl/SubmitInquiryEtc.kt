package com.software.cafejariapp.domain.useCase.mainUseCaseImpl

import com.software.cafejariapp.core.CustomException
import com.software.cafejariapp.core.TokenExpiredException
import com.software.cafejariapp.data.source.remote.dto.InquiryEtcRequest
import com.software.cafejariapp.domain.repository.MainRepository
import com.software.cafejariapp.domain.entity.AccessToken


class SubmitInquiryEtc(
    private val mainRepository: MainRepository
) {

    suspend operator fun invoke(
        accessToken: AccessToken,
        email: String,
        content: String
    ) {
        try {
            mainRepository.submitInquiryEtc(
                accessToken = accessToken.value,
                inquiryEtcRequest = InquiryEtcRequest(email = email, content = content)
            )
        } catch (e: TokenExpiredException) {
            throw e
        } catch (e: CustomException) {
            throw e
        }
    }
}