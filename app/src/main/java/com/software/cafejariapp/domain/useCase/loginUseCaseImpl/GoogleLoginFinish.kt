package com.software.cafejariapp.domain.useCase.loginUseCaseImpl

import com.software.cafejariapp.core.CustomException
import com.software.cafejariapp.data.source.remote.HttpRoutes
import com.software.cafejariapp.presentation.util.TimeUtil
import com.software.cafejariapp.data.source.remote.dto.SocialLoginRequest
import com.software.cafejariapp.domain.repository.LoginRepository
import com.software.cafejariapp.domain.entity.User
import com.software.cafejariapp.domain.repository.TokenRepository
import com.software.cafejariapp.domain.entity.AccessToken
import com.software.cafejariapp.domain.entity.RefreshToken


class GoogleLoginFinish(
    private val loginRepository: LoginRepository,
    private val tokenRepository: TokenRepository
) {
    suspend operator fun invoke(
        googleAccessToken: String,
        serverCode: String
    ): Triple<AccessToken, RefreshToken, User> {
        try {
            val socialLoginResponse = loginRepository.socialLogin(
                url = HttpRoutes.GOOGLE_LOGIN_FINISH, socialLoginRequest = SocialLoginRequest(
                    access_token = googleAccessToken, code = serverCode
                )
            )
            val accessToken = AccessToken(
                value = socialLoginResponse.access_token,
                expiration = TimeUtil.getAccessTokenExpiration()
            )
            val refreshToken = RefreshToken(id = 0, value = socialLoginResponse.refresh_token)
            tokenRepository.insertTokenToRoom(refreshToken)
            return Triple(accessToken, refreshToken, socialLoginResponse.user.toUser())
        } catch (e: CustomException) {
            throw e
        }
    }
}

