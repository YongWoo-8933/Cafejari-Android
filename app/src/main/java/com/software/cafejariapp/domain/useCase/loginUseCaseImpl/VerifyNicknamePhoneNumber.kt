package com.software.cafejariapp.domain.useCase.loginUseCaseImpl

import com.software.cafejariapp.core.CustomException
import com.software.cafejariapp.data.source.remote.dto.PreAuthorizeDto
import com.software.cafejariapp.domain.repository.LoginRepository


class VerifyNicknamePhoneNumber(
    private val loginRepository: LoginRepository
) {
    suspend operator fun invoke(nickname: String, phoneNumber: String): Pair<String, String> {
        return try {
            val response = loginRepository.preAuthorization(PreAuthorizeDto(nickname, phoneNumber))
            Pair(response.nickname, response.phone_number)
        } catch (e: CustomException) {
            throw e
        }
    }
}

