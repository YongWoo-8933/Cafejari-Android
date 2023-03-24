package com.software.cafejariapp.domain.useCase.mainUseCaseImpl

import com.software.cafejariapp.core.CustomException
import com.software.cafejariapp.domain.repository.MainRepository
import com.software.cafejariapp.domain.entity.OnSaleCafe


class GetOnSaleCafeList(
    private val mainRepository: MainRepository,
) {
    suspend operator fun invoke(): List<OnSaleCafe> {
        return try {
            val onSaleCafeResponseList = mainRepository.getOnSaleCafeList().sortedBy { it.order }
            val response = mutableListOf<OnSaleCafe>()
            onSaleCafeResponseList.forEach { onSaleCafeResponse ->
                response.add(
                    OnSaleCafe(
                        order = onSaleCafeResponse.order,
                        saleContent = onSaleCafeResponse.content,
                        image = onSaleCafeResponse.image,
                        cafeInfoId = onSaleCafeResponse.cafe_info.id,
                        name = onSaleCafeResponse.cafe_info.name,
                        city = onSaleCafeResponse.cafe_info.city,
                        gu = onSaleCafeResponse.cafe_info.gu,
                        address = onSaleCafeResponse.cafe_info.address,
                        latitude = onSaleCafeResponse.cafe_info.latitude,
                        longitude = onSaleCafeResponse.cafe_info.longitude
                    )
                )
            }
            response
        } catch (e: CustomException) {
            throw e
        }
    }
}