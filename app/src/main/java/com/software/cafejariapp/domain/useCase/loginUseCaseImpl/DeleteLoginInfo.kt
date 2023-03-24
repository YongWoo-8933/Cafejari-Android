package com.software.cafejariapp.domain.useCase.loginUseCaseImpl

import com.software.cafejariapp.domain.repository.LoginRepository
import com.software.cafejariapp.domain.entity.Login


class DeleteLoginInfo(
    private val loginRepository: LoginRepository
) {
    suspend operator fun invoke(login: Login) {
        loginRepository.deleteLoginFromRoom(login)
    }
}

