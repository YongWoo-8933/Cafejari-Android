package com.software.cafejariapp.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Item(
    val id: Int,
    val name: String,
    val category: String,
    val brand: String,
    val image: String,
    val price: Int,
):Parcelable
