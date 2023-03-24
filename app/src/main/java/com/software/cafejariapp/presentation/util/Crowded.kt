package com.software.cafejariapp.presentation.util

import androidx.compose.ui.graphics.Color
import com.software.cafejariapp.R
import com.software.cafejariapp.presentation.theme.*

data class Crowded (
    val int: Int,
    val string: String,
    val icon: Int,
    val color: Color,
    val complementaryColor: Color,
    val description: String
) {
    companion object {
        val crowdedNegative = Crowded(-1, "정보없음", R.drawable.cafe_icon, Gray, White, "정보가 없어요")
        val crowded0 = Crowded(0, "한적", R.drawable.cafe_icon_0, CrowdedBlue, White,"카페가 한적해요")
        val crowded1 = Crowded(1, "여유", R.drawable.cafe_icon_1, CrowdedGreen, TextBlack,"카페가 여유로워요")
        val crowded2 = Crowded(2, "보통", R.drawable.cafe_icon_2, CrowdedYellow, TextBlack,"보통이에요")
        val crowded3 = Crowded(3, "혼잡", R.drawable.cafe_icon_3, CrowdedOrange, White,"카페가 붐벼요")
        val crowded4 = Crowded(4, "만석", R.drawable.cafe_icon_4, CrowdedRed, White,"카페가 거의 만석이에요")
        val crowdedListWithoutNegative = listOf(crowded0, crowded1, crowded2, crowded3, crowded4)
    }
}