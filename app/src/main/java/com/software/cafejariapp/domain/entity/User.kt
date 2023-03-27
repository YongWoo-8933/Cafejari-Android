package com.software.cafejariapp.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User (
    val userId: Int,
    val profileId: Int,
    val isStaff: Boolean,
    val dateJoined: String,
    val email: String,
    val lastLogin: String,
    val authorization: Boolean,
    val nickname: String,
    val fcmToken: String,
    val phoneNumber: String,
    val image: String,
    val point: Int,
    val grade: Int,
    val activity: Int,
): Parcelable {
    companion object{
        val empty = User(
            0,
            0,
            false,
            "",
            "",
            "",
            false,
            "",
            "",
            "",
            "",
            0,
            0,
            0,
        )
    }
}
