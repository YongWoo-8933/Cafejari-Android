package com.software.cafejariapp.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class EventPointHistory(
    val id: Int,
    val content: String,
    val point: Int,
    val time: String
):Parcelable {
    companion object {
        val empty = EventPointHistory(0, "", 0, "")
    }
}
