package com.software.cafejariapp.presentation.feature.map.screen.masterRoom

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.software.cafejariapp.presentation.component.VerticalSpacer
import com.software.cafejariapp.presentation.util.Crowded
import com.software.cafejariapp.core.toCrowded
import com.software.cafejariapp.presentation.feature.map.component.HorizontalCrowdedColorBar
import com.software.cafejariapp.presentation.theme.HalfTransparentBlack
import com.software.cafejariapp.presentation.theme.MoreLightGray
import com.software.cafejariapp.presentation.theme.White

@Composable
fun CrowdedEditor(
    selectedCrowdedIndex: Int,
    onCrowdedChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    isTapAnimationEnable: Boolean = false
) {

    val isAnimationVisible = rememberSaveable { mutableStateOf(isTapAnimationEnable) }
    val animationColor by rememberInfiniteTransition().animateColor(
        initialValue = Color.Black.copy(0.3f),
        targetValue = Color.Transparent,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Card(
        modifier = modifier.fillMaxWidth(),
        border = BorderStroke(
            width = (1.5).dp,
            color = MoreLightGray
        ),
        shape = MaterialTheme.shapes.large,
        backgroundColor = White,
        elevation = 0.dp
    ) {

        Column(
            modifier = Modifier.padding(
                vertical = 20.dp,
                horizontal = 16.dp
            ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = selectedCrowdedIndex.toCrowded().description,
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.primary,
            )

            Box {

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Crowded.crowdedListWithoutNegative.forEach { crowded ->
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .background(
                                    color = White,
                                    shape = MaterialTheme.shapes.medium
                                )
                                .clickable { onCrowdedChange(crowded.int) }
                                .padding(
                                    top = 20.dp,
                                    bottom = 8.dp
                                ),
                                horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            if (crowded.int == selectedCrowdedIndex) {
                                Image(
                                    painter = painterResource(id = crowded.icon),
                                    contentDescription = "혼잡도",
                                    modifier = Modifier.height(24.dp),
                                    alignment = Alignment.Center
                                )
                            } else {
                                VerticalSpacer(height = 24.dp)
                            }
                        }
                    }
                }

                if (isAnimationVisible.value) {
                    Row(
                        modifier = Modifier
                            .matchParentSize()
                            .background(
                                color = animationColor,
                                shape = MaterialTheme.shapes.medium
                            )
                            .clickable { isAnimationVisible.value = false }
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = "여기를 눌러 혼잡도를 변경해주세요!",
                            color = White
                        )
                    }
                }
            }

            HorizontalCrowdedColorBar()

            VerticalSpacer(height = 4.dp)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    modifier = Modifier
                        .background(
                            color = HalfTransparentBlack,
                            shape = RoundedCornerShape(50)
                        )
                        .padding(
                            horizontal = 8.dp,
                            vertical = 4.dp
                        ),
                    text = Crowded.crowded0.string,
                    style = MaterialTheme.typography.overline,
                    color = White,
                )

                Text(
                    modifier = Modifier
                        .background(
                            color = HalfTransparentBlack,
                            shape = RoundedCornerShape(50)
                        )
                        .padding(
                            horizontal = 8.dp,
                            vertical = 4.dp
                        ),
                    text = Crowded.crowded4.string,
                    style = MaterialTheme.typography.overline,
                    color = White,
                )
            }
        }
    }
}