package com.software.cafejariapp.domain.useCase.cafeUseCaseImpl

import com.software.cafejariapp.core.CustomException
import com.software.cafejariapp.core.TokenExpiredException
import com.software.cafejariapp.data.source.remote.dto.ThumbsUpRequest
import com.software.cafejariapp.domain.entity.User
import com.software.cafejariapp.domain.repository.CafeRepository
import com.software.cafejariapp.domain.entity.AccessToken

class RequestThumbsUp(
    private val cafeRepository: CafeRepository
) {
    suspend operator fun invoke(
        accessToken: AccessToken, recentLogId: Int, isAdThumbsUp: Boolean
    ): User {
        return try {
            cafeRepository.requestThumbsUp(
                accessToken.value, ThumbsUpRequest(recentLogId, isAdThumbsUp)
            ).toUser()
        } catch (e: TokenExpiredException) {
            throw e
        } catch (e: CustomException) {
            throw e
        }
    }
}

