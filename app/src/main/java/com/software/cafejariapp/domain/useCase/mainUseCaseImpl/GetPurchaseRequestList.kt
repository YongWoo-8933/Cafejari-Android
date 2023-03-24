package com.software.cafejariapp.domain.useCase.mainUseCaseImpl

import com.software.cafejariapp.core.CustomException
import com.software.cafejariapp.core.TokenExpiredException
import com.software.cafejariapp.domain.repository.MainRepository
import com.software.cafejariapp.domain.entity.PurchaseHistory
import com.software.cafejariapp.domain.entity.AccessToken

class GetPurchaseRequestList(
    private val mainRepository: MainRepository
) {

    suspend operator fun invoke(accessToken: AccessToken): List<PurchaseHistory> {
        return try {
            val responseList = mainRepository.getPurchaseRequests(accessToken.value)
            val purchaseHistoryList = mutableListOf<PurchaseHistory>()
            val rejectedList = mutableListOf<PurchaseHistory>()
            responseList.forEach { purchaseResponse ->
                if (purchaseResponse.state < 0) {
                    rejectedList.add(
                        PurchaseHistory(
                            id = purchaseResponse.id,
                            time = purchaseResponse.time,
                            state = purchaseResponse.state,
                            item_name = purchaseResponse.item.name,
                            item_brand = purchaseResponse.item.brand,
                            item_price = purchaseResponse.item.price,
                            item_image = purchaseResponse.item.image
                        )
                    )
                } else {
                    purchaseHistoryList.add(
                        PurchaseHistory(
                            id = purchaseResponse.id,
                            time = purchaseResponse.time,
                            state = purchaseResponse.state,
                            item_name = purchaseResponse.item.name,
                            item_brand = purchaseResponse.item.brand,
                            item_price = purchaseResponse.item.price,
                            item_image = purchaseResponse.item.image
                        )
                    )
                }
            }
            purchaseHistoryList + rejectedList
        } catch (e: TokenExpiredException) {
            throw e
        } catch (e: CustomException) {
            throw e
        }
    }
}