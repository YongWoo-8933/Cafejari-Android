package com.software.cafejariapp.presentation.feature.map.state

import com.google.android.gms.ads.interstitial.InterstitialAd

data class AdState(
    val interstitialAd: InterstitialAd? = null,

    val remainSecondsBeforeAdPopUp: Int = 4
)