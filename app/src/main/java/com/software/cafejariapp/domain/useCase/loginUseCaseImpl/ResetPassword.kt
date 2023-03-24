package com.software.cafejariapp.domain.useCase.loginUseCaseImpl

import com.software.cafejariapp.core.CustomException
import com.software.cafejariapp.data.source.remote.dto.ResetPasswordRequest
import com.software.cafejariapp.domain.repository.LoginRepository


class ResetPassword(
    private val loginRepository: LoginRepository
) {
    suspend operator fun invoke(
        token: String,
        userId: Int,
        password1: String,
        password2: String
    ) {
        try {
            loginRepository.resetPassword(ResetPasswordRequest(userId, token, password1, password2))
        } catch (e: CustomException) {
            throw e
        }
    }
}

