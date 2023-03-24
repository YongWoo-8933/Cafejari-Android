package com.software.cafejariapp.presentation.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

@Composable
fun BottomSheetShape() = object : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection:
        LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Rounded(
            roundRect = RoundRect(
                left = 0f,
                top = 0f,
                right = size.width,
                bottom = size.height,
                topLeftCornerRadius = CornerRadius(size.width * 0.06f, size.width * 0.06f),
                topRightCornerRadius = CornerRadius(size.width * 0.06f, size.width * 0.06f),
                bottomLeftCornerRadius = CornerRadius(0f, 0f),
                bottomRightCornerRadius = CornerRadius(0f, 0f)
            )
        )
    }
}