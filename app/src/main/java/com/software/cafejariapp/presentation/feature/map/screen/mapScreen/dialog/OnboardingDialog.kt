package com.software.cafejariapp.presentation.feature.map.screen.mapScreen.dialog

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.software.cafejariapp.R
import com.software.cafejariapp.presentation.component.HorizontalSpacer
import com.software.cafejariapp.presentation.component.VerticalSpacer
import com.software.cafejariapp.presentation.theme.MoreHeavyGray
import com.software.cafejariapp.presentation.theme.Transparent
import com.software.cafejariapp.presentation.theme.White

@OptIn(ExperimentalComposeUiApi::class, ExperimentalPagerApi::class)
@Composable
fun OnboardingDialog(
    onDismiss: () -> Unit,

    pagerState: PagerState = rememberPagerState(pageCount = 4)
){

    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false,
        ),
    ) {
        Card(
            modifier = Modifier
                .width(320.dp),
            backgroundColor = White,
            shape = MaterialTheme.shapes.large
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                VerticalSpacer(height = 12.dp)
                Box(contentAlignment = Alignment.Center) {
                    Row {
                        HorizontalSpacer(width = 4.dp)
                        for (i in 0..3) {
                            if (i == pagerState.currentPage) {
                                Image(
                                    painter = painterResource(id = R.drawable.stamp_icon),
                                    contentDescription = "페이지안내",
                                    modifier = Modifier.size(16.dp)
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Outlined.Circle,
                                    contentDescription = "미선택된페이지",
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            HorizontalSpacer(width = 4.dp)
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(
                            modifier = Modifier.padding(end = 12.dp),
                            onClick = { if(pagerState.currentPage == 3) onDismiss() }
                        ) {
                            Icon(
                                modifier = Modifier.size(32.dp),
                                imageVector = Icons.Rounded.Cancel,
                                contentDescription = "나가기",
                                tint = if(pagerState.currentPage == 3) {
                                    MaterialTheme.colors.primary
                                } else {
                                    Transparent
                                }
                            )
                        }
                    }
                }
                HorizontalPager(
                    state = pagerState,
                    dragEnabled = true,
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) { index ->
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        VerticalSpacer(height = 28.dp)
                        Text(
                            text = OnboardingUnit.onboardingPages[index].title,
                            style = MaterialTheme.typography.h1,
                            color = MaterialTheme.colors.primary
                        )
                        VerticalSpacer(height = 28.dp)
                        Text(
                            text = OnboardingUnit.onboardingPages[index].content1,
                            style = MaterialTheme.typography.subtitle1,
                            color = MoreHeavyGray
                        )
                        Text(
                            text = OnboardingUnit.onboardingPages[index].content2,
                            style = MaterialTheme.typography.subtitle1,
                            color = MoreHeavyGray
                        )
                        Text(
                            text = OnboardingUnit.onboardingPages[index].content3,
                            style = MaterialTheme.typography.subtitle1,
                            color = MoreHeavyGray
                        )
                        VerticalSpacer(height = 12.dp)
                        Image(
                            painter = painterResource(id = OnboardingUnit.onboardingPages[index].imageResourceId),
                            contentDescription = "온보딩",
                            modifier = Modifier.width(200.dp),
                            contentScale = ContentScale.FillWidth
                        )
                    }
                }
            }
        }
    }
}

data class OnboardingUnit(
    val title: String,
    val content1: String,
    val content2: String,
    val content3: String,
    val imageResourceId: Int
) {
    companion object {
        val onboardingPages = listOf(
            OnboardingUnit(
                title = "혼잡도를 확인해보세요",
                content1 = "지도에서 카페를 선택하여",
                content2 = "실시간 혼잡도를 쉽게",
                content3 = "확인하실 수 있습니다",
                imageResourceId = R.drawable.onboarding_0
            ),
            OnboardingUnit(
                title = "혼잡도를 직접 공유하세요",
                content1 = "'혼잡도 공유' 버튼을 통해",
                content2 = "이용하고 계시는 카페의",
                content3 = "혼잡도를 공유할 수 있어요",
                imageResourceId = R.drawable.onboarding_1
            ),
            OnboardingUnit(
                title = "포인트를 모아보세요",
                content1 = "혼잡도를 직접 공유하시면",
                content2 = "활동 시간에 따라",
                content3 = "포인트를 지급해드려요",
                imageResourceId = R.drawable.onboarding_2
            ),
            OnboardingUnit(
                title = "카페를 추가하세요",
                content1 = "원하는 카페가 지도에 없다면",
                content2 = "'카페 추가'버튼을 통해",
                content3 = "등록 요청해보세요",
                imageResourceId = R.drawable.onboarding_3
            )
        )
    }
}