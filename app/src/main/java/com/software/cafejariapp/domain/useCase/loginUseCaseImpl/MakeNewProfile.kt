package com.software.cafejariapp.domain.useCase.loginUseCaseImpl

import com.software.cafejariapp.core.CustomException
import com.software.cafejariapp.data.source.remote.dto.MakeNewProfileRequest
import com.software.cafejariapp.domain.repository.LoginRepository
import com.software.cafejariapp.domain.entity.User
import com.software.cafejariapp.domain.entity.AccessToken


class MakeNewProfile(
    private val loginRepository: LoginRepository
) {
    suspend operator fun invoke(
        accessToken: AccessToken,
        userId: Int,
        nickname: String,
        phoneNumber: String,
        fcmToken: String
    ): User {
        return try {
            loginRepository.makeNewProfile(
                accessToken = accessToken.value, makeNewProfileRequest = MakeNewProfileRequest(
                    user = userId,
                    nickname = nickname,
                    phone_number = phoneNumber,
                    fcm_token = fcmToken
                )
            ).toUser()
        } catch (e: CustomException) {
            throw e
        }
    }
}

