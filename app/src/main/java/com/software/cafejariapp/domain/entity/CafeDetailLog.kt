package com.software.cafejariapp.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class CafeDetailLog(
    val id: Int,
    val update: String,
    val crowded: Int
):Parcelable{
    companion object{
        val empty = CafeDetailLog(0, "", -1)
    }
}
