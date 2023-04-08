package com.software.cafejariapp.presentation.feature.main.event

import android.net.Uri
import com.software.cafejariapp.domain.entity.CafeLog
import com.software.cafejariapp.presentation.GlobalState

sealed class ProfileEvent {
    // profile
    data class ProfileScreenInit(val globalState: GlobalState) : ProfileEvent()

    // profile edit
    data class ProfileEditScreenInit(val globalState: GlobalState) : ProfileEvent()
    data class DeleteAccount(val globalState: GlobalState) : ProfileEvent()
    data class CafejariLoginForAuth(
        val globalState: GlobalState, val password: String
    ) : ProfileEvent()
    data class GoogleLoginForAuth(
        val globalState: GlobalState,
        val email: String,
        val code: String,
        val onFinish: () -> Unit
    ) : ProfileEvent()
    data class KakaoLoginForAuth(
        val globalState: GlobalState,
        val accessToken: String,
        val onFinish: () -> Unit
    ) : ProfileEvent()
    data class UpdateProfile(
        val globalState: GlobalState,
        val nickname: String,
        val image: Uri?
    ) : ProfileEvent()

    // profile Kalendar
    data class ProfileKalendarScreenInit(val globalState: GlobalState) : ProfileEvent()
    data class SelectCafeLogs(val selectedCafeLogs: List<CafeLog>) : ProfileEvent()

    // point history
    data class GetEventPointHistories(val globalState: GlobalState) : ProfileEvent()
    data class GetHistoryCafeLogs(val globalState: GlobalState) : ProfileEvent()

}