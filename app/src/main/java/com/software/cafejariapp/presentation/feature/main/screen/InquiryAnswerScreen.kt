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
import com.software.cafejariapp.presentation.GlobalState
import com.software.cafejariapp.presentation.util.Time
import com.software.cafejariapp.presentation.component.*
import com.software.cafejariapp.presentation.feature.main.event.MainEvent
import com.software.cafejariapp.presentation.feature.main.viewModel.MainViewModel
import com.software.cafejariapp.presentation.theme.LightGray
import com.software.cafejariapp.presentation.theme.MoreHeavyGray
import com.software.cafejariapp.presentation.theme.White

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun InquiryAnswerScreen(
    globalState: GlobalState,
    mainViewModel: MainViewModel
) {

    val mainState = mainViewModel.state.value
    val selectedInquiryEtcId = rememberSaveable { mutableStateOf(0) }
    val selectedDeleteInquiryEtcId = rememberSaveable { mutableStateOf(0) }

    NetworkChecker(globalState)

    LaunchedEffect(Unit) {
        mainViewModel.onEvent(MainEvent.GetInquiryEtcs(globalState))
    }

    BackHandler(
        enabled = true,
        onBack = {
            when {
                selectedDeleteInquiryEtcId.value != 0 -> selectedDeleteInquiryEtcId.value = 0
                else -> globalState.navController.popBackStack()
            }
        }
    )

    if (selectedDeleteInquiryEtcId.value != 0) {
        CustomAlertDialog(
            onDismiss = { selectedDeleteInquiryEtcId.value = 0 },
            positiveButtonText = "삭제",
            onPositiveButtonClick = {
                mainViewModel.onEvent(MainEvent.DeleteInquiryEtc(
                    globalState = globalState,
                    inquiryEtcId = selectedDeleteInquiryEtcId.value
                ))
            },
            negativeButtonText = "취소",
            onNegativeButtonClick = { },
            content = {
                Text(
                    text = "문의 기록을 삭제하시겠습니까?",
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
                title = "내 문의 내용",
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
                mainState.isInquiryEtcLoading -> {
                    FullSizeLoadingScreen()
                }
                mainState.inquiryEtcs.isEmpty() -> {
                    EmptyScreen("문의한 내용이 없습니다")
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {

                        items(mainState.inquiryEtcs) { inquiryEtc ->

                            Column(
                                modifier = Modifier
                                    .combinedClickable(
                                        onClick = {
                                            if (selectedInquiryEtcId.value == inquiryEtc.id) {
                                                selectedInquiryEtcId.value = 0
                                            } else {
                                                selectedInquiryEtcId.value = inquiryEtc.id
                                            }
                                        },
                                        onLongClick = {
                                            selectedDeleteInquiryEtcId.value = inquiryEtc.id
                                        },
                                    )
                                    .padding(
                                        horizontal = 20.dp,
                                        vertical = 24.dp
                                    )
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    Column(
                                        modifier = Modifier.weight(9f)
                                    ) {

                                        Text(
                                            text = "${Time.getLocalDate(inquiryEtc.requestDate)}",
                                            style = MaterialTheme.typography.body2,
                                            color = LightGray
                                        )

                                        VerticalSpacer(height = 8.dp)

                                        Text(inquiryEtc.content)
                                    }

                                    Row(
                                        modifier = Modifier.weight(1f),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {

                                        Icon(
                                            imageVector = if (selectedInquiryEtcId.value == inquiryEtc.id) {
                                                Icons.Rounded.ExpandLess
                                            } else {
                                                Icons.Rounded.ExpandMore
                                            },
                                            contentDescription = "문의답변",
                                            tint = MoreHeavyGray,
                                            modifier = Modifier.size(28.dp)
                                        )
                                    }
                                }

                                AnimatedVisibility(
                                    visible = selectedInquiryEtcId.value == inquiryEtc.id,
                                    enter = fadeIn() + expandVertically(),
                                    exit = fadeOut() + shrinkVertically(),
                                    modifier = Modifier.padding(top = 20.dp)
                                ) {

                                    if (inquiryEtc.answer.isBlank()) {
                                        Text("\uD83D\uDCAC  아직 문의를 확인하지 못했어요")
                                    } else {
                                        Text("[답변]  ${inquiryEtc.answer}")
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


