package com.software.cafejariapp.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Version(
    val release: Int,
    val major: Int,
    val minor: Int,
):Parcelable{
    companion object {
        val empty = Version(-1, -1, -1)
        val current = Version(2, 4, 4)
    }
}
