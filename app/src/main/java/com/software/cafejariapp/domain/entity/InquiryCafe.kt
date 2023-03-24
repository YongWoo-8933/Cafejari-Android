package com.software.cafejariapp.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class InquiryCafe(
    val id: Int,
    val name: String,
    val address: String,
    val requestDate: String,
    val result: String,
):Parcelable {
    companion object {
        val empty = InquiryCafe(0, "", "", "", "")
    }
}
