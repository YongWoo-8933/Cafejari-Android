package com.software.cafejariapp.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Leader(
    val nickname: String,
    val activity: Int,
    val ranking: Int,
    val userImage: String
):Parcelable