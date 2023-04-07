package com.software.cafejariapp.presentation.feature.main.screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.software.cafejariapp.core.customPlaceholder
import com.software.cafejariapp.domain.entity.Leader
import com.software.cafejariapp.presentation.feature.main.component.ProfileImage
import com.software.cafejariapp.presentation.GlobalState
import com.software.cafejariapp.presentation.component.*
import com.software.cafejariapp.presentation.util.TimeUtil
import com.software.cafejariapp.presentation.feature.main.event.LeaderBoardEvent
import com.software.cafejariapp.presentation.feature.main.viewModel.LeaderBoardViewModel
import com.software.cafejariapp.presentation.theme.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@ExperimentalPagerApi
@Composable
fun LeaderBoardScreen(
    globalState: GlobalState,
    leaderBoardViewModel: LeaderBoardViewModel
) {

    val leaderBoardState = leaderBoardViewModel.state.value
    val pagerState = rememberPagerState(pageCount = 3)
    val scope = rememberCoroutineScope()

    NetworkChecker(globalState)

    LaunchedEffect(Unit) {
        leaderBoardViewModel.onEvent(LeaderBoardEvent.GetLeaders(globalState = globalState))
    }

    Scaffold(
        modifier = Modifier.fillMaxWidth(),
        topBar = {
            BaseTopAppBar(
                isBackButtonEnable = false,
                title = "명예의 카페지기",
                isScrolled = true
            )
        },
        backgroundColor = MoreLightGray
    ) { paddingValue ->

        CustomSwipeRefresh(
            isLoading = leaderBoardState.isLeadersLoading,
            onRefresh = { leaderBoardViewModel.onEvent(LeaderBoardEvent.GetLeaders(globalState = globalState)) }
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = paddingValue.calculateTopPadding())
                    .customPlaceholder(leaderBoardState.isLeadersLoading)
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = White)
                        .padding(horizontal = 40.dp)
                ) {

                    TabRow(
                        modifier = Modifier.height(48.dp),
                        selectedTabIndex = pagerState.currentPage,
                        divider = { TabRowDefaults.Divider(color = Transparent) },
                        backgroundColor = White,
                        indicator = {
                            TabRowDefaults.Indicator(
                                modifier = Modifier
                                    .tabIndicatorOffset(it[pagerState.currentPage])
                                    .padding(horizontal = 28.dp),
                                color = MaterialTheme.colors.primary,
                            )
                        }
                    ) {

                        listOf(
                            RankingType.Week,
                            RankingType.Month,
                            RankingType.Total
                        ).forEach { type ->
                            Tab(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .background(color = White),
                                selected = pagerState.currentPage == type.index,
                                onClick = {
                                    scope.launch { pagerState.scrollToPage(type.index) }
                                },
                                text = {
                                    Text(
                                        text = type.string,
                                        style = MaterialTheme.typography.subtitle2,
                                        color = if (pagerState.currentPage == type.index) {
                                            MaterialTheme.colors.primary
                                        } else {
                                            LightGray
                                        }
                                    )
                                }
                            )
                        }
                    }
                }

                Column(
                    modifier = Modifier
                        .background(color = White)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    VerticalSpacer(height = 20.dp)

                    Card(
                        backgroundColor = MoreLightGray,
                        shape = RoundedCornerShape(50),
                    ) {

                        Text(
                            modifier = Modifier.padding(
                                vertical = 8.dp,
                                horizontal = 12.dp
                            ),
                            style = MaterialTheme.typography.caption,
                            text = when(pagerState.currentPage) {
                                RankingType.Week.index -> TimeUtil.getFirstDayOfThisWeek() + "  ~  오늘"
                                RankingType.Month.index -> "${LocalDateTime.now().year}년 ${LocalDateTime.now().monthValue}월 1일  ~  오늘"
                                RankingType.Total.index -> "2022년 10월 1일  ~  오늘"
                                else -> ""
                            },
                        )
                    }

                    VerticalSpacer(height = 20.dp)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.Center
                    ) {

                        when (pagerState.currentPage) {
                            RankingType.Week.index -> {
                                TopLeaderPart(
                                    leaders = leaderBoardState.weekLeaders
                                )
                            }
                            RankingType.Month.index -> {
                                TopLeaderPart(
                                    leaders = leaderBoardState.monthLeaders
                                )
                            }
                            RankingType.Total.index -> {
                                TopLeaderPart(
                                    leaders = leaderBoardState.totalLeaders
                                )
                            }
                        }
                    }

                    VerticalSpacer(height = 28.dp)
                }

                Column {

                    HorizontalPager(
                        state = pagerState
                    ) { index ->

                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            content = {

                                item {
                                    when (index) {
                                        RankingType.Week.index -> {
                                            BottomLeaderPart(
                                                leaders = leaderBoardState.weekLeaders,
                                                isLoading = leaderBoardState.isLeadersLoading
                                            )
                                        }
                                        RankingType.Month.index -> {
                                            BottomLeaderPart(
                                                leaders = leaderBoardState.monthLeaders,
                                                isLoading = leaderBoardState.isLeadersLoading
                                            )
                                        }
                                        RankingType.Total.index -> {
                                            BottomLeaderPart(
                                                leaders = leaderBoardState.totalLeaders,
                                                isLoading = leaderBoardState.isLeadersLoading
                                            )
                                        }
                                    }

                                    VerticalSpacer(height = 70.dp)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun LeaderUnit(
    index: Int,
    masterNickname: String,
    activityTimeString: String,

    modifier: Modifier = Modifier,
    color: Color,
    imageModel: Any?,
) {

    Card(
        modifier = modifier,
        backgroundColor = MoreLightGray,
        elevation = 0.dp
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                modifier = Modifier.padding(start = 20.dp),
                text = "${index + 1} ",
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.body2,
                color = color
            )

            HorizontalSpacer(width = 12.dp)

            ProfileImage(
                image = imageModel,
                modifier = Modifier.size(50.dp)
            )

            HorizontalSpacer(width = 12.dp)

            Text(
                text = masterNickname,
                style = MaterialTheme.typography.body1,
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 20.dp),
                text = activityTimeString,
                textAlign = TextAlign.End,
                style = MaterialTheme.typography.body2
            )
        }
    }
}

@Composable
fun TopLeaderUnit(
    profileImage: Any?,
    rankingText: String,
    nickname: String,
    activityTimeText: String,
    profileImageSize: Dp,
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = rankingText,
            style = MaterialTheme.typography.body2,
            color = MaterialTheme.colors.primary
        )

        VerticalSpacer(height = 4.dp)

        ProfileImage(
            image = profileImage,
            modifier = Modifier.size(profileImageSize),
            border = BorderStroke(
                width = (1.5).dp,
                color = MaterialTheme.colors.primary
            )
        )

        VerticalSpacer(height = 10.dp)

        Text(
            text = nickname,
            textAlign = TextAlign.Center
        )

        Text(
            text = activityTimeText,
            style = MaterialTheme.typography.button
        )
    }
}

