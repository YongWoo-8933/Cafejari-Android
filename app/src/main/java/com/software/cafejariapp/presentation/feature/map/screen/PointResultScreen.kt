package com.software.cafejariapp.presentation.feature.map.screen

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ThumbUp
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.software.cafejariapp.R
import com.software.cafejariapp.presentation.GlobalState
import com.software.cafejariapp.presentation.component.BorderedCtaButton
import com.software.cafejariapp.presentation.component.CustomAdBanner
import com.software.cafejariapp.presentation.component.PrimaryCtaButton
import com.software.cafejariapp.presentation.component.VerticalSpacer
import com.software.cafejariapp.presentation.feature.map.util.PointResultType
import com.software.cafejariapp.presentation.util.Screen
import com.software.cafejariapp.presentation.util.TimeUtil
import com.software.cafejariapp.presentation.theme.White
import kotlinx.coroutines.delay

@ExperimentalPagerApi
@Composable
fun PointResultScreen(
    globalState: GlobalState,
    point: Int,
    type: PointResultType,
) {

    val isAnimationInitiated = rememberSaveable { mutableStateOf(false) }
    val animateDp = animateDpAsState(
        targetValue = if (isAnimationInitiated.value) 0.dp else (-400).dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioHighBouncy,
            stiffness = Spring.StiffnessMediumLow
        )
    )

    LaunchedEffect(Unit) {
        delay(1500L)
        isAnimationInitiated.value = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = White)
            .statusBarsPadding()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        if (type == PointResultType.ThumbsUp) {
            Icon(
                imageVector = Icons.Rounded.ThumbUp,
                contentDescription = "마스터일반추천",
                modifier = Modifier.size(64.dp).offset(x = 0.dp, y = animateDp.value)
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.coin),
                contentDescription = "돈 획득",
                modifier = Modifier.offset(x = 0.dp, y = animateDp.value)
            )
        }

        VerticalSpacer(height = 30.dp)

        if (type == PointResultType.MasterExpiredWithAd || type == PointResultType.MasterExpired) {
            Text(
                text = if (globalState.masterCafeLog.value.start.isNotBlank() && globalState.masterCafeLog.value.finish.isNotBlank()) {
                    "(총 ${
                        TimeUtil.getPassingTimeFromTo(
                            globalState.masterCafeLog.value.start,
                            globalState.masterCafeLog.value.finish
                        )
                    } 활동)"
                } else {
                    ""
                },
                color = MaterialTheme.colors.primary
            )

            VerticalSpacer(height = 12.dp)
        }

        Text(
            text = "${point}P 획득!",
            fontSize = 32.sp,
            color = MaterialTheme.colors.primary,
            fontWeight = FontWeight.Bold
        )

        VerticalSpacer(height = 8.dp)

        Text(
            text = when (type) {
                PointResultType.MasterExpiredWithAd -> {
                    "광고를 보고 ${point / 3}P 추가 획득!"
                }
                PointResultType.MasterExpired -> {
                    "광고 보고 종료하면 포인트가 1.5배!"
                }
                PointResultType.ThumbsUpWithAd -> {
                    "광고보고 추천하여 ${point}P 획득!"
                }
                else -> {
                    "마스터 추천 성공!\n\n다음부터는 광고보고 추천하여\n${point}P 받아가세요"
                }
            },
            fontSize = 20.sp,
            color = MaterialTheme.colors.primary,
            textAlign = TextAlign.Center
        )

        VerticalSpacer(height = 80.dp)

        Text(
            text = "지금까지 모은 포인트: ${globalState.user.value.point}P",
            style = MaterialTheme.typography.subtitle1,
            color = MaterialTheme.colors.primaryVariant
        )

        VerticalSpacer(height = 12.dp)

        PrimaryCtaButton(
            text = if (type == PointResultType.MasterExpired || type == PointResultType.MasterExpiredWithAd) {
                "마스터 랭크 보러가기"
            } else {
                "포인트 상점으로 가기"
            },
            onClick = {
                if (type == PointResultType.MasterExpired || type == PointResultType.MasterExpiredWithAd) {
                    globalState.navController.navigate(Screen.LeaderBoardScreen.route) {
                        popUpTo(Screen.MapScreen.route) { inclusive = false }
                    }
                } else {
                    globalState.navController.navigate(Screen.ShopScreen.route) {
                        popUpTo(Screen.MapScreen.route) { inclusive = false }
                    }
                }
            }
        )

        VerticalSpacer(height = 10.dp)

        BorderedCtaButton(
            text = if (type == PointResultType.MasterExpired || type == PointResultType.MasterExpiredWithAd) {
                "내 마스터 활동 보러가기"
            } else {
                "홈 화면으로 가기"
            },
            onClick = {
                if (type == PointResultType.MasterExpired || type == PointResultType.MasterExpiredWithAd) {
                    globalState.navController.navigate(Screen.ProfileKalendarScreen.route) {
                        popUpTo(Screen.MapScreen.route) { inclusive = false }
                    }
                } else {
                    globalState.navController.navigate(Screen.MapScreen.route) {
                        popUpTo(Screen.MapScreen.route) { inclusive = true }
                    }
                }
            }
        )
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {

        CustomAdBanner()
    }
}