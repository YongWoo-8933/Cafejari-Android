package com.software.cafejariapp.domain.util

sealed class DisableDateId {
    companion object {
        val onBoarding = 0
        val update = 1
        val permission = 2
        val popUp = 3
    }
}