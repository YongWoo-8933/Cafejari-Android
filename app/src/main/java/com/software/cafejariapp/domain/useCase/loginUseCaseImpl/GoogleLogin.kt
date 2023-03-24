package com.software.cafejariapp.domain.useCase.loginUseCaseImpl

import com.software.cafejariapp.core.CustomException
import com.software.cafejariapp.data.source.remote.dto.GoogleLoginRequest
import com.software.cafejariapp.domain.repository.LoginRepository


class GoogleLogin (
    private val loginRepository: LoginRepository
){
    suspend operator fun invoke(email: String, code: String): Triple<Boolean, String, String> {
        return try{
            val googleLoginResponse = loginRepository.googleLogin(GoogleLoginRequest(email, code))
            Triple( googleLoginResponse.is_user_exist, googleLoginResponse.access_token, googleLoginResponse.code )
        } catch (e: CustomException){
            throw e
        }
    }
}

