package com.software.cafejariapp.presentation.feature.map.event

import com.software.cafejariapp.presentation.GlobalState
import com.software.cafejariapp.presentation.feature.map.util.PointResultType

sealed class MasterRoomEvent {
    data class RegisterMaster(
        val globalState: GlobalState,
        val periodMinute: Int,
        val initialCrowdedInt: Int,
    ) : MasterRoomEvent()

    data class UpdateCrowded(
        val globalState: GlobalState,
        val newCrowdedInt: Int
    ) : MasterRoomEvent()

    data class DeleteCafeDetailLog(
        val globalState: GlobalState,
        val selectedDetailLogId: Int
    ) : MasterRoomEvent()

    data class ExpireMaster(
        val globalState: GlobalState,
        val adMultipleApplied: Boolean,
        val onSuccess: (Int, PointResultType) -> Unit
    ) : MasterRoomEvent()
}
