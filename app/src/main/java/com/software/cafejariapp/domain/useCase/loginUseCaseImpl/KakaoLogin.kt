package com.software.cafejariapp.domain.useCase.loginUseCaseImpl

import com.software.cafejariapp.core.CustomException
import com.software.cafejariapp.data.source.remote.dto.KakaoLoginRequest
import com.software.cafejariapp.domain.repository.LoginRepository


class KakaoLogin(
    private val loginRepository: LoginRepository
) {
    suspend operator fun invoke(kakaoAccessToken: String): Pair<Boolean, String> {
        return try {
            val kakaoLoginResponse = loginRepository.kakaoLogin(KakaoLoginRequest(kakaoAccessToken))
            Pair(kakaoLoginResponse.is_user_exist, kakaoAccessToken)
        } catch (e: CustomException) {
            throw e
        }
    }
}

