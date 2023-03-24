package com.software.cafejariapp.domain.useCase.loginUseCaseImpl

import com.software.cafejariapp.core.CustomException
import com.software.cafejariapp.core.TokenExpiredException
import com.software.cafejariapp.data.source.remote.dto.SmsSendRequest
import com.software.cafejariapp.domain.repository.LoginRepository


class SmsSend(
    private val loginRepository: LoginRepository
) {
    suspend operator fun invoke(phoneNumber: String) {
        try {
            loginRepository.smsSend(SmsSendRequest(phoneNumber))
        } catch (e: TokenExpiredException) {
            throw e
        } catch (e: CustomException) {
            throw e
        }
    }
}

