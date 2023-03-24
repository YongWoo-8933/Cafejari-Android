package com.software.cafejariapp.domain.util

sealed class SocialUserType {
    object CafeJari: SocialUserType()
    object Kakao: SocialUserType()
    object Google: SocialUserType()
}