@Composable
fun TopLeaderPart(
    leaders: List<Leader>
) {

    when (leaders.size) {
        0 -> {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {

                Text(
                    modifier = Modifier.padding(vertical = 30.dp),
                    text = "집계된 랭킹이 없습니다",
                    style = MaterialTheme.typography.subtitle2
                )
            }
        }
        1 -> {
            TopLeaderUnit(
                profileImage = leaders[0].userImage,
                rankingText = "TOP1",
                nickname = leaders[0].nickname,
                activityTimeText = TimeUtil.getHourMinuteFromSecond(leaders[0].activity),
                profileImageSize = 108.dp
            )
        }
        2 -> {
            TopLeaderUnit(
                profileImage = leaders[0].userImage,
                rankingText = "TOP1",
                nickname = leaders[0].nickname,
                activityTimeText = TimeUtil.getHourMinuteFromSecond(leaders[0].activity),
                profileImageSize = 108.dp
            )

            HorizontalSpacer(width = 16.dp)

            TopLeaderUnit(
                profileImage = leaders[1].userImage,
                rankingText = "TOP2",
                nickname = leaders[1].nickname,
                activityTimeText = TimeUtil.getHourMinuteFromSecond(leaders[1].activity),
                profileImageSize = 84.dp
            )
        }
        else -> {
            TopLeaderUnit(
                profileImage = leaders[1].userImage,
                rankingText = "TOP2",
                nickname = leaders[1].nickname,
                activityTimeText = TimeUtil.getHourMinuteFromSecond(leaders[1].activity),
                profileImageSize = 84.dp
            )

            HorizontalSpacer(width = 16.dp)

            TopLeaderUnit(
                profileImage = leaders[0].userImage,
                rankingText = "TOP1",
                nickname = leaders[0].nickname,
                activityTimeText = TimeUtil.getHourMinuteFromSecond(leaders[0].activity),
                profileImageSize = 108.dp
            )

            HorizontalSpacer(width = 16.dp)

            TopLeaderUnit(
                profileImage = leaders[2].userImage,
                rankingText = "TOP3",
                nickname = leaders[2].nickname,
                activityTimeText = TimeUtil.getHourMinuteFromSecond(leaders[2].activity),
                profileImageSize = 84.dp
            )
        }
    }
}

@Composable
fun BottomLeaderPart(
    leaders: List<Leader>,
    isLoading: Boolean
) {

    if (isLoading) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(80.dp)
                .padding(20.dp),
            color = HeavyGray
        )
    } else {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {

            leaders.forEachIndexed { index, ranking ->
                if (index != 0 && index != 1 && index != 2) {
                    LeaderUnit(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(70.dp),
                        index = index,
                        masterNickname = ranking.nickname,
                        activityTimeString = TimeUtil.getHourMinuteFromSecond(ranking.activity),
                        color = TextBlack,
                        imageModel = leaders[index].userImage,
                    )
                }
            }
        }
    }
}

sealed class RankingType(
    val index: Int,
    val string: String
) {
    object Week : RankingType(0, "주간")
    object Month : RankingType(1, "월간")
    object Total : RankingType(2, "누적")
}