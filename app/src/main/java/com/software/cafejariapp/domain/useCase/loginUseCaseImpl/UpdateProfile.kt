package com.software.cafejariapp.domain.useCase.loginUseCaseImpl

import com.software.cafejariapp.core.CustomException
import com.software.cafejariapp.data.util.error.ErrorType
import com.software.cafejariapp.core.TokenExpiredException
import com.software.cafejariapp.domain.repository.LoginRepository
import com.software.cafejariapp.domain.entity.User
import com.software.cafejariapp.domain.entity.AccessToken


class UpdateProfile (
    private val loginRepository: LoginRepository
){
    suspend operator fun invoke(
        accessToken: AccessToken,
        imagePath: String? = null,
        nickname: String? = null,
        profileId: Int
    ): User {
        if(imagePath == null && nickname == null) {
            throw CustomException(ErrorType.ErrorMessage("변경사항이 없습니다"))
        }else{
            return try{
                loginRepository.updateProfile(
                    accessToken.value, profileId, imagePath, nickname
                ).toUser()
            } catch (e: TokenExpiredException){
                throw e
            } catch (e: CustomException){
                throw e
            }
        }
    }
}

