package com.software.cafejariapp.domain.useCase.mainUseCaseImpl

import com.software.cafejariapp.core.CustomException
import com.software.cafejariapp.domain.repository.MainRepository
import com.software.cafejariapp.domain.entity.Version

class GetVersion(
    private val mainRepository: MainRepository
) {

    suspend operator fun invoke(): Version {
        return try {
            val resList = mainRepository.getVersion()
            if (resList.isEmpty()) Version.empty else resList[0]
        } catch (e: CustomException) {
            throw e
        }
    }
}