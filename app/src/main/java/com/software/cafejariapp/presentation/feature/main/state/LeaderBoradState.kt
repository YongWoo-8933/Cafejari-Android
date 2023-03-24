package com.software.cafejariapp.presentation.feature.main.state

import com.software.cafejariapp.domain.entity.Ranker

data class LeaderBoardState (
    val rankingMonthList: List<Ranker> = emptyList(),
    val rankingWeekList: List<Ranker> = emptyList(),
    val isRankingListLoading: Boolean = true
)