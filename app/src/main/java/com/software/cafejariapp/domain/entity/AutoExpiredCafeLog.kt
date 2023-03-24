package com.software.cafejariapp.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class AutoExpiredCafeLog(
    val id: Int,
    val cafeLogId: Int,
    val cafeId: Int,
    val time: String,
    val name: String,
    val floor: Int,
    val start: String,
    val finish: String,
    val latitude: Double,
    val longitude: Double,
    val point: Int,
):Parcelable{
    companion object{
        val empty = AutoExpiredCafeLog(0, 0, 0, "", "", 1, "", "",0.0,0.0, 0)
    }
}
