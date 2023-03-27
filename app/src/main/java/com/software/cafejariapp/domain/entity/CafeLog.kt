package com.software.cafejariapp.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class CafeLog(
    val id: Int,
    val cafeId: Int,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val floor: Int,
    val start: String,
    val finish: String,
    val expired: Boolean,
    val point: Int,
    val updatePeriod: Int,
    val master: CafeMaster,
    val cafeDetailLogs: List<CafeDetailLog> = emptyList()
):Parcelable{
    companion object{
        val empty = CafeLog(0, 0,"", 0.0, 0.0,1, "",
            "", false,0, 30, CafeMaster.empty, emptyList())
    }
}
