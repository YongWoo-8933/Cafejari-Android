package com.software.cafejariapp.domain.useCase.mainUseCaseImpl

import com.software.cafejariapp.core.CustomException
import com.software.cafejariapp.core.TokenExpiredException
import com.software.cafejariapp.domain.repository.MainRepository
import com.software.cafejariapp.domain.entity.AccessToken


class DeleteInquiryCafe(
    private val mainRepository: MainRepository
) {

    suspend operator fun invoke(accessToken: AccessToken, inquiryCafeId: Int) {
        try {
            mainRepository.deleteInquiryCafe(accessToken.value, inquiryCafeId)
        } catch (e: TokenExpiredException) {
            throw e
        } catch (e: CustomException) {
            throw e
        }
    }
}