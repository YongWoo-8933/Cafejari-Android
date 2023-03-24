package com.software.cafejariapp.domain.useCase.loginUseCaseImpl

import com.software.cafejariapp.domain.repository.LoginRepository
import com.software.cafejariapp.domain.entity.Login


class GetPredictions(
    private val loginRepository: LoginRepository
) {
    suspend operator fun invoke(email: String): List<Login> {
        val loginList = loginRepository.getLoginsFromRoom()
        return if (email.isNotBlank()) {
            val contains = loginList.filter { it.email.contains(email) }
            val same = contains.filter { it.email == email }
            if (same.isNotEmpty()) emptyList() else contains
        } else {
            emptyList()
        }
    }
}

