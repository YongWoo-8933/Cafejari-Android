package com.software.cafejariapp.presentation.feature.map.event

import com.google.android.libraries.places.api.model.PhotoMetadata
import com.software.cafejariapp.presentation.GlobalState
import com.software.cafejariapp.domain.entity.CafeInfo
import com.software.cafejariapp.presentation.feature.map.util.PointResultType

sealed class MapEvent {
    data class MapInit(
        val globalState: GlobalState,
        val onOnBoardingDialogOpen: () -> Unit
    ) : MapEvent()

    object ClearModalPlaceInfo : MapEvent()

    object ClearPopUpNotifications : MapEvent()

    object ClearSearchCafes : MapEvent()

    object SetPopUpViewed : MapEvent()

    data class SortOnSaleCafeByDistance(val globalState: GlobalState) : MapEvent()

    data class FetchModalCafePlaceInfo(val cafeInfo: CafeInfo) : MapEvent()

    data class FetchMoreImages(val photoMetadatas: List<PhotoMetadata>) : MapEvent()

    data class DeleteAutoExpiredCafeLog(val globalState: GlobalState) : MapEvent()

    data class ThumbsUp(
        val globalState: GlobalState,
        val recentLogId: Int,
        val isAdThumbsUp: Boolean,
        val onSuccess: (Int, PointResultType) -> Unit
    ) : MapEvent()

    data class SearchCafe(
        val globalState: GlobalState,
        val query: String
    ) : MapEvent()

    data class AddAdPoint(
        val globalState: GlobalState,
        val cafeLogId: Int
    ) : MapEvent()
}