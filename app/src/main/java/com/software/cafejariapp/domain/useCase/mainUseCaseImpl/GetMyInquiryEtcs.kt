package com.software.cafejariapp.domain.useCase.mainUseCaseImpl

import com.software.cafejariapp.core.CustomException
import com.software.cafejariapp.core.TokenExpiredException
import com.software.cafejariapp.domain.repository.MainRepository
import com.software.cafejariapp.domain.entity.InquiryEtc
import com.software.cafejariapp.domain.entity.AccessToken


class GetMyInquiryEtcs(
    private val mainRepository: MainRepository
) {

    suspend operator fun invoke(accessToken: AccessToken): List<InquiryEtc> {
        return try {
            val myInquiryEtcResponseList = mainRepository.getMyInquiryEtcs(accessToken.value)
            if (myInquiryEtcResponseList.isEmpty()) {
                emptyList()
            } else {
                val res = mutableListOf<InquiryEtc>()
                myInquiryEtcResponseList.forEach { inquiryEtcResponse ->
                    res.add(
                        InquiryEtc(
                            id = inquiryEtcResponse.id,
                            content = inquiryEtcResponse.content,
                            requestDate = inquiryEtcResponse.time,
                            answer = inquiryEtcResponse.answer ?: ""
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