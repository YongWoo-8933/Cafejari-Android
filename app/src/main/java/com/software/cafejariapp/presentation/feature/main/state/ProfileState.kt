package com.software.cafejariapp.presentation.feature.main.state

import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.himanshoe.kalendar.common.data.KalendarEvent
import com.software.cafejariapp.domain.entity.CafeLog
import com.software.cafejariapp.domain.entity.Event
import com.software.cafejariapp.domain.entity.EventPointHistory
import com.software.cafejariapp.domain.util.SocialUserType

@OptIn(ExperimentalPagerApi::class)
data class ProfileState constructor(
    // profile
    val events: List<Event> = emptyList(),
    val pagerState: PagerState = PagerState(1, 0, 0f, 3, false),
    val isEventLoaded: Boolean = false,

    // profile edit
    val socialUserType: SocialUserType = SocialUserType.CafeJari,
    val isSocialUserTypeLoading: Boolean = true,
    val isProfileEditAuthed: Boolean = false,
    val isProfileUpdateLoading: Boolean = false,

    // profile kalendar
    val kalendarCafeLogs: List<List<CafeLog>> = emptyList(),
    val kalendarEvents: List<KalendarEvent> = emptyList(),
    val selectedCafeLogs: List<CafeLog> = emptyList(),
    val isKalendarCafeLogLoading: Boolean = false,

    // point history
    val historyCafeLogs: List<List<CafeLog>> = emptyList(),
    val eventPointHistories: List<EventPointHistory> = emptyList(),
    val isHistoryCafeLogLoading: Boolean = true,
    val isEventPointHistoryLoading: Boolean = true
)
