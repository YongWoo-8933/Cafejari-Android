package com.software.cafejariapp.domain.useCase.loginUseCaseImpl

import com.software.cafejariapp.core.CustomException
import com.software.cafejariapp.data.source.remote.dto.RegisterRequest
import com.software.cafejariapp.domain.repository.LoginRepository


class VerifyEmailPassword(
    private val loginRepository: LoginRepository
) {
    suspend operator fun invoke(
        email: String,
        password1: String,
        password2: String
    ): Pair<String, String> {
        return try {
            val response =
                loginRepository.preRegistration(RegisterRequest(email, password1, password2))
            Pair(response.email, response.password)
        } catch (e: CustomException) {
            throw e
        }
    }
}

