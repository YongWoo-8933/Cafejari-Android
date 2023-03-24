package com.software.cafejariapp.domain.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Login(
    @PrimaryKey val email: String,
    val password: String,
    val test: Int = 1
): Parcelable {
    companion object{
        val empty = Login(
            "",
            "",
            0
        )
    }
}
