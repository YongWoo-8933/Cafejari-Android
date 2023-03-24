package com.software.cafejariapp.presentation.feature.main.event

import com.software.cafejariapp.domain.entity.Item
import com.software.cafejariapp.presentation.GlobalState
import com.software.cafejariapp.domain.entity.PurchaseHistory

sealed class ShopEvent {

    // shop
    data class GetItems(val globalState: GlobalState) : ShopEvent()
    data class RequestPurchase(
        val globalState: GlobalState,
        val item: Item
    ) : ShopEvent()

    // shopping bag
    data class GetPurchaseHistories(val globalState: GlobalState) : ShopEvent()
    data class DeletePurchaseHistory(
        val globalState: GlobalState,
        val history: PurchaseHistory
    ) : ShopEvent()
}