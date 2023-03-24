package com.software.cafejariapp.data.source.remote.dto

import com.software.cafejariapp.domain.entity.Item
import kotlinx.serialization.Serializable


@Serializable
data class PurchaseRequest(
    val item_id: Int,
)

@Serializable
data class PurchaseResponse(
    val id: Int,
    val time: String,
    val state: Int,
    val item: Item
)