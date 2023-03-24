package com.software.cafejariapp.domain.useCase.mainUseCaseImpl

import com.software.cafejariapp.core.CustomException
import com.software.cafejariapp.domain.repository.MainRepository
import com.software.cafejariapp.domain.entity.Item


class GetItemList(
    private val mainRepository: MainRepository
) {

    suspend operator fun invoke(): List<Item> {
        return try {
            mainRepository.getItems()
        } catch (e: CustomException) {
            throw e
        }
    }
}