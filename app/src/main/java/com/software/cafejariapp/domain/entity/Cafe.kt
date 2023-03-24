package com.software.cafejariapp.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class Cafe(
    val id: Int,
    val crowded: Int,
    val master: CafeMaster = CafeMaster.empty,
    val floor: Int,
    val wallSocket: String,
    val restroom: String,
    val recentUpdatedLogs: List<CafeRecentLog> = emptyList(),
): Parcelable {
    fun isMasterAvailable(): Boolean {
        return this.master.userId == 0
    }
    fun hasWallSocketInfo(): Boolean {
        return this.wallSocket.isNotBlank()
    }
    fun hasRestroomInfo(): Boolean {
        return this.restroom.isNotBlank()
    }

    companion object{
        val empty = Cafe(0, -1, CafeMaster.empty, 0, "", "", emptyList())
    }
}