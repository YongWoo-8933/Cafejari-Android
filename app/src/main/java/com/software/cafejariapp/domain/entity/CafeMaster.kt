package com.software.cafejariapp.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class CafeMaster(
    val userId: Int,
    val nickname: String,
    val grade: Int,
): Parcelable {
    companion object{
        val empty = CafeMaster(0, "_none", 0)
    }
}
