package com.software.cafejariapp.domain.useCase.loginUseCaseImpl

import com.software.cafejariapp.core.CustomException
import com.software.cafejariapp.data.source.remote.dto.RecommendationDto
import com.software.cafejariapp.domain.repository.LoginRepository


class VerifyRecommendationNickname(
    private val loginRepository: LoginRepository
) {
    suspend operator fun invoke(nickname: String): String {
        return try {
            loginRepository.preRecommendation(
                preRecommendationDto = RecommendationDto(nickname = nickname)
            ).nickname
        } catch (e: CustomException) {
            throw e
        }
    }
}

