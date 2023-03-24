package com.software.cafejariapp.data.source.remote.dto

import kotlinx.serialization.Serializable


@Serializable
data class TokenRefreshResponse(
    val access: String = "",
    val access_token_expiration: String = "",
    val error: String? = null
)

@Serializable
data class TokenRefreshRequest(
    val refresh: String
)

@Serializable
data class TokenVerifyRequest(
    val token: String
)
