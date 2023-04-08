package com.software.cafejariapp.presentation.feature.main.screen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ExpandLess
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.software.cafejariapp.core.customPlaceholder
import com.software.cafejariapp.presentation.GlobalState
import com.software.cafejariapp.presentation.component.*
import com.software.cafejariapp.presentation.feature.main.event.MainEvent
import com.software.cafejariapp.presentation.feature.main.viewModel.MainViewModel
import com.software.cafejariapp.presentation.theme.Gray
import com.software.cafejariapp.presentation.theme.MoreHeavyGray
import com.software.cafejariapp.presentation.theme.White
import com.software.cafejariapp.presentation.util.TimeUtil

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RegisterCafeResultScreen(
    globalState: GlobalState,
    mainViewModel: MainViewModel,
) {

    val mainState = mainViewModel.state.value
    val selectedInquiryCafeId = rememberSaveable { mutableStateOf(0) }
    val selectedDeleteInquiryCafeId = rememberSaveable { mutableStateOf(0) }

    NetworkChecker(globalState)

    LaunchedEffect(Unit) {
        mainViewModel.onEvent(MainEvent.GetInquiryCafes(globalState))
    }

    BackHandler(
        enabled = true,
        onBack = {
            when {
                selectedDeleteInquiryCafeId.value != 0 -> selectedDeleteInquiryCafeId.value = 0
                else -> globalState.navController.popBackStack()
            }
        }
    )

    if (selectedDeleteInquiryCafeId.value != 0) {
        CustomAlertDialog(
            onDismiss = { selectedDeleteInquiryCafeId.value = 0 },
            positiveButtonText = "삭제",
            onPositiveButtonClick = {
                mainViewModel.onEvent(MainEvent.DeleteInquiryCafe(
                    globalState = globalState,
                    inquiryCafeId = selectedDeleteInquiryCafeId.value
                ))
            },
            negativeButtonText = "취소",
            onNegativeButtonClick = { },
            content = {
                Text(
                    text = "카페 등록요청 기록을 삭제하시겠습니까?",
                    style = MaterialTheme.typography.subtitle1
                )
            }
        )
    }

    Scaffold(
        topBar = {
            BaseTopAppBar(
                isBackButtonEnable = true,
                onBackButtonClick = { globalState.navController.popBackStack() },
                title = "등록 요청한 카페",
                isScrolled = true
            )
        },
        backgroundColor = White
    ) { paddingValue ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = White)
                .padding(paddingValue.calculateTopPadding()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            when {
                mainState.inquiryCafes.isEmpty() -> {
                    EmptyScreen("등록 요청한 카페가 없습니다")
                }
                else -> {
                    CustomSwipeRefresh(
                        isLoading = mainState.isInquiryCafeLoading,
                        onRefresh = { mainViewModel.onEvent(MainEvent.GetInquiryCafes(globalState)) }
                    ) {

                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {

                            items(mainState.inquiryCafes) { inquiryCafe ->

                                Column(
                                    modifier = Modifier
                                        .combinedClickable(
                                            onClick = {
                                                if (selectedInquiryCafeId.value == inquiryCafe.id) {
                                                    selectedInquiryCafeId.value = 0
                                                } else {
                                                    selectedInquiryCafeId.value = inquiryCafe.id
                                                }
                                            },
                                            onLongClick = {
                                                selectedDeleteInquiryCafeId.value = inquiryCafe.id
                                            },
                                        ).padding(
                                            horizontal = 20.dp,
                                            vertical = 24.dp
                                        )
                                ) {

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .customPlaceholder(visible = mainState.isInquiryCafeLoading),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {

                                        Column {

                                            Text(
                                                text = "${TimeUtil.getLocalDate(inquiryCafe.requestDate)}",
                                                style = MaterialTheme.typography.body2,
                                                color = Gray
                                            )

                                            VerticalSpacer(height = 8.dp)

                                            Text(
                                                text = inquiryCafe.name,
                                                style = MaterialTheme.typography.subtitle2
                                            )

                                            Text(
                                                text = inquiryCafe.address, color = Gray
                                            )
                                        }

                                        Icon(
                                            imageVector = if (selectedInquiryCafeId.value == inquiryCafe.id) {
                                                Icons.Rounded.ExpandLess
                                            } else {
                                                Icons.Rounded.ExpandMore
                                            },
                                            contentDescription = "등록요청결과",
                                            tint = MoreHeavyGray,
                                            modifier = Modifier.size(28.dp)
                                        )
                                    }

                                    AnimatedVisibility(
                                        visible = selectedInquiryCafeId.value == inquiryCafe.id,
                                        enter = fadeIn() + expandVertically(),
                                        exit = fadeOut() + shrinkVertically(),
                                        modifier = Modifier.padding(top = 20.dp)
                                    ) {

                                        if (inquiryCafe.result.isBlank()) {
                                            Text("\uD83D\uDCAC  아직 등록요청을 확인하지 못했어요")
                                        } else {
                                            Text("[처리됨]  ${inquiryCafe.result}")
                                        }
                                    }
                                }

                                BaseDivider()
                            }
                        }
                    }
                }
            }
        }
    }
}

