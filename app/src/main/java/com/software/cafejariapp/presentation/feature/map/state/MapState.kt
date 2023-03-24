package com.software.cafejariapp.presentation.feature.map.state

import android.graphics.Bitmap
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.libraries.places.api.model.PhotoMetadata
import com.google.android.libraries.places.api.model.Place
import com.software.cafejariapp.domain.entity.OnSaleCafe
import com.software.cafejariapp.domain.entity.PopUpNotification
import com.software.cafejariapp.domain.entity.CafeInfo
import com.software.cafejariapp.domain.entity.Event

@OptIn(ExperimentalPagerApi::class)
data class MapState(
    val popUpNotifications: List<PopUpNotification> = emptyList(),

    val onSaleCafes: List<OnSaleCafe> = emptyList(),

    val searchCafeInfos: List<CafeInfo> = emptyList(),

    val popUpNotificationPagerState: PagerState = PagerState(
        0,
        0,
        0f,
        3,
        false
    ),

    val modalCafePlaceInfo: Place? = null,

    val modalCafeBitmaps: List<Bitmap>? = null,

    val modalCafeMoreBitmaps: List<Bitmap>? = null,

    val modalPhotoMetadatas: List<PhotoMetadata>? = null,

    val modalAttributionTexts: List<String> = emptyList(),

    val randomEvent: Event = Event.empty,

    val isEventLoading: Boolean = false,

    val isPlaceInfoLoading: Boolean = false,

    val isImageLoading: Boolean = false,

    val isPopUpViewed: Boolean = false,
)