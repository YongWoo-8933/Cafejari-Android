package com.software.cafejariapp.presentation.feature.main.state

import com.software.cafejariapp.domain.entity.Leader

data class LeaderBoardState (
    val totalLeaders: List<Leader> = emptyList(),
    val monthLeaders: List<Leader> = emptyList(),
    val weekLeaders: List<Leader> = emptyList(),
    val isLeadersLoading: Boolean = true
)