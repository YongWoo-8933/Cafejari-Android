package com.software.cafejariapp.presentation.feature.main.state

import com.software.cafejariapp.domain.entity.Item
import com.software.cafejariapp.domain.entity.PurchaseHistory

data class ShopState(
    // shop
    val items: List<Item> = emptyList(),
    val isItemLoading: Boolean = true,

    // shopping bag
    val purchaseHistories: List<PurchaseHistory> = emptyList(),
    val isPurchaseHistoryLoading: Boolean = true
)
