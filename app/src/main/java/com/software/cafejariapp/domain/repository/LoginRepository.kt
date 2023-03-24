package com.software.cafejariapp.domain.repository

import com.software.cafejariapp.data.source.remote.dto.*
import com.software.cafejariapp.domain.entity.Login


interface LoginRepository {

    suspend fun getLoginsFromRoom(): List<Login>

    suspend fun insertLoginToRoom(login: Login)

    suspend fun deleteLoginFromRoom(login: Login)

    suspend fun login(loginRequest: CafejariLoginRequest): LoginResponse

    suspend fun socialLogin(
        url: String,
        socialLoginRequest: SocialLoginRequest
    ): SocialLoginResponse

    suspend fun googleLogin(googleLoginRequest: GoogleLoginRequest): GoogleLoginResponse

    suspend fun kakaoLogin(kakaoLoginRequest: KakaoLoginRequest): KakaoLoginResponse

    suspend fun logout(logoutRequest: LogoutRequest)

    suspend fun getUser(accessToken: String): UserResponse

    suspend fun makeNewProfile(
        accessToken: String,
        makeNewProfileRequest: MakeNewProfileRequest
    ): UserResponse

    suspend fun preRegistration(registerRequest: RegisterRequest): PreRegisterResponse

    suspend fun registration(registerRequest: RegisterRequest): RegisterResponse

    suspend fun smsSend(smsSendRequest: SmsSendRequest)

    suspend fun smsAuth(smsAuthRequest: SmsAuthRequest)

    suspend fun preRecommendation(preRecommendationDto: RecommendationDto): RecommendationDto

    suspend fun recommendation(
        accessToken: String,
        recommendationDto: RecommendationDto
    ): UserResponse

    suspend fun preAuthorization(preAuthorizeDto: PreAuthorizeDto): PreAuthorizeDto

    suspend fun authorization(
        accessToken: String,
        profileId: Int,
        authRequest: AuthRequest
    ): UserResponse

    suspend fun updateFcmToken(
        accessToken: String,
        profile_id: Int,
        updateFcmTokenRequest: UpdateFcmTokenRequest
    ): UserResponse

    suspend fun updateProfile(
        accessToken: String,
        profile_id: Int,
        image_path: String?, nickname: String?
    ): UserResponse

    suspend fun resetSmsSend(smsSendRequest: SmsSendRequest)

    suspend fun resetSmsAuth(smsAuthRequest: SmsAuthRequest): ResetSmsAuthResponse

    suspend fun resetPassword(resetPasswordRequest: ResetPasswordRequest)

    suspend fun getSocialUserType(accessToken: String): SocialUserCheckResponse
}
