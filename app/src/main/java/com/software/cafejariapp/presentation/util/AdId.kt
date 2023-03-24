package com.software.cafejariapp.presentation.util

sealed class AdId(val id: String) {
    object Banner : AdId("ca-app-pub-6775038074316382/9833826369")
    object Interstitial : AdId("ca-app-pub-6775038074316382/4595023697")
    object Reward : AdId("ca-app-pub-6775038074316382/1678088453")

    // test시 활성화
    //    object Banner : AdId("ca-app-pub-3940256099942544/6300978111")
    //    object Interstitial : AdId("ca-app-pub-3940256099942544/1033173712")
    //    object Reward : AdId("ca-app-pub-3940256099942544/5224354917")
}
