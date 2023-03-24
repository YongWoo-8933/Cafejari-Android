package com.software.cafejariapp.domain.useCase.mainUseCaseImpl

import com.software.cafejariapp.domain.repository.MainRepository
import com.software.cafejariapp.domain.util.DisableDateId

class IsOnboardingWatched(
    private val mainRepository: MainRepository
) {

    suspend operator fun invoke(): Boolean {
        return !mainRepository.getDisabledDates().none { it.id == DisableDateId.onBoarding }
    }
}