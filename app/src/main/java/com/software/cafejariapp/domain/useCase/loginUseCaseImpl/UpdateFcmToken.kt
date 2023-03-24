package com.software.cafejariapp.domain.useCase.loginUseCaseImpl

import com.software.cafejariapp.core.CustomException
import com.software.cafejariapp.core.TokenExpiredException
import com.software.cafejariapp.data.source.remote.dto.UpdateFcmTokenRequest
import com.software.cafejariapp.domain.repository.LoginRepository
import com.software.cafejariapp.domain.entity.User
import com.software.cafejariapp.domain.entity.AccessToken


class UpdateFcmToken(
    private val loginRepository: LoginRepository
) {
    suspend operator fun invoke(
        accessToken: AccessToken,
        profileId: Int,
        fcmToken: String
    ): User {
        return try {
            loginRepository.updateFcmToken(
                accessToken.value, profileId, UpdateFcmTokenRequest(fcmToken)
            ).toUser()
        } catch (e: TokenExpiredException) {
            throw e
        } catch (e: CustomException) {
            throw e
        }
    }
}

