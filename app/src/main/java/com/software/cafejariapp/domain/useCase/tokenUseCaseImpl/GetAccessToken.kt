package com.software.cafejariapp.domain.useCase.tokenUseCaseImpl

import com.software.cafejariapp.core.CustomException
import com.software.cafejariapp.data.source.remote.dto.TokenRefreshRequest
import com.software.cafejariapp.domain.repository.TokenRepository
import com.software.cafejariapp.domain.entity.AccessToken
import com.software.cafejariapp.domain.entity.RefreshToken


class GetAccessToken(
    private val tokenRepository: TokenRepository
) {
    suspend operator fun invoke(refreshToken: RefreshToken): AccessToken {
        return try {
            val response =
                tokenRepository.getAccessTokenFromServer(TokenRefreshRequest(refreshToken.value))
            AccessToken(value = response.access, expiration = response.access_token_expiration)
        } catch (e: CustomException) {
            throw e
        }
    }
}

