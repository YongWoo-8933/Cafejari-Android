package com.software.cafejariapp.presentation.feature.main.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.himanshoe.kalendar.common.KalendarKonfig
import com.himanshoe.kalendar.common.KalendarSelector
import com.himanshoe.kalendar.common.KalendarStyle
import com.himanshoe.kalendar.common.YearRange
import com.software.cafejariapp.R
import com.software.cafejariapp.presentation.GlobalState
import com.software.cafejariapp.presentation.feature.main.component.CustomKalendar
import com.software.cafejariapp.core.toFloor
import com.software.cafejariapp.domain.entity.CafeLog
import com.software.cafejariapp.presentation.component.*
import com.software.cafejariapp.presentation.feature.main.event.ProfileEvent
import com.software.cafejariapp.presentation.feature.main.viewModel.ProfileViewModel
import com.software.cafejariapp.presentation.theme.HeavyGray
import com.software.cafejariapp.presentation.theme.LightGray
import com.software.cafejariapp.presentation.theme.TextBlack
import com.software.cafejariapp.presentation.theme.White
import com.software.cafejariapp.presentation.util.BottomSheetShape
import com.software.cafejariapp.presentation.util.Time
import kotlinx.coroutines.launch
import java.time.YearMonth

@OptIn(ExperimentalMaterialApi::class)
@ExperimentalPagerApi
@Composable
fun ProfileKalendarScreen(
    globalState: GlobalState,
    profileViewModel: ProfileViewModel,
) {

    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState()
    val profileState = profileViewModel.state.value
    val scope = rememberCoroutineScope()

    NetworkChecker(globalState)

    LaunchedEffect(Unit) {
        profileViewModel.onEvent(ProfileEvent.ProfileKalendarScreenInit(globalState))
    }

    BackHandler(
        enabled = true,
        onBack = {
            when {
                bottomSheetScaffoldState.bottomSheetState.isExpanded -> {
                    scope.launch { bottomSheetScaffoldState.bottomSheetState.collapse() }
                }
                else -> globalState.navController.popBackStack()
            }
        }
    )

    BottomSheetScaffold(
        modifier = Modifier.fillMaxWidth(),
        scaffoldState = bottomSheetScaffoldState,
        sheetGesturesEnabled = true,
        sheetShape = BottomSheetShape(),
        sheetPeekHeight = 0.dp,
        sheetElevation = 5.dp,
        topBar = {
            BaseTopAppBar(
                isBackButtonEnable = true,
                onBackButtonClick = { globalState.navController.popBackStack() },
                title = "내 혼잡도 공유활동",
                isScrolled = true
            )
        },
        sheetContentColor = White,
        sheetContent = {
            ProfileKalendarBottomSheetContent(
                selectedCafeLogs = profileState.selectedCafeLogs
            )
        }
    ) { paddingValue ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White)
                .padding(top = paddingValue.calculateTopPadding())
        ) {

            item {

                VerticalSpacer(height = 12.dp)
            }

            item {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(White)
                        .padding(
                            horizontal = 20.dp,
                            vertical = 25.dp
                        ),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "지금까지",
                        style = MaterialTheme.typography.subtitle1
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = "${globalState.user.value.activity / 3600}시간 ${(globalState.user.value.activity % 3600) / 60}분",
                            style = MaterialTheme.typography.subtitle2,
                            color = MaterialTheme.colors.primary
                        )

                        Text(
                            text = "동안 카페를 지켰어요!",
                            style = MaterialTheme.typography.subtitle1,
                            color = MaterialTheme.colors.primary
                        )
                    }
                }
            }

            item {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 20.dp,
                            bottom = 25.dp,
                            end = 20.dp
                        )
                        .background(color = White)
                        .border(
                            width = 1.5.dp,
                            color = MaterialTheme.colors.primary,
                            shape = MaterialTheme.shapes.large
                        ),
                ) {

                    if (profileState.isKalendarCafeLogLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(80.dp)
                                .padding(20.dp)
                                .align(Alignment.CenterHorizontally),
                            color = HeavyGray
                        )
                    } else {
                        CustomKalendar(
                            kalendarKonfig = KalendarKonfig(
                                yearRange = YearRange(
                                    YearMonth.now().year - 2,
                                    YearMonth.now().year + 2
                                ),
                            ),
                            onCurrentDayClick = { date, _ ->
                                profileState.kalendarCafeLogs.forEach { cafeLogs ->
                                    if (Time.getLocalDate(cafeLogs[0].start) == date) {
                                        profileViewModel.onEvent(
                                            ProfileEvent.SelectCafeLogs(cafeLogs)
                                        )
                                        scope.launch {
                                            bottomSheetScaffoldState.bottomSheetState.expand()
                                        }
                                    }
                                }
                            },
                            kalendarEvents = profileState.kalendarEvents,
                            kalendarStyle = KalendarStyle(
                                kalendarSelector = KalendarSelector.Circle(
                                    selectedColor = MaterialTheme.colors.background,
                                    todayColor = MaterialTheme.colors.background,
                                    selectedTextColor = MaterialTheme.colors.primaryVariant,
                                    defaultTextColor = TextBlack,
                                ),
                                elevation = 0.dp
                            ),
                        )
                    }
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

