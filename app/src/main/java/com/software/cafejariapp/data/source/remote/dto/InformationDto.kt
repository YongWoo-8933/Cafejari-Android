package com.software.cafejariapp.data.source.remote.dto

import kotlinx.serialization.Serializable


@Serializable
data class FAQResponse(
    val order: Int,
    val question: String,
    val answer: String
)

@Serializable
data class InquiryCafeAdditionalInfoRequest(
    val cafe_info: Int,
    val store_information: String,
    val opening_hour: String,
    val wall_socket: String,
    val restroom: String,
)

@Serializable
data class InquiryCafeRequest(
    val email: String,
    val cafe_name: String,
    val cafe_address: String,
)

@Serializable
data class InquiryCafeResponse(
    val id: Int,
    val time: String,
    val cafe_name: String,
    val cafe_address: String,
    val result: String?,
)

@Serializable
data class InquiryEtcRequest(
    val email: String,
    val content: String,
)

@Serializable
data class InquiryEtcResponse(
    val id: Int,
    val time: String,
    val content: String,
    val answer: String?,
)

@Serializable
data class OnSaleCafeResponse(
    val order: Int, val content: String, val image: String, val cafe_info: CafeInfoResponse
)

@Serializable
data class PopUpNotificationResponse(
    val order: Int, val url: String, val image: String, val cafe_info: CafeInfoResponse? = null
)

@Serializable
data class RankingResponse(
    val id: Int, val user: UserResponse, val ranking: Int, val activity: Int
)
