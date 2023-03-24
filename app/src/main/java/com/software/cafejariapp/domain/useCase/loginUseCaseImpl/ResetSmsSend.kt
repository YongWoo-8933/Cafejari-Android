package com.software.cafejariapp.domain.useCase.loginUseCaseImpl

import com.software.cafejariapp.core.CustomException
import com.software.cafejariapp.data.source.remote.dto.SmsSendRequest
import com.software.cafejariapp.domain.repository.LoginRepository


class ResetSmsSend(
    private val loginRepository: LoginRepository
) {
    suspend operator fun invoke(
        phoneNumber: String
    ) {
        try {
            loginRepository.resetSmsSend(SmsSendRequest(phoneNumber))
        } catch (e: CustomException) {
            throw e
        }
    }
}