@Composable
fun ProfileKalendarBottomSheetContent(
    selectedCafeLogs: List<CafeLog>
) {

    val listState = rememberLazyListState(0, 0)

    LaunchedEffect(selectedCafeLogs) {
        listState.animateScrollToItem(0)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(color = White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (selectedCafeLogs.isEmpty()) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(80.dp)
                    .padding(20.dp)
                    .align(Alignment.CenterHorizontally),
                color = HeavyGray
            )
        } else {
            VerticalSpacer(height = 3.dp)

            BottomSheetHandle()

            VerticalSpacer(height = 12.dp)

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = White)
                    .padding(15.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    modifier = Modifier.size(25.dp),
                    painter = painterResource(id = R.drawable.stamp_icon),
                    contentDescription = "출석 도장",
                    tint = Color.Unspecified
                )

                HorizontalSpacer(width = 16.dp)

                Text(
                    text = Time.getYearMonthDay(selectedCafeLogs[0].start),
                    style = MaterialTheme.typography.subtitle2,
                )
            }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.7f)
            .background(color = White),
        state = listState
    ) {

        if (selectedCafeLogs.isNotEmpty()) {
            item {

                VerticalSpacer(height = 12.dp)
            }

            items(selectedCafeLogs) { cafeLog ->

                if (cafeLog.expired) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = White)
                            .padding(
                                start = 15.dp,
                                end = 15.dp,
                                bottom = 20.dp
                            )
                    ) {

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    width = 1.dp,
                                    color = MaterialTheme.colors.onBackground,
                                    shape = MaterialTheme.shapes.large
                                )
                                .padding(
                                    horizontal = 16.dp,
                                    vertical = 20.dp
                                ),
                            verticalArrangement = Arrangement.Center
                        ) {

                            Row {

                                Text(
                                    text = " ${cafeLog.name} ${cafeLog.floor.toFloor()}층",
                                    style = MaterialTheme.typography.body2,
                                )

                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = "+${cafeLog.point}p",
                                    style = MaterialTheme.typography.subtitle2,
                                    textAlign = TextAlign.End,
                                    color = MaterialTheme.colors.primary
                                )
                            }

                            VerticalSpacer(height = 12.dp)

                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Row(
                                    modifier = Modifier
                                        .background(
                                            color = LightGray,
                                            shape = RoundedCornerShape(50)
                                        )
                                        .padding(
                                            start = 13.dp,
                                            end = 13.dp,
                                            top = 7.dp,
                                            bottom = 7.dp
                                        ),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "${Time.getHourMinute(cafeLog.start)} ~ ${
                                            Time.getHourMinute(cafeLog.finish)
                                        }",
                                        style = MaterialTheme.typography.button,
                                        color = White
                                    )
                                }

                                HorizontalSpacer(width = 12.dp)

                                Text(
                                    text = "총 ${
                                        Time.getPassingTimeFromTo(
                                            start = cafeLog.start, 
                                            finish = cafeLog.finish
                                        )
                                    } 활동",
                                    style = MaterialTheme.typography.caption,
                                )
                            }
                        }
                    }
                }
            }
        }

        item {

            VerticalSpacer(height = 100.dp)
        }
    }
}
