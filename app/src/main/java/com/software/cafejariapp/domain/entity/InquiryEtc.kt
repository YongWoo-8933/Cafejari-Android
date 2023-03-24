package com.software.cafejariapp.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class InquiryEtc(
    val id: Int,
    val content: String,
    val requestDate: String,
    val answer: String,
):Parcelable {
    companion object {
        val empty = InquiryEtc(0, "", "", "")
    }
}
