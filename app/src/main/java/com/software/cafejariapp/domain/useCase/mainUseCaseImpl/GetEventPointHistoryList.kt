package com.software.cafejariapp.domain.useCase.mainUseCaseImpl

import com.software.cafejariapp.core.CustomException
import com.software.cafejariapp.domain.repository.MainRepository
import com.software.cafejariapp.domain.entity.EventPointHistory
import com.software.cafejariapp.domain.entity.AccessToken


class GetEventPointHistoryList(
    private val mainRepository: MainRepository
) {

    suspend operator fun invoke(accessToken: AccessToken): List<EventPointHistory> {
        return try {
            mainRepository.getEventPointHistories(accessToken.value)
        } catch (e: CustomException) {
            throw e
        }
    }
}