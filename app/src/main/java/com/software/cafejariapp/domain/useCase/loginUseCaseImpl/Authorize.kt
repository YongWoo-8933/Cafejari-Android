package com.software.cafejariapp.domain.useCase.loginUseCaseImpl

import com.software.cafejariapp.core.CustomException
import com.software.cafejariapp.presentation.util.Time
import com.software.cafejariapp.data.source.remote.dto.AuthRequest
import com.software.cafejariapp.data.source.remote.dto.RegisterRequest
import com.software.cafejariapp.domain.repository.LoginRepository
import com.software.cafejariapp.domain.entity.Login
import com.software.cafejariapp.domain.entity.User
import com.software.cafejariapp.domain.repository.TokenRepository
import com.software.cafejariapp.domain.entity.AccessToken
import com.software.cafejariapp.domain.entity.RefreshToken


class Authorize(
    private val loginRepository: LoginRepository,
    private val tokenRepository: TokenRepository,
) {
    suspend operator fun invoke(
        email: String,
        password: String,
        nickname: String,
        phoneNumber: String
    ): Triple<AccessToken, RefreshToken, User> {
        return try {
            val registerResponse = loginRepository.registration(
                RegisterRequest(email = email, password1 = password, password2 = password)
            )
            val authorizeResponse = loginRepository.authorization(
                accessToken = registerResponse.access_token,
                profileId = registerResponse.user.profile.id,
                authRequest = AuthRequest(nickname = nickname, phone_number = phoneNumber)
            )
            val accessToken = AccessToken(
                value = registerResponse.access_token, expiration = Time.getAccessTokenExpiration()
            )
            val refreshToken = RefreshToken(id = 0, value = registerResponse.refresh_token)
            tokenRepository.insertTokenToRoom(refreshToken)
            loginRepository.insertLoginToRoom(
                Login(email = email, password = password)
            )
            Triple(accessToken, refreshToken, authorizeResponse.toUser())
        } catch (e: CustomException) {
            throw e
        }
    }
}

