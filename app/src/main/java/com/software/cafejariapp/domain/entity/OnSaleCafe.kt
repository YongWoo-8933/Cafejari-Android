package com.software.cafejariapp.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class OnSaleCafe(
    val order: Int,
    val saleContent: String,
    val image: String,
    val cafeInfoId: Int,
    val name: String,
    val city: String,
    val gu: String,
    val address: String,
    val latitude: Double,
    val longitude: Double
):Parcelable {
    companion object {
        val empty = OnSaleCafe(0, "", "", 0, "", "", "", "", 0.0, 0.0)
    }
}
