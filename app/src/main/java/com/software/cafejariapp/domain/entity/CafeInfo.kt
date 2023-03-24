package com.software.cafejariapp.domain.entity

import android.os.Parcelable
import com.software.cafejariapp.presentation.util.Crowded
import com.software.cafejariapp.core.toCrowded
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class CafeInfo(
    val id: Int,
    val name: String,
    val city: String,
    val gu: String,
    val address: String,
    val totalFloor: Int,
    val firstFloor: Int,
    val latitude: Double,
    val longitude: Double,
    val googlePlaceId: String,
    val cafes: List<Cafe> = emptyList(),
    val moreInfo: MoreInfo = MoreInfo.empty
):Parcelable {
    fun isAssociated(): Boolean {
        return this.moreInfo.id != 0
    }
    fun getMinCrowded(): Crowded {
        var minCrowdedInt = -1

        this.cafes.forEach { cafe ->
            if (cafe.crowded != -1) {
                if (minCrowdedInt == -1) {
                    minCrowdedInt = cafe.crowded
                } else {
                    if (minCrowdedInt > cafe.crowded) minCrowdedInt = cafe.crowded
                }
            }
        }

        return minCrowdedInt.toCrowded()
    }
    fun getRestroomExistCafes(): List<Cafe> {
        val res = mutableListOf<Cafe>()
        this.cafes.forEach { cafe ->
            if (cafe.hasRestroomInfo()) {
                res.add(cafe)
            }
        }
        return res
    }
    fun getWallSocketExistCafes(): List<Cafe> {
        val res = mutableListOf<Cafe>()
        this.cafes.forEach { cafe ->
            if (cafe.hasWallSocketInfo()) {
                res.add(cafe)
            }
        }
        return res
    }

    fun getTopFloor(): String {
        return if (this.firstFloor < 0) {
            if (this.firstFloor + this.totalFloor < 0) {
                (this.firstFloor + this.totalFloor - 1).toString()
            } else if (this.firstFloor + this.totalFloor == 0) {
                this.firstFloor.toString()
            } else {
                (this.firstFloor + this.totalFloor).toString()
            }
        } else {
            (this.firstFloor + this.totalFloor - 1).toString()
        }
    }

    companion object {
        val empty = CafeInfo(0, "", "", "", "",
            0, 0, 0.0, 0.0, "_none")
    }
}