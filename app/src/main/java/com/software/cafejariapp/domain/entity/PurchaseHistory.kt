package com.software.cafejariapp.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class PurchaseHistory(
    val id: Int,
    val time: String,
    val state: Int,
    val item_name: String,
    val item_brand: String,
    val item_price: Int,
    val item_image: String,
):Parcelable{
    companion object{
        val empty = PurchaseHistory(0, "_none", 0, "_none", "_none", 0, "_none")
    }
}
