package com.software.cafejariapp.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class MasterRoomImageSaying(
    val image: String,
    val person: String,
    val saying: String
):Parcelable{
    companion object{
        val empty = MasterRoomImageSaying("", "", "")
    }
}
