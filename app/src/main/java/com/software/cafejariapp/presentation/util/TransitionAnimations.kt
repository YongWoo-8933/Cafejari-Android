package com.software.cafejariapp.presentation.util

import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.ui.unit.IntOffset

object TransitionAnimations {
    private val transitionAnimationSpec = tween<IntOffset>(
        delayMillis = 0, durationMillis = 350, easing = FastOutSlowInEasing
    )

    private val fadeAnimationSpec = tween<Float>(
        delayMillis = 0,
        durationMillis = 350,
        easing = FastOutSlowInEasing,
    )

    val fadeInAnimation = fadeIn(animationSpec = fadeAnimationSpec)

    val fadeOutAnimation = fadeOut(animationSpec = fadeAnimationSpec)

    val leftToRightExitTransition = slideOutHorizontally(
        targetOffsetX = { fullWidth: Int -> fullWidth }, animationSpec = transitionAnimationSpec
    ) + fadeOutAnimation

    val rightToLeftEnterTransition = slideInHorizontally(
        initialOffsetX = { fullWidth: Int -> fullWidth }, animationSpec = transitionAnimationSpec
    ) + fadeInAnimation
}