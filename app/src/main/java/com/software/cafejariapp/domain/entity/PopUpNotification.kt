package com.software.cafejariapp.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class PopUpNotification(
    val order: Int,
    val url: String,
    val image: String,
    val cafeInfo: CafeInfo,
):Parcelable {
    fun hasConnectedCafeInfo(): Boolean {
        return this.cafeInfo.id != 0
    }
    companion object {
        val empty = PopUpNotification(0, "", "", CafeInfo.empty)
    }
}
