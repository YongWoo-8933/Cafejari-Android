package com.software.cafejariapp.domain.useCase.cafeUseCaseImpl

import com.software.cafejariapp.core.CustomException
import com.software.cafejariapp.core.TokenExpiredException
import com.software.cafejariapp.data.source.remote.dto.AddAdPointRequest
import com.software.cafejariapp.domain.entity.User
import com.software.cafejariapp.domain.repository.CafeRepository
import com.software.cafejariapp.domain.entity.AccessToken

class AddAdPoint(
    private val cafeRepository: CafeRepository
) {
    suspend operator fun invoke(accessToken: AccessToken, cafeLogId: Int): User {
        return try {
            cafeRepository.addAdPoint(
                accessToken.value, AddAdPointRequest(cafeLogId)
            ).toUser()
        } catch (e: TokenExpiredException) {
            throw e
        } catch (e: CustomException) {
            throw e
        }
    }
}

