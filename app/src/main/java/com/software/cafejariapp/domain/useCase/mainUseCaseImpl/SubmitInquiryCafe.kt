package com.software.cafejariapp.domain.useCase.mainUseCaseImpl

import com.software.cafejariapp.core.CustomException
import com.software.cafejariapp.core.TokenExpiredException
import com.software.cafejariapp.data.source.remote.dto.InquiryCafeRequest
import com.software.cafejariapp.domain.repository.MainRepository
import com.software.cafejariapp.domain.entity.AccessToken

class SubmitInquiryCafe(
    private val mainRepository: MainRepository
) {

    suspend operator fun invoke(
        accessToken: AccessToken,
        email: String,
        cafeName: String,
        cafeAddress: String,
    ) {
        try {
            mainRepository.submitInquiryCafe(
                accessToken = accessToken.value,
                inquiryCafeRequest = InquiryCafeRequest(
                    email = email, cafe_name = cafeName, cafe_address = cafeAddress
                )
            )
        } catch (e: TokenExpiredException) {
            throw e
        } catch (e: CustomException) {
            throw e
        }
    }
}