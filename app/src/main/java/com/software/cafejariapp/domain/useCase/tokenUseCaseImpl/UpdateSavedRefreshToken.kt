package com.software.cafejariapp.domain.useCase.tokenUseCaseImpl

import com.software.cafejariapp.domain.repository.TokenRepository
import com.software.cafejariapp.domain.entity.RefreshToken

class UpdateSavedRefreshToken(
    private val tokenRepository: TokenRepository
) {

    suspend operator fun invoke(refreshToken: RefreshToken) {
        return tokenRepository.insertTokenToRoom(refreshToken)
    }
}