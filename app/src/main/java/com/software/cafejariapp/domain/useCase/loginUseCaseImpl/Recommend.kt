package com.software.cafejariapp.domain.useCase.loginUseCaseImpl

import com.software.cafejariapp.core.CustomException
import com.software.cafejariapp.core.TokenExpiredException
import com.software.cafejariapp.data.source.remote.dto.RecommendationDto
import com.software.cafejariapp.domain.repository.LoginRepository
import com.software.cafejariapp.domain.entity.User
import com.software.cafejariapp.domain.entity.AccessToken


class Recommend(
    private val loginRepository: LoginRepository
) {
    suspend operator fun invoke(accessToken: AccessToken, nickname: String): User {
        return try {
            loginRepository.recommendation(
                accessToken = accessToken.value,
                recommendationDto = RecommendationDto(nickname = nickname)
            ).toUser()
        } catch (e: TokenExpiredException) {
            throw e
        } catch (e: CustomException) {
            throw e
        }
    }
}

