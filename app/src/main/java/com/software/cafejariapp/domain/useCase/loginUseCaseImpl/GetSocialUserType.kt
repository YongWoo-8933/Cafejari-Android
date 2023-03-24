package com.software.cafejariapp.domain.useCase.loginUseCaseImpl

import com.software.cafejariapp.core.CustomException
import com.software.cafejariapp.core.TokenExpiredException
import com.software.cafejariapp.domain.repository.LoginRepository
import com.software.cafejariapp.domain.util.SocialUserType
import com.software.cafejariapp.domain.entity.AccessToken


class GetSocialUserType(
    private val loginRepository: LoginRepository
) {
    suspend operator fun invoke(accessToken: AccessToken): SocialUserType {
        return try {
            when (loginRepository.getSocialUserType(accessToken.value).type) {
                "kakao" -> SocialUserType.Kakao
                "google" -> SocialUserType.Google
                else -> SocialUserType.CafeJari
            }
        } catch (e: TokenExpiredException) {
            throw e
        } catch (e: CustomException) {
            throw e
        }
    }
}

