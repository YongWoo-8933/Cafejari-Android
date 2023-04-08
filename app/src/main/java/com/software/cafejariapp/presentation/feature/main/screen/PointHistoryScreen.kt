package com.software.cafejariapp.presentation.feature.main.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.software.cafejariapp.presentation.GlobalState
import com.software.cafejariapp.presentation.util.Time
import com.software.cafejariapp.core.toFloor
import com.software.cafejariapp.domain.entity.CafeLog
import com.software.cafejariapp.domain.entity.EventPointHistory
import com.software.cafejariapp.presentation.component.*
import com.software.cafejariapp.presentation.feature.main.event.ProfileEvent
import com.software.cafejariapp.presentation.feature.main.viewModel.ProfileViewModel
import com.software.cafejariapp.presentation.theme.Gray
import com.software.cafejariapp.presentation.theme.TextBlack
import com.software.cafejariapp.presentation.theme.Transparent
import com.software.cafejariapp.presentation.theme.White
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun PointHistoryScreen(
    globalState: GlobalState,
    profileViewModel: ProfileViewModel,
) {

    val profileState = profileViewModel.state.value
    val pagerState: PagerState = rememberPagerState(pageCount = 2)
    val scope = rememberCoroutineScope()

    NetworkChecker(globalState)

    LaunchedEffect(Unit) {
        profileViewModel.onEvent(ProfileEvent.PointHistoryScreenInit(globalState))
    }

    Scaffold(
        topBar = {
            BaseTopAppBar(
                isBackButtonEnable = true,
                onBackButtonClick = { globalState.navController.popBackStack() },
                title = "포인트 지급내역",
                isScrolled = true
            )
        },
        backgroundColor = White
    ) { paddingValue ->

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = paddingValue.calculateTopPadding())
        ) {

            Column {

                TabRow(
                    modifier = Modifier.height(50.dp),
                    selectedTabIndex = pagerState.currentPage,
                    backgroundColor = White,
                    divider = { TabRowDefaults.Divider(color = Transparent) },
                    indicator = {
                        TabRowDefaults.Indicator(
                            modifier = Modifier
                                .tabIndicatorOffset(it[pagerState.currentPage])
                                .padding(horizontal = 12.dp),
                            color = MaterialTheme.colors.primary,
                        )
                    }
                ) {

                    listOf("이벤트", "혼잡도 공유").forEachIndexed { index, text ->
                        Tab(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            selected = pagerState.currentPage == index,
                            onClick = {
                                scope.launch { pagerState.scrollToPage(index) }
                            },
                            text = {
                                Text(
                                    text = text,
                                    color = if (pagerState.currentPage == index) TextBlack else Gray
                                )
                            }
                        )
                    }
                }

                HorizontalPager(
                    state = pagerState
                ) { index ->

                    when {
                        index == 0 && profileState.isEventPointHistoryLoading -> {
                            FullSizeLoadingScreen(loadingText = "이벤트 내역 로딩중..")
                        }
                        index == 1 && profileState.isHistoryCafeLogLoading -> {
                            FullSizeLoadingScreen(loadingText = "마스터 활동내역 로딩중..")
                        }
                        index == 0 && profileState.eventPointHistories.isEmpty() -> {
                            EmptyScreen("이벤트로 받은 포인트 내역이 없습니다")
                        }
                        index == 1 && profileState.historyCafeLogs.isEmpty() -> {
                            EmptyScreen("혼잡도공유로 받은 포인트 내역이 없습니다")
                        }
                        index == 0 && profileState.eventPointHistories.isNotEmpty() -> {
                            EventPointPart(eventPointHistories = profileState.eventPointHistories)
                        }
                        index == 1 && profileState.historyCafeLogs.isNotEmpty() -> {
                            CafeLogsPart(cafeLogs = profileState.historyCafeLogs)
                        }
                        else -> {  }
                    }
                }
            }
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {

            CustomAdBanner()
        }
    }
}

@Composable
fun EventPointPart(
    eventPointHistories: List<EventPointHistory>
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {

        items(eventPointHistories) { eventPointHistory ->

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 16.dp,
                        vertical = 20.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(3f),
                ) {

                    Text(
                        text = eventPointHistory.content,
                        style = MaterialTheme.typography.body2,
                    )

                    VerticalSpacer(height = 4.dp)

                    Row {

                        Text(
                            text = "${Time.getLocalDate(eventPointHistory.time)}",
                            style = MaterialTheme.typography.caption,
                            color = Gray
                        )

                        HorizontalSpacer(width = 8.dp)

                        Text(
                            text = "지급완료",
                            style = MaterialTheme.typography.overline,
                            color = White,
                            modifier = Modifier
                                .background(
                                    color = MaterialTheme.colors.secondary,
                                    shape = RoundedCornerShape(4.dp)
                                )
                                .padding(
                                    vertical = 2.dp,
                                    horizontal = 4.dp
                                )
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .fillMaxHeight(),
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Center
                ) {

                    Text(
                        text = "+${eventPointHistory.point}P",
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.primary
                    )
                }
            }

            BaseDivider()
        }

        item {

            VerticalSpacer(height = 100.dp)
        }
    }
}

@Composable
fun CafeLogsPart(
    cafeLogs: List<List<CafeLog>>
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {

        cafeLogs.forEach { cafeLogs ->

            items(cafeLogs) { cafeLog ->

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 16.dp,
                            vertical = 20.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(3f),
                    ) {

                        Text(
                            text = "${cafeLog.name} ${cafeLog.floor.toFloor()}층",
                            style = MaterialTheme.typography.body2,
                        )

                        VerticalSpacer(height = 4.dp)

                        Row {
                            Text(
                                text = "${Time.getLocalDate(cafeLog.start)}",
                                style = MaterialTheme.typography.caption,
                                color = Gray
                            )

                            HorizontalSpacer(width = 8.dp)

                            Text(
                                text = "${Time.getHourMinute(cafeLog.start)} ~ ${
                                    Time.getHourMinute(
                                        cafeLog.finish
                                    )
                                }",
                                style = MaterialTheme.typography.overline,
                                color = White,
                                modifier = Modifier
                                    .background(
                                        color = MaterialTheme.colors.secondary,
                                        shape = RoundedCornerShape(4.dp)
                                    )
                                    .padding(
                                        vertical = 2.dp,
                                        horizontal = 4.dp
                                    )
                            )
                        }
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .fillMaxHeight(),
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.Center
                    ) {

                        Text(
                            text = "+${cafeLog.point}P",
                            style = MaterialTheme.typography.body2,
                            color = MaterialTheme.colors.primary
                        )
                    }
                }

                BaseDivider()
            }
        }

        item {

            VerticalSpacer(height = 100.dp)
        }
    }
}