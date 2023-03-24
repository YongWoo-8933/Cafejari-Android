package com.software.cafejariapp.presentation.feature.map.event

import android.content.Context
import androidx.compose.runtime.MutableState
import com.software.cafejariapp.presentation.GlobalState

sealed class AdEvent {

    data class LoadInterstitialAd(val context: Context) : AdEvent()

    data class ShowInterstitialAd(val context: Context) : AdEvent()

    data class ShowRewardedAd(
        val globalState: GlobalState,
        val context: Context,
        val loadingState: MutableState<Boolean>,
        val onSuccess: () -> Unit
    ) : AdEvent()
}