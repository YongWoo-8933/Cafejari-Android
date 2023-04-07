package com.software.cafejariapp.domain.useCase.loginUseCaseImpl

import com.software.cafejariapp.core.CustomException
import com.software.cafejariapp.presentation.util.TimeUtil
import com.software.cafejariapp.data.source.remote.dto.CafejariLoginRequest
import com.software.cafejariapp.domain.repository.LoginRepository
import com.software.cafejariapp.domain.entity.Login
import com.software.cafejariapp.domain.entity.User
import com.software.cafejariapp.domain.entity.AccessToken
import com.software.cafejariapp.domain.entity.RefreshToken
import com.software.cafejariapp.domain.repository.TokenRepository


class CafeJariLogin(
    private val loginRepository: LoginRepository,
    private val tokenRepository: TokenRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String
    ): Triple<AccessToken, RefreshToken, User> {
        try {
            val response =
                loginRepository.login(CafejariLoginRequest(email = email, password = password))
            val accessToken = AccessToken(
                value = response.access_token, expiration = TimeUtil.getAccessTokenExpiration()
            )
            val refreshToken = RefreshToken(id = 0, value = response.refresh_token)

            tokenRepository.insertTokenToRoom(refreshToken)
            loginRepository.insertLoginToRoom(
                Login(email = email, password = password)
            )
            return Triple(accessToken, refreshToken, response.user.toUser())
        } catch (e: CustomException) {
            throw e
        }
    }
}

