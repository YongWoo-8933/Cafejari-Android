package com.software.cafejariapp.presentation.feature.main.screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.software.cafejariapp.domain.entity.Ranker
import com.software.cafejariapp.presentation.feature.main.component.ProfileImage
import com.software.cafejariapp.presentation.GlobalState
import com.software.cafejariapp.presentation.component.*
import com.software.cafejariapp.presentation.util.Time
import com.software.cafejariapp.presentation.feature.main.event.LeaderBoardEvent
import com.software.cafejariapp.presentation.feature.main.viewModel.LeaderBoardViewModel
import com.software.cafejariapp.presentation.theme.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@ExperimentalPagerApi
@Composable
fun LeaderBoardScreen(
    globalState: GlobalState,
    leaderBoardViewModel: LeaderBoardViewModel
) {

    val leaderBoardState = leaderBoardViewModel.state.value
    val pagerState = rememberPagerState(pageCount = 2)
    val scope = rememberCoroutineScope()

    NetworkChecker(globalState)

    LaunchedEffect(Unit) {
        leaderBoardViewModel.onEvent(LeaderBoardEvent.Init(globalState = globalState))
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

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = paddingValue.calculateTopPadding())
        ) {

            Row(
                modifier = Modifier
                    .background(color = White)
                    .fillMaxWidth()
                    .padding(vertical = 20.dp),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.Center
            ) {

                when(pagerState.currentPage) {
                    0 -> {
                        TopLeaderPart(
                            leaders = leaderBoardState.rankingMonthList
                        )
                    }
                    1 -> {
                        TopLeaderPart(
                            leaders = leaderBoardState.rankingWeekList
                        )
                    }
                }
            }

            Column {

                TabRow(
                    modifier = Modifier.height(48.dp),
                    selectedTabIndex = pagerState.currentPage,
                    divider = { TabRowDefaults.Divider(color = Transparent) },
                    indicator = {
                        TabRowDefaults.Indicator(
                            modifier = Modifier
                                .tabIndicatorOffset(it[pagerState.currentPage])
                                .padding(horizontal = 10.dp),
                            color = MaterialTheme.colors.primary,
                        )
                    }
                ) {

                    listOf("월간", "주간").forEachIndexed { index, text ->
                        Tab(
                            modifier = Modifier
                                .fillMaxHeight()
                                .background(color = White),
                            selected = pagerState.currentPage == index,
                            onClick = {
                                scope.launch { pagerState.scrollToPage(index) }
                            },
                            text = {
                                Text(
                                    text = text,
                                    style = MaterialTheme.typography.subtitle2,
                                    color = MaterialTheme.colors.primary
                                )
                            }
                        )
                    }
                }

                HorizontalPager(
                    state = pagerState
                ) { index ->

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        content = {

                            item {
                                when (index) {
                                    0 -> {
                                        BottomLeaderPart(
                                            leaders = leaderBoardState.rankingMonthList,
                                            isLoading = leaderBoardState.isRankingListLoading
                                        )
                                    }
                                    1 -> {
                                        BottomLeaderPart(
                                            leaders = leaderBoardState.rankingWeekList,
                                            isLoading = leaderBoardState.isRankingListLoading
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
    leaders: List<Ranker>
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
                activityTimeText = Time.getHourMinuteFromSecond(leaders[0].activity),
                profileImageSize = 108.dp
            )
        }
        2 -> {
            TopLeaderUnit(
                profileImage = leaders[0].userImage,
                rankingText = "TOP1",
                nickname = leaders[0].nickname,
                activityTimeText = Time.getHourMinuteFromSecond(leaders[0].activity),
                profileImageSize = 108.dp
            )

            HorizontalSpacer(width = 16.dp)

            TopLeaderUnit(
                profileImage = leaders[1].userImage,
                rankingText = "TOP2",
                nickname = leaders[1].nickname,
                activityTimeText = Time.getHourMinuteFromSecond(leaders[1].activity),
                profileImageSize = 84.dp
            )
        }
        else -> {
            TopLeaderUnit(
                profileImage = leaders[1].userImage,
                rankingText = "TOP2",
                nickname = leaders[1].nickname,
                activityTimeText = Time.getHourMinuteFromSecond(leaders[1].activity),
                profileImageSize = 84.dp
            )

            HorizontalSpacer(width = 16.dp)

            TopLeaderUnit(
                profileImage = leaders[0].userImage,
                rankingText = "TOP1",
                nickname = leaders[0].nickname,
                activityTimeText = Time.getHourMinuteFromSecond(leaders[0].activity),
                profileImageSize = 108.dp
            )

            HorizontalSpacer(width = 16.dp)

            TopLeaderUnit(
                profileImage = leaders[2].userImage,
                rankingText = "TOP3",
                nickname = leaders[2].nickname,
                activityTimeText = Time.getHourMinuteFromSecond(leaders[2].activity),
                profileImageSize = 84.dp
            )
        }
    }
}

@Composable
fun BottomLeaderPart(
    leaders: List<Ranker>,
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
                        activityTimeString = Time.getHourMinuteFromSecond(ranking.activity),
                        color = TextBlack,
                        imageModel = leaders[index].userImage,
                    )
                }
            }
        }
    }
}