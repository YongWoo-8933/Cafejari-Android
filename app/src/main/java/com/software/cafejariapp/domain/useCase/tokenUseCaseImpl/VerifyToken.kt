package com.software.cafejariapp.domain.useCase.tokenUseCaseImpl

import com.software.cafejariapp.core.CustomException
import com.software.cafejariapp.data.source.remote.dto.TokenVerifyRequest
import com.software.cafejariapp.domain.repository.TokenRepository
import com.software.cafejariapp.domain.entity.RefreshToken

class VerifyToken(
    private val tokenRepository: TokenRepository
) {

    suspend operator fun invoke(refreshToken: RefreshToken): Boolean {
        try {
            return tokenRepository.getTokenValidity(TokenVerifyRequest(refreshToken.value))
        } catch (e: CustomException) {
            throw e
        }
    }
}