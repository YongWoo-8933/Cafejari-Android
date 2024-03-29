package com.software.cafejariapp.presentation.feature.main.event

import com.software.cafejariapp.presentation.GlobalState


sealed class LeaderBoardEvent {
    data class GetLeaders(val globalState: GlobalState) : LeaderBoardEvent()
}