package com.software.cafejariapp.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable


@Parcelize
@Serializable
data class MoreInfo(
    val id: Int,
    val image: String,
    val event1: String,
    val event2: String,
    val event3: String,
    val notice1: String,
    val notice2: String,
    val notice3: String
):Parcelable {
    companion object{
        val empty = MoreInfo(0, "_none", "_none", "_none", "_none",
             "_none", "_none", "_none")
    }
}


