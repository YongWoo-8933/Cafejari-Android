package com.software.cafejariapp.domain.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class RefreshToken(
    @PrimaryKey val id: Int,
    val value: String = ""
): Parcelable {
    companion object{
        val empty = RefreshToken(0, "")
    }
}
