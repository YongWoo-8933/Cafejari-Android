package com.software.cafejariapp.domain.useCase.mainUseCaseImpl

import com.software.cafejariapp.core.CustomException
import com.software.cafejariapp.core.TokenExpiredException
import com.software.cafejariapp.domain.repository.MainRepository
import com.software.cafejariapp.domain.entity.InquiryCafe
import com.software.cafejariapp.domain.entity.AccessToken


class GetMyInquiryCafes(
    private val mainRepository: MainRepository
) {

    suspend operator fun invoke(accessToken: AccessToken): List<InquiryCafe> {
        return try {
            val myInquiryCafeResponseList = mainRepository.getMyInquiryCafes(accessToken.value)
            if (myInquiryCafeResponseList.isEmpty()) {
                emptyList()
            } else {
                val res = mutableListOf<InquiryCafe>()
                myInquiryCafeResponseList.forEach { inquiryCafeResponse ->
                    res.add(
                        InquiryCafe(
                            id = inquiryCafeResponse.id,
                            name = inquiryCafeResponse.cafe_name,
                            address = inquiryCafeResponse.cafe_address,
                            requestDate = inquiryCafeResponse.time,
                            result = inquiryCafeResponse.result ?: ""
                        )
                    )
                }
                res
            }
        } catch (e: TokenExpiredException) {
            throw e
        } catch (e: CustomException) {
            throw e
        }
    }
}