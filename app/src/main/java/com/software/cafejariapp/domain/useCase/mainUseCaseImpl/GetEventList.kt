package com.software.cafejariapp.domain.useCase.mainUseCaseImpl

import com.software.cafejariapp.core.CustomException
import com.software.cafejariapp.presentation.util.TimeUtil
import com.software.cafejariapp.domain.repository.MainRepository
import com.software.cafejariapp.domain.entity.Event


class GetEventList(
    private val mainRepository: MainRepository
) {

    suspend operator fun invoke(): Pair<List<Event>, List<Event>> {
        return try {
            val events = mainRepository.getEvents()
            when {
                events.isEmpty() -> {
                    Pair(emptyList(), emptyList())
                }
                else -> {
                    val expiredEvents = mutableListOf<Event>()
                    val unExpiredEvents = mutableListOf<Event>()
                    events.forEach { event ->
                        if (TimeUtil.isPast(event.finish)) {
                            expiredEvents.add(event)
                        } else {
                            unExpiredEvents.add(event)
                        }
                    }
                    Pair(unExpiredEvents.toList(), expiredEvents.toList())
                }
            }
        } catch (e: CustomException) {
            throw e
        }
    }
}