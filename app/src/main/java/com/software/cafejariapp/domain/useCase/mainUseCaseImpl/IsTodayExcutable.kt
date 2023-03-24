package com.software.cafejariapp.domain.useCase.mainUseCaseImpl

import com.software.cafejariapp.domain.repository.MainRepository
import java.time.LocalDate

class IsTodayExecutable(
    private val mainRepository: MainRepository
) {

    suspend operator fun invoke(id: Int): Boolean {
        val today = LocalDate.now()
        val disabledDateList =
            mainRepository.getDisabledDates().filter { it.id == id }
        return if (disabledDateList.isEmpty()) {
            true
        } else {
            val date = disabledDateList.first()
            date.year != today.year || date.month != today.monthValue || date.day != today.dayOfMonth
        }
    }
}