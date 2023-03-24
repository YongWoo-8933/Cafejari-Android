package com.software.cafejariapp.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User (
    val userId: Int,
    val profile_id: Int,
    val isStaff: Boolean,
    val dateJoined: String,
    val email: String,
    val last_login: String,
    val authorization: Boolean,
    val nickname: String,
    val fcm_token: String,
    val phone_number: String,
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
