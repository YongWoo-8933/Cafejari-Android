package com.software.cafejariapp.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class CafeRecentLog(
    val id: Int,
    val master: CafeMaster,
    val update: String,
    val crowded: Int
): Parcelable {
    companion object{
        val empty = CafeRecentLog(0, CafeMaster.empty, "", -1)
    }
}
