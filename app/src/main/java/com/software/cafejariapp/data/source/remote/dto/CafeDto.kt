package com.software.cafejariapp.data.source.remote.dto

import android.os.Parcelable
import com.software.cafejariapp.domain.entity.CafeDetailLog
import com.software.cafejariapp.domain.entity.CafeLog
import com.software.cafejariapp.domain.entity.CafeMaster
import com.software.cafejariapp.domain.entity.MoreInfo
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable


@Serializable
data class AddAdPointRequest(
    val cafe_log_id: Int
)

@Parcelize
@Serializable
data class AutoExpiredLogResponse(
    val id: Int = 0,
    val time: String = "",
    val cafe_log: AutoExpiredLogCafeLogResponse = AutoExpiredLogCafeLogResponse.empty,
) : Parcelable


@Parcelize
@Serializable
data class AutoExpiredLogCafeLogResponse(
    val id: Int = 0,
    val start: String,
    val finish: String,
    val expired: Boolean,
    val point: Int,
    val cafe: AutoExpiredLogCafeResponse
) : Parcelable {
    companion object {
        val empty = AutoExpiredLogCafeLogResponse(
            0, "", "", true, 0, AutoExpiredLogCafeResponse(
                0, 1, AutoExpiredLogCafeInfoResponse(0, "", 0.0, 0.0)
            )
        )
    }
}

@Parcelize
@Serializable
data class AutoExpiredLogCafeResponse(
    val id: Int,
    val floor: Int,
    val cafe_info: AutoExpiredLogCafeInfoResponse,
) : Parcelable

@Parcelize
@Serializable
data class AutoExpiredLogCafeInfoResponse(
    val id: Int,
    val name: String,
    val latitude: Double,
    val longitude: Double,
) : Parcelable

@Parcelize
@Serializable
data class CafeInfoResponse(
    val id: Int,
    val name: String,
    val city: String,
    val gu: String,
    val address: String,
    val total_floor: Int,
    val floor: Int,
    val latitude: Double,
    val longitude: Double,
    val google_place_id: String,
    val cafe: List<CafeInfoCafeResponse> = emptyList(),
    val more_info: List<MoreInfo> = emptyList()
) : Parcelable

@Parcelize
@Serializable
data class CafeInfoCafeResponse(
    val id: Int,
    val master: UserResponse? = UserResponse.empty,
    val floor: Int,
    val wall_socket: String? = "",
    val restroom: String? = "",
    val recent_updated_log: List<CafeInfoCafeRecentLogResponse> = emptyList(),
) : Parcelable


@Parcelize
@Serializable
data class CafeInfoCafeRecentLogResponse(
    val id: Int, val update: String, val cafe_detail_log: CafeInfoCafeDetailLogResponse
) : Parcelable


@Parcelize
@Serializable
data class CafeInfoCafeDetailLogResponse(
    val id: Int, val update: String, val crowded: Int, val cafe_log: CafeInfoCafeLogResponse
) : Parcelable


@Parcelize
@Serializable
data class CafeInfoCafeLogResponse(
    val id: Int,
    val expired: Boolean,
    val master: UserResponse? = UserResponse.empty,
) : Parcelable

@Parcelize
@Serializable
data class CafeLogResponse(
    val id: Int,
    val start: String,
    val finish: String,
    val expired: Boolean,
    val point: Int,
    val update_period: Int = 30,
    val cafe_detail_log: List<CafeDetailLog> = emptyList(),
    val cafe: CafeLogCafeResponse,
    val master: UserResponse
) : Parcelable {

    fun toCafeLog(): CafeLog {
        return CafeLog(
            id = this.id,
            cafeId = this.cafe.id,
            name = this.cafe.cafe_info.name,
            latitude = this.cafe.cafe_info.latitude,
            longitude = this.cafe.cafe_info.longitude,
            floor = this.cafe.floor,
            start = this.start,
            finish = this.finish,
            expired = this.expired,
            point = this.point,
            updatePeriod = this.update_period,
            master = CafeMaster(
                userId = this.master.id,
                nickname = this.master.profile.nickname,
                grade = this.master.profile.grade,
            ),
            cafeDetailLogs = this.cafe_detail_log
        )
    }
}


@Parcelize
@Serializable
data class CafeLogCafeResponse(
    val id: Int,
    val floor: Int,
    val cafe_info: CafeLogCafeInfoResponse,
) : Parcelable


@Parcelize
@Serializable
data class CafeLogCafeInfoResponse(
    val id: Int,
    val name: String,
    val city: String,
    val gu: String,
    val address: String,
    val total_floor: Int,
    val latitude: Double,
    val longitude: Double
) : Parcelable

@Serializable
data class ExpireMasterRequest(
    val cafe_log_id: Int, val ad_multiple_applied: Boolean
)

@Serializable
data class RegisterMasterRequest(
    val cafe_id: Int,
    val update_period: Int,
    val crowded: Int
)

@Serializable
data class ThumbsUpRequest(
    val recent_log_id: Int, val ad_thumbs_up: Boolean
)

@Serializable
data class UpdateCrowdedRequest(
    val cafe_log_id: Int,
    val crowded: Int,
)