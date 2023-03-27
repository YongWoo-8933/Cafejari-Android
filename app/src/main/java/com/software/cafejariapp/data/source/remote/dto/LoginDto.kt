package com.software.cafejariapp.data.source.remote.dto

import android.os.Parcelable
import com.software.cafejariapp.domain.entity.User
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
data class AuthRequest(
    val nickname: String, val phone_number: String
)

@Serializable
data class CafejariLoginRequest(
    val email: String, val password: String
)

@Serializable
data class GoogleLoginRequest(
    val email: String, val code: String
)

@Serializable
data class GoogleLoginResponse(
    val access_token: String,
    val is_user_exist: Boolean,
    val code: String
)

@Serializable
data class KakaoLoginRequest(
    val access_token: String
)

@Serializable
data class KakaoLoginResponse(
    val access_token: String = "", val is_user_exist: Boolean = false
)

@Serializable
data class LogoutRequest(
    val refresh: String
)

@Serializable
data class LoginResponse(
    val access_token: String,
    val refresh_token: String,
    val user: UserResponse
)

@Serializable
data class MakeNewProfileRequest(
    val user: Int,
    val nickname: String,
    val phone_number: String,
    val fcm_token: String
)

@Serializable
data class PreAuthorizeDto(
    val nickname: String, val phone_number: String
)

@Serializable
data class PreRegisterResponse(
    val email: String, val password: String
)

@Parcelize
@Serializable
data class ProfileResponse(
    val id: Int,
    val nickname: String = "",
    val fcm_token: String,
    val phone_number: String = "12345678",
    val image: String,
    val point: Int,
    val grade: Int,
    val activity: Int
) : Parcelable {
    companion object {
        val empty = ProfileResponse(
            0,
            "",
            "_none",
            "12345678",
            "",
            0,
            0,
            0
        )
    }
}

@Serializable
data class RecommendationDto(
    val nickname: String
)

@Serializable
data class RegisterRequest(
    val email: String,
    val password1: String,
    val password2: String,
)

@Serializable
data class RegisterResponse(
    val access_token: String,
    val refresh_token: String,
    val user: UserResponse,
)

@Serializable
data class ResetPasswordRequest(
    val user_id: Int,
    val token: String,
    val new_password1: String,
    val new_password2: String,
)

@Serializable
data class ResetSmsAuthResponse(
    val token: String,
    val user_id: Int,
    val email: String,
)

@Serializable
data class SmsAuthRequest(
    val phone_number: String, val auth_number: String
)

@Serializable
data class SmsSendRequest(
    val phone_number: String
)

@Serializable
data class SocialLoginRequest(
    val access_token: String, val code: String
)

@Serializable
data class SocialLoginResponse(
    val access_token: String,
    val refresh_token: String,
    val user: UserResponse
)

@Serializable
data class SocialUserCheckResponse(
    val type: String
)

@Serializable
data class UpdateFcmTokenRequest(
    val fcm_token: String
)

@Parcelize
@Serializable
data class UserResponse(
    val id: Int,
    val last_login: String,
    val is_staff: Boolean,
    val date_joined: String,
    val email: String,
    val authorization: Boolean,
    val profile: ProfileResponse = ProfileResponse.empty
) : Parcelable {
    companion object {
        val empty = UserResponse(
            0,
            "",
            false,
            "",
            "",
            false
        )
    }

    fun toUser(): User {
        return User(
            userId = this.id,
            profileId = this.profile.id,
            isStaff = this.is_staff,
            dateJoined = this.date_joined,
            email = this.email,
            lastLogin = this.last_login,
            authorization = this.authorization,
            nickname = this.profile.nickname,
            fcmToken = this.profile.fcm_token,
            phoneNumber = this.profile.phone_number,
            image = this.profile.image,
            point = this.profile.point,
            grade = this.profile.grade,
            activity = this.profile.activity
        )
    }
}