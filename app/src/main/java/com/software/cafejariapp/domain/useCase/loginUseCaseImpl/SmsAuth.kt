package com.software.cafejariapp.domain.useCase.loginUseCaseImpl

import com.software.cafejariapp.core.CustomException
import com.software.cafejariapp.core.TokenExpiredException
import com.software.cafejariapp.data.source.remote.dto.SmsAuthRequest
import com.software.cafejariapp.domain.repository.LoginRepository


class SmsAuth(
    private val loginRepository: LoginRepository
) {
    suspend operator fun invoke(phoneNumber: String, authNumber: String) {
        try {
            loginRepository.smsAuth(SmsAuthRequest(phoneNumber, authNumber))
        } catch (e: TokenExpiredException) {
            throw e
        } catch (e: CustomException) {
            throw e
        }
    }
}

