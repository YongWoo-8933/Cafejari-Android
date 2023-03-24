package com.software.cafejariapp.domain.useCase.tokenUseCaseImpl

import com.software.cafejariapp.domain.repository.TokenRepository
import com.software.cafejariapp.domain.entity.RefreshToken


class GetSavedRefreshToken(
    private val tokenRepository: TokenRepository
) {

    suspend operator fun invoke(): RefreshToken {
        return tokenRepository.getTokenFromRoom()
    }
}