package com.software.cafejariapp.presentation.feature.login.event

import android.content.Context
import com.software.cafejariapp.presentation.GlobalState
import com.software.cafejariapp.domain.entity.Login


sealed class LoginEvent {

    object ClearPredictions : LoginEvent()

    data class ChangePredictions(val email: String) : LoginEvent()

    data class DeletePrediction(val login: Login) : LoginEvent()

    data class CafejariLogin(
        val globalState: GlobalState,
        val email: String,
        val password: String,
        val onFinish: () -> Unit
    ) : LoginEvent()

    data class GoogleLogin(
        val globalState: GlobalState,
        val email: String,
        val code: String,
        val onFinish: () -> Unit
    ) : LoginEvent()

    data class KakaoLogin(
        val globalState: GlobalState,
        val accessToken: String,
        val onFinish: () -> Unit
    ) : LoginEvent()

    data class VerifyEmailPassword(
        val globalState: GlobalState,
        val email: String,
        val password1: String,
        val password2: String,
        val onFinish: () -> Unit
    ) : LoginEvent()

    data class SendSms(
        val globalState: GlobalState,
        val phoneNumber: String,
        val onSuccess: () -> Unit
    ) : LoginEvent()

    data class SendSmsForReset(
        val globalState: GlobalState,
        val phoneNumber: String,
        val onSuccess: () -> Unit
    ) : LoginEvent()

    data class AuthSms(
        val globalState: GlobalState,
        val phoneNumber: String,
        val authNumber: String,
        val onSuccess: () -> Unit,
        val onFinish: () -> Unit
    ) : LoginEvent()

    data class AuthSmsForReset(
        val globalState: GlobalState,
        val phoneNumber: String,
        val authNumber: String,
        val onSuccess: (String) -> Unit
    ) : LoginEvent()

    data class VerifyRecommendationNickname(
        val globalState: GlobalState,
        val nickname: String
    ) : LoginEvent()

    data class Recommend(
        val globalState: GlobalState,
        val nickname: String
    ) : LoginEvent()

    data class Authorize(
        val globalState: GlobalState,
        val nickname: String,
        val phoneNumber: String,
        val onSuccess: () -> Unit,
        val onFinish: () -> Unit,
        val context: Context
    ) : LoginEvent()

    data class ResetPassword(
        val globalState: GlobalState,
        val password1: String,
        val password2: String
    ) : LoginEvent()
}