package com.software.cafejariapp.presentation.feature.main.screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.skydoves.landscapist.glide.GlideImage
import com.software.cafejariapp.R
import com.software.cafejariapp.core.customPlaceholder
import com.software.cafejariapp.presentation.GlobalState
import com.software.cafejariapp.domain.entity.Event
import com.software.cafejariapp.presentation.component.*
import com.software.cafejariapp.presentation.feature.main.event.MainEvent
import com.software.cafejariapp.presentation.feature.main.viewModel.MainViewModel
import com.software.cafejariapp.presentation.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun EventScreen(
    globalState: GlobalState,
    mainViewModel: MainViewModel,
) {

    val mainState = mainViewModel.state.value

    NetworkChecker(globalState)

    LaunchedEffect(Unit) {
        mainViewModel.onEvent(MainEvent.GetEvents(globalState))
    }

    Scaffold(
        topBar = {
            BaseTopAppBar(
                isBackButtonEnable = true,
                onBackButtonClick = { globalState.navController.popBackStack() },
                title = "이벤트",
                isScrolled = true
            )
        },
        backgroundColor = White
    ) { paddingValue ->

        CustomSwipeRefresh(
            isLoading = mainState.isEventsLoading,
            onRefresh = { mainViewModel.onEvent(MainEvent.GetEvents(globalState)) }
        ) {

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = paddingValue.calculateTopPadding()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                items(mainState.events) { event ->

                    EventItem(
                        event = event,
                        isLoading = mainState.isEventsLoading
                    ) {
                        globalState.navigateToWebView(
                            topAppBarTitle = "이벤트 상세",
                            url = event.url
                        )
                    }

                    BaseDivider()
                }

                items(mainState.expiredEvents) { expiredEvent ->

                    Box {

                        EventItem(
                            event = expiredEvent,
                            isLoading = mainState.isEventsLoading
                        ) {
                            globalState.navigateToWebView(
                                topAppBarTitle = "이벤트 상세",
                                url = expiredEvent.url
                            )
                        }

                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .background(color = HalfTransparentBlack),
                            contentAlignment = Alignment.Center
                        ) {

                            Text(
                                text = "종료된 이벤트 입니다",
                                color = White,
                                style = MaterialTheme.typography.h4
                            )
                        }
                    }

                    BaseDivider()
                }

                item {

                    VerticalSpacer(height = 50.dp)
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
private fun EventItem(
    modifier: Modifier = Modifier,
    event: Event,
    isLoading: Boolean,
    onClick: () -> Unit
) {

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clickable(enabled = event.url != "_none") {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Card(
                modifier = Modifier.customPlaceholder(visible = isLoading),
                shape = MaterialTheme.shapes.medium
            ) {

                GlideImage(
                    modifier = Modifier.fillMaxWidth(),
                    imageModel = event.image,
                    contentScale = ContentScale.FillWidth,
                    placeHolder = painterResource(id = R.drawable.glide_image_placeholder)
                )
            }

            VerticalSpacer(height = 16.dp)

            Text(
                modifier = Modifier.customPlaceholder(visible = isLoading),
                text = event.name,
                style = MaterialTheme.typography.subtitle2
            )

            VerticalSpacer(height = 8.dp)

            Text(
                modifier = Modifier.customPlaceholder(visible = isLoading),
                text = event.preview
            )

            VerticalSpacer(height = 4.dp)

            Text(
                modifier = Modifier.customPlaceholder(visible = isLoading),
                text = "${
                    event.start.substring(0, 10)
                        .replace("-", ".")
                }  ~  ${event.finish.substring(0, 10).replace("-", ".")}", color = HeavyGray
            )
        }
    }
}

