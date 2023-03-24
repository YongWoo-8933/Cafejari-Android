package com.software.cafejariapp.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Paragraph(
    val order: Int,
    val sub_title: String,
    val image: String = "_none",
    val sub_content: String,
):Parcelable{
    companion object{
        val empty = Paragraph(1, "_none", "_none", "_none")
    }
}
