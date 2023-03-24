package com.software.cafejariapp.presentation.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.isUnspecified
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ScaleFactor
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.toSize
import kotlin.math.roundToInt

@Composable
fun ZoomableImage(
    modifier: Modifier = Modifier,
    painter: Painter,
    contentDescription: String? = "카페 이미지들",
    contentScale: ContentScale = ContentScale.Fit,
    zoomable: Boolean = true
) {

    val size = remember {
        mutableStateOf(IntSize.Zero)
    }
    val offsetXState = remember {
        mutableStateOf(0f)
    }
    val offsetYState = remember {
        mutableStateOf(0f)
    }
    val zoomState = remember {
        mutableStateOf(1f)
    }
    val zoomAnimated = animateFloatAsState(targetValue = zoomState.value)

    fun limitOffset() {
        if (painter.intrinsicSize.isUnspecified) {
            return
        }
        val srcSize = Size(painter.intrinsicSize.width, painter.intrinsicSize.height)
        val destSize = size.value.toSize()
        val scaleFactor: ScaleFactor = contentScale.computeScaleFactor(srcSize, destSize)

        val currentWidth = painter.intrinsicSize.width * zoomState.value * scaleFactor.scaleX
        val currentHeight = painter.intrinsicSize.height * zoomState.value * scaleFactor.scaleY

        offsetXState.value = if (currentWidth >= size.value.width) {
            val sizeDiff = (currentWidth - size.value.width) / 2
            offsetXState.value.coerceIn(
                (-sizeDiff)..sizeDiff
            )
        } else {
            val sizeDiff = (size.value.width - currentWidth) / 2
            offsetXState.value.coerceIn(
                -sizeDiff..sizeDiff
            )
        }

        offsetYState.value = if (currentHeight >= size.value.height) {
            val sizeDiff = (currentHeight - size.value.height) / 2
            offsetYState.value.coerceIn(
                (-sizeDiff)..sizeDiff
            )
        } else {
            val sizeDiff = (size.value.height - currentHeight) / 2
            offsetYState.value.coerceIn(
                -sizeDiff..sizeDiff
            )
        }
    }

    Image(
        modifier = modifier
            .onGloballyPositioned {
                size.value = it.size
            }
            .offset {
                IntOffset(offsetXState.value.roundToInt(), offsetYState.value.roundToInt())
            }
            .graphicsLayer {
                if (zoomable) {
                    this.scaleX = zoomAnimated.value
                    this.scaleY = zoomAnimated.value
                }
            }
            .pointerInput(Unit) {
                forEachGesture {
                    awaitPointerEventScope {
                        awaitFirstDown()
                        do {
                            val event = awaitPointerEvent()
                            val offset = event.calculatePan()
                            zoomState.value *= event.calculateZoom()
                            if (zoomState.value < 1f) zoomState.value = 1f
                            if (zoomState.value > 4f) zoomState.value = 4f
                            offsetXState.value += offset.x * zoomState.value
                            offsetYState.value += offset.y * zoomState.value
                            limitOffset()
                            event.changes.forEach { pointerInputChange: PointerInputChange ->
                                pointerInputChange.consume()
                            }
                        } while (event.changes.any { it.pressed })
                    }
                }
            },
        painter = painter,
        contentDescription = contentDescription
    )
}