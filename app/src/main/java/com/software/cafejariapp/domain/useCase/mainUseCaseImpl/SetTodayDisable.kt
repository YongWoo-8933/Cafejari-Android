package com.software.cafejariapp.domain.useCase.mainUseCaseImpl

import com.software.cafejariapp.domain.repository.MainRepository
import com.software.cafejariapp.domain.entity.DisableDate
import java.time.LocalDate

class SetTodayDisable(
    private val mainRepository: MainRepository
) {

    suspend operator fun invoke(id: Int) {
        val today = LocalDate.now()
        mainRepository.insertDisabledDate(
            DisableDate(
                id = id,
                year = today.year,
                month = today.monthValue,
                day = today.dayOfMonth
            )
        )
    }
}