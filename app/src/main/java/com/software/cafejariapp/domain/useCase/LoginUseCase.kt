package com.software.cafejariapp.domain.useCase

import com.software.cafejariapp.domain.useCase.loginUseCaseImpl.*

data class LoginUseCase(
    val cafeJariLogin: CafeJariLogin,
    val googleLogin: GoogleLogin,
    val kakaoLogin: KakaoLogin,
    val googleLoginFinish: GoogleLoginFinish,
    val kakaoLoginFinish: KakaoLoginFinish,
    val logout: Logout,
    val getPredictions: GetPredictions,
    val deleteLoginInfo: DeleteLoginInfo,
    val getUser: GetUser,
    val verifyEmailPassword: VerifyEmailPassword,
    val verifyNicknamePhoneNumber: VerifyNicknamePhoneNumber,
    val smsSend: SmsSend,
    val resetSmsSend: ResetSmsSend,
    val smsAuth: SmsAuth,
    val resetSmsAuth: ResetSmsAuth,
    val resetPassword: ResetPassword,
    val verifyRecommendationNickname: VerifyRecommendationNickname,
    val recommend: Recommend,
    val authorize: Authorize,
    val makeNewProfile: MakeNewProfile,
    val updateProfile: UpdateProfile,
    val updateFcmToken: UpdateFcmToken,
    val getSocialUserType: GetSocialUserType
)
