package com.software.cafejariapp.domain.useCase.loginUseCaseImpl

import com.software.cafejariapp.core.CustomException
import com.software.cafejariapp.data.source.remote.dto.SmsAuthRequest
import com.software.cafejariapp.domain.repository.LoginRepository


class ResetSmsAuth(
    private val loginRepository: LoginRepository
) {
    suspend operator fun invoke(
        phoneNumber: String,
        authNumber: String
    ): Triple<String, Int, String> {
        return try {
            val response = loginRepository.resetSmsAuth(SmsAuthRequest(phoneNumber, authNumber))
            Triple(response.token, response.user_id, response.email)
        } catch (e: CustomException) {
            throw e
        }
    }
}

