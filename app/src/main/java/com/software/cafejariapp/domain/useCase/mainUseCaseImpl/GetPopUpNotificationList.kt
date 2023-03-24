package com.software.cafejariapp.domain.useCase.mainUseCaseImpl

import com.software.cafejariapp.core.CustomException
import com.software.cafejariapp.domain.repository.MainRepository
import com.software.cafejariapp.domain.entity.PopUpNotification
import com.software.cafejariapp.domain.entity.CafeInfo


class GetPopUpNotificationList(
    private val mainRepository: MainRepository,
) {
    suspend operator fun invoke(): List<PopUpNotification> {
        return try {
            val popUpNotificationResponseList =
                mainRepository.getPopUpNotificationList().sortedBy { it.order }
            val response = mutableListOf<PopUpNotification>()
            popUpNotificationResponseList.forEach { popUpNotificationResponse ->
                response.add(
                    PopUpNotification(
                        order = popUpNotificationResponse.order,
                        url = popUpNotificationResponse.url,
                        image = popUpNotificationResponse.image,
                        cafeInfo = if (popUpNotificationResponse.cafe_info == null) {
                            CafeInfo.empty
                        } else {
                            CafeInfo.empty.copy(
                                id = popUpNotificationResponse.cafe_info.id,
                                latitude = popUpNotificationResponse.cafe_info.latitude,
                                longitude = popUpNotificationResponse.cafe_info.longitude
                            )
                        }
                    )
                )
            }
            response
        } catch (e: CustomException) {
            throw e
        }
    }
}