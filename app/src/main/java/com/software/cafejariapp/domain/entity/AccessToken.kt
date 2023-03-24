package com.software.cafejariapp.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AccessToken(
    val value: String,
    val expiration: String,
): Parcelable {
    companion object{
        val empty = AccessToken("", "")
    }
}
