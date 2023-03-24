package com.software.cafejariapp.domain.useCase.loginUseCaseImpl

import com.software.cafejariapp.core.CustomException
import com.software.cafejariapp.data.source.remote.dto.LogoutRequest
import com.software.cafejariapp.domain.repository.LoginRepository
import com.software.cafejariapp.domain.entity.RefreshToken


class Logout(
    private val loginRepository: LoginRepository
) {
    suspend operator fun invoke(refreshToken: RefreshToken) {
        try {
            loginRepository.logout(LogoutRequest(refreshToken.value))
        } catch (e: CustomException) {
            throw e
        }
    }
}

