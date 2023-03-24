package com.software.cafejariapp.presentation.feature.map.viewModel

import android.graphics.Bitmap
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.*
import com.software.cafejariapp.core.*
import com.software.cafejariapp.domain.entity.OnSaleCafe
import com.software.cafejariapp.domain.useCase.MainUseCase
import com.software.cafejariapp.domain.entity.AutoExpiredCafeLog
import com.software.cafejariapp.domain.useCase.CafeUseCase
import com.software.cafejariapp.domain.util.DisableDateId
import com.software.cafejariapp.presentation.feature.map.event.MapEvent
import com.software.cafejariapp.presentation.feature.map.state.MapState
import com.software.cafejariapp.presentation.feature.map.util.PointResultType
import com.software.cafejariapp.presentation.util.Time
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

import javax.inject.Inject
import kotlin.random.Random


@OptIn(ExperimentalPagerApi::class)
@HiltViewModel
class MapViewModel @Inject constructor(
    val mainUseCase: MainUseCase,
    private val cafeUseCase: CafeUseCase,
    private val placesClient: PlacesClient
) : ViewModel() {

    private val _state = mutableStateOf(MapState())
    val state: State<MapState> = _state

    private val previewImageNum = 4

    private var modalCafeInfoId = 0

    fun onEvent(event: MapEvent) {
        when (event) {
            is MapEvent.MapInit -> {
                viewModelScope.launch {
                    event.globalState.refreshCafeInfos()
                    delay(200L)


                    _state.value = state.value.copy(
                        isEventLoading = true,
                    )
                    val eventsPair = mainUseCase.getEventList()
                    val unExpiredEvents = eventsPair.first
                    if (unExpiredEvents.isNotEmpty()) {
                        _state.value = state.value.copy(
                            randomEvent = unExpiredEvents[Random.nextInt(unExpiredEvents.size)],
                        )
                    }
                    _state.value = state.value.copy(
                        isEventLoading = false,
                    )
                    delay(200L)


                    if (!state.value.isPopUpViewed && mainUseCase.isTodayExecutable(DisableDateId.popUp)) {
                        val popUpNotificationList = mainUseCase.getPopUpNotificationList()
                        if (popUpNotificationList.isNotEmpty()) {
                            _state.value = state.value.copy(
                                popUpNotifications = popUpNotificationList,
                                popUpNotificationPagerState = PagerState(pageCount = popUpNotificationList.size)
                            )
                        }
                        if (event.globalState.user.value.dateJoined.length >= 19) {
                            if (Time.getPassingDayFrom(event.globalState.user.value.dateJoined) < 3 && !mainUseCase.isOnboardingWatched()) {
                                event.onOnBoardingDialogOpen()
                            }
                        }
                    }
                    delay(200L)


                    _state.value = state.value.copy(
                        onSaleCafes = mainUseCase.getOnSaleCafeList()
                    )
                }
            }
            is MapEvent.ClearModalPlaceInfo -> {
                _state.value = state.value.copy(
                    modalCafePlaceInfo = null,
                    modalCafeBitmaps = null,
                    modalCafeMoreBitmaps = null,
                    modalPhotoMetadatas = null,
                    modalAttributionTexts = emptyList(),
                    isImageLoading = false,
                    isPlaceInfoLoading = false,
                )
            }
            is MapEvent.ClearPopUpNotifications -> {
                _state.value = state.value.copy(
                    popUpNotificationPagerState = PagerState(0), popUpNotifications = emptyList()
                )
            }
            is MapEvent.ClearSearchCafes -> {
                _state.value = state.value.copy(
                    searchCafeInfos = emptyList()
                )
            }
            is MapEvent.SetPopUpViewed -> {
                _state.value = state.value.copy(
                    isPopUpViewed = true
                )
            }
            is MapEvent.SortOnSaleCafeByDistance -> {
                if (event.globalState.userLocation.value != null) {
                    val sortedOnSaleCafes = mutableListOf<OnSaleCafe>()
                    state.value.onSaleCafes.forEach { onSaleCafe ->
                        sortedOnSaleCafes.add(
                            onSaleCafe.copy(
                                order = event.globalState.userLocation.value.getDistance(
                                    onSaleCafe.latitude,
                                    onSaleCafe.longitude
                                )
                            )
                        )
                    }
                    _state.value = state.value.copy(
                        onSaleCafes = sortedOnSaleCafes.sortedBy { it.order }
                    )
                }
            }
            is MapEvent.FetchModalCafePlaceInfo -> {
                modalCafeInfoId = event.cafeInfo.id
                val placeId = event.cafeInfo.googlePlaceId
                val placeFields = listOf(
                    Place.Field.PHOTO_METADATAS,
                    Place.Field.OPENING_HOURS,
                    Place.Field.BUSINESS_STATUS,
                    Place.Field.UTC_OFFSET
                )
                val request = FetchPlaceRequest.newInstance(placeId, placeFields)
                _state.value = state.value.copy(
                    isImageLoading = true,
                    isPlaceInfoLoading = true
                )
                placesClient.fetchPlace(request)
                    .addOnSuccessListener { response: FetchPlaceResponse ->
                        if (modalCafeInfoId == event.cafeInfo.id) {
                            val place = response.place
                            val photoMetadatas = place.photoMetadatas
                            if (photoMetadatas != null && photoMetadatas.isNotEmpty()) {
                                var lastIndex = previewImageNum - 1
                                val bitmaps = mutableListOf<Bitmap>()
                                val attributions = mutableListOf<String>()
                                if (photoMetadatas.size < previewImageNum) {
                                    lastIndex = photoMetadatas.size - 1
                                }
                                for (index in 0..lastIndex) {
                                    val photoMetadata = photoMetadatas[index]
                                    attributions.add(photoMetadata.attributions)
                                    val photoRequest =
                                        FetchPhotoRequest.builder(photoMetadata).build()
                                    placesClient.fetchPhoto(photoRequest)
                                        .addOnSuccessListener { fetchPhotoResponse: FetchPhotoResponse ->
                                            if (modalCafeInfoId == event.cafeInfo.id) {
                                                bitmaps.add(fetchPhotoResponse.bitmap)
                                                if (bitmaps.size == lastIndex + 1) {
                                                    _state.value = state.value.copy(
                                                        modalCafeBitmaps = bitmaps.toList(),
                                                        modalAttributionTexts = attributions.toList(),
                                                        isImageLoading = false,
                                                        modalPhotoMetadatas = photoMetadatas
                                                    )
                                                    if (photoMetadatas.size > previewImageNum) {
                                                        _state.value = state.value.copy(
                                                            modalCafeMoreBitmaps = emptyList()
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                        .addOnFailureListener {
                                            _state.value = state.value.copy(
                                                isImageLoading = false
                                            )
                                        }
                                }
                            } else {
                                _state.value = state.value.copy(
                                    isImageLoading = false,
                                    modalCafeBitmaps = emptyList()
                                )
                            }
                            _state.value = state.value.copy(
                                modalCafePlaceInfo = place,
                                isPlaceInfoLoading = false
                            )
                        }
                    }
                    .addOnFailureListener {
                        _state.value = state.value.copy(
                            isPlaceInfoLoading = false,
                            modalCafeBitmaps = emptyList()
                        )
                    }
            }
            is MapEvent.FetchMoreImages -> {
                _state.value = state.value.copy(
                    isImageLoading = true
                )
                val photoMetadatas = event.photoMetadatas
                val lastIndex = photoMetadatas.size - 1
                val bitmaps = mutableListOf<Bitmap>()
                val attributions = mutableListOf<String>()
                state.value.modalAttributionTexts.forEach { attributions.add(it) }

                for (index in previewImageNum..lastIndex) {
                    val photoMetadata = photoMetadatas[index]
                    val photoRequest = FetchPhotoRequest.builder(photoMetadata).build()
                    attributions.add(photoMetadata.attributions)
                    placesClient.fetchPhoto(photoRequest)
                        .addOnSuccessListener { fetchPhotoResponse: FetchPhotoResponse ->
                            bitmaps.add(fetchPhotoResponse.bitmap)
                            if (bitmaps.size + previewImageNum == photoMetadatas.size) {
                                _state.value = state.value.copy(
                                    modalCafeMoreBitmaps = bitmaps.toList(),
                                    modalAttributionTexts = attributions.toList(),
                                    isPlaceInfoLoading = false
                                )
                            }
                        }
                        .addOnFailureListener {
                            _state.value = state.value.copy(
                                isImageLoading = false
                            )
                        }
                }
            }
            is MapEvent.DeleteAutoExpiredCafeLog -> {
                viewModelScope.launch {
                    try {
                        cafeUseCase.deleteAutoExpiredCafeLog(
                            accessToken = event.globalState.accessToken.value,
                            autoExpiredCafeLogId = event.globalState.autoExpiredCafeLog.value.id
                        )
                        event.globalState.autoExpiredCafeLog.value = AutoExpiredCafeLog.empty
                    } catch (e: TokenExpiredException) {
                        try {
                            event.globalState.refreshAccessToken {
                                onEvent(MapEvent.DeleteAutoExpiredCafeLog(event.globalState))
                            }
                        } catch (e: RefreshTokenExpiredException) {
                        }
                    } catch (throwable: Throwable) {

                    }
                }
            }
            is MapEvent.ThumbsUp -> {
                viewModelScope.launch {
                    try {
                        event.globalState.user.value = cafeUseCase.requestThumbsUp(
                            accessToken = event.globalState.accessToken.value,
                            recentLogId = event.recentLogId,
                            isAdThumbsUp = event.isAdThumbsUp
                        )
                        event.onSuccess(
                            25,
                            if (event.isAdThumbsUp) {
                                PointResultType.ThumbsUpWithAd
                            } else {
                                PointResultType.ThumbsUp
                            }
                        )
                    } catch (e: TokenExpiredException) {
                        try {
                            event.globalState.refreshAccessToken {
                                onEvent(
                                    MapEvent.ThumbsUp(
                                        event.globalState,
                                        event.recentLogId,
                                        event.isAdThumbsUp,
                                        event.onSuccess
                                    )
                                )
                            }
                        } catch (e: RefreshTokenExpiredException) {
                        }
                    } catch (e: CustomException) {
                        event.globalState.showSnackBar(e.message.toString())
                    }
                }
            }
            is MapEvent.SearchCafe -> {
                viewModelScope.launch {
                    try {
                        val res =
                            cafeUseCase.search(event.query, event.globalState.userLocation.value)
                        if (res.isNotEmpty()) {
                            _state.value = state.value.copy(
                                searchCafeInfos = cafeUseCase.search(
                                    event.query, event.globalState.userLocation.value
                                )
                            )
                        }
                    } catch (e: CustomException) {
                        event.globalState.showSnackBar(e.message.toString())
                    }
                }
            }
            is MapEvent.AddAdPoint -> {
                viewModelScope.launch {
                    try {
                        event.globalState.user.value = cafeUseCase.addAdPoint(
                            accessToken = event.globalState.accessToken.value,
                            cafeLogId = event.cafeLogId
                        )
                    } catch (e: TokenExpiredException) {
                        try {
                            event.globalState.refreshAccessToken {
                                onEvent(MapEvent.AddAdPoint(event.globalState, event.cafeLogId))
                            }
                        } catch (e: RefreshTokenExpiredException) {
                        }
                    } catch (e: CustomException) {
                        event.globalState.showSnackBar(e.message.toString())
                    }
                }
            }
        }
    }
}