package com.software.cafejariapp.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Event(
    val id: Int,
    val name: String,
    val url: String,
    val start: String,
    val finish: String,
    val preview: String,
    val image: String,
    val is_posted: Boolean,
):Parcelable {
    companion object {
        val empty = Event(0, "", "", "", "", "각종 이벤트를 확인해보세요!", "", true)
    }
}
