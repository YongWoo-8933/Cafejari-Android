package com.software.cafejariapp.presentation.feature.map.util

sealed class PointResultType {
    object MasterExpired : PointResultType()
    object MasterExpiredWithAd : PointResultType()
    object ThumbsUp : PointResultType()
    object ThumbsUpWithAd : PointResultType()
}