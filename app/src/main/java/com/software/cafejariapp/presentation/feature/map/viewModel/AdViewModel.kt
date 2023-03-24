package com.software.cafejariapp.presentation.feature.map.viewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.libraries.places.api.net.PlacesClient
import com.software.cafejariapp.core.*
import com.software.cafejariapp.domain.useCase.CafeUseCase
import com.software.cafejariapp.domain.useCase.MainUseCase
import com.software.cafejariapp.presentation.feature.map.event.AdEvent
import com.software.cafejariapp.presentation.feature.map.state.AdState
import com.software.cafejariapp.presentation.util.AdId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AdViewModel @Inject constructor(

) : ViewModel() {

    private val _state = mutableStateOf(AdState())
    val state: State<AdState> = _state

    fun onEvent(event: AdEvent) {
        when(event) {
            is AdEvent.LoadInterstitialAd -> {
                InterstitialAd.load(
                    event.context,
                    AdId.Interstitial.id,
                    AdRequest.Builder().build(),
                    object : InterstitialAdLoadCallback() {
                        override fun onAdFailedToLoad(p0: LoadAdError) {
                            _state.value = state.value.copy(
                                interstitialAd = null,
                            )
                        }

                        override fun onAdLoaded(p0: InterstitialAd) {
                            _state.value = state.value.copy(
                                interstitialAd = p0,
                            )
                        }
                    },
                )
            }
            is AdEvent.ShowInterstitialAd -> {
                viewModelScope.launch {
                    if (state.value.interstitialAd != null) {
                        while (state.value.remainSecondsBeforeAdPopUp > 0) {
                            _state.value = state.value.copy(
                                remainSecondsBeforeAdPopUp = state.value.remainSecondsBeforeAdPopUp - 1,
                            )
                            delay(1000L)
                        }
                        _state.value = state.value.copy(
                            remainSecondsBeforeAdPopUp = 4
                        )
                        state.value.interstitialAd!!.show(event.context.findActivity())
                        onEvent(AdEvent.LoadInterstitialAd(event.context))
                    }
                }
            }
            is AdEvent.ShowRewardedAd -> {
                event.loadingState.value = true
                RewardedAd.load(
                    event.context,
                    AdId.Reward.id,
                    AdRequest.Builder().build(),
                    object : RewardedAdLoadCallback() {
                        override fun onAdFailedToLoad(adError: LoadAdError) {
                            if (event.loadingState.value) {
                                event.loadingState.value = false
                                event.globalState.showSnackBar("현재 볼러올 수 있는 광고가 없습니다. 잠시후 다시 시도해주세요")
                            }
                        }

                        override fun onAdLoaded(p0: RewardedAd) {
                            if (event.loadingState.value) {
                                event.loadingState.value = false
                                p0.show(
                                    event.context.findActivity(),
                                    OnUserEarnedRewardListener() {
                                        fun onUserEarnedReward(rewardItem: RewardItem) {
                                            event.onSuccess()
                                            event.loadingState.value = false
                                        }
                                        onUserEarnedReward(it)
                                    }
                                )
                            }
                        }
                    }
                )
                viewModelScope.launch {
                    delay(12000L)
                    if (event.loadingState.value) {
                        event.loadingState.value = false
                        event.globalState.showSnackBar("현재 볼러올 수 있는 광고가 없습니다. 잠시후 다시 시도해주세요")
                    }
                }
            }
        }
    }
}