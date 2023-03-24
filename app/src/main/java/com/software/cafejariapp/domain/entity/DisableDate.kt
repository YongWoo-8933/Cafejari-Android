package com.software.cafejariapp.domain.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class DisableDate(
    @PrimaryKey val id: Int,
    val year: Int,
    val month: Int,
    val day: Int
): Parcelable {
    companion object{
        val empty = DisableDate(0, 0,0, 0)
    }
}