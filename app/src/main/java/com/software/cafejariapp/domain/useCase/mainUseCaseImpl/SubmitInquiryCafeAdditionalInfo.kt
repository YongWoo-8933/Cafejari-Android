package com.software.cafejariapp.domain.useCase.mainUseCaseImpl

import com.software.cafejariapp.core.CustomException
import com.software.cafejariapp.core.TokenExpiredException
import com.software.cafejariapp.data.source.remote.dto.InquiryCafeAdditionalInfoRequest
import com.software.cafejariapp.domain.repository.MainRepository
import com.software.cafejariapp.domain.entity.AccessToken

class SubmitInquiryCafeAdditionalInfo(
    private val mainRepository: MainRepository
) {

    suspend operator fun invoke(
        accessToken: AccessToken,
        cafeInfoId: Int,
        storeInformation: String,
        openingHour: String,
        wallSocket: String,
        restroom: String,
    ) {
        try {
            mainRepository.submitInquiryCafeAdditionalInfo(
                accessToken = accessToken.value,
                inquiryCafeAdditionalInfoRequest = InquiryCafeAdditionalInfoRequest(
                    cafe_info = cafeInfoId,
                    store_information = storeInformation,
                    opening_hour = openingHour,
                    wall_socket = wallSocket,
                    restroom = restroom
                )
            )
        } catch (e: TokenExpiredException) {
            throw e
        } catch (e: CustomException) {
            throw e
        }
    }
}