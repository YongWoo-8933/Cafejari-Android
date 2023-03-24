package com.software.cafejariapp.data.util.error

import kotlinx.serialization.Serializable


@Serializable
data class ErrorResponse(
    val error_code: Int? = null,
    val detail: String? = null
)

@Serializable
data class TokenInvalidResponse(
    val code: String? = null
)

@Serializable
data class RegisterErrorResponse(
    val non_field_errors : List<String>? = null,
    val email: List<String>? = null,
    val password1: List<String>? = null,
    val nickname: List<String>? = null,
    val phone_number: List<String>? = null,
)
