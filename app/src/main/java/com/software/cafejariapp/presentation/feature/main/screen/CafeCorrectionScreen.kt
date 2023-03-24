package com.software.cafejariapp.presentation.feature.main.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.software.cafejariapp.R
import com.software.cafejariapp.presentation.GlobalState
import com.software.cafejariapp.presentation.component.*
import com.software.cafejariapp.presentation.component.Comment
import com.software.cafejariapp.presentation.feature.main.event.MainEvent
import com.software.cafejariapp.presentation.feature.main.viewModel.MainViewModel
import com.software.cafejariapp.presentation.theme.LightGray
import com.software.cafejariapp.presentation.theme.White
import kotlinx.coroutines.launch

@ExperimentalPagerApi
@Composable
fun CafeCorrectionScreen(
    globalState: GlobalState,
    mainViewModel: MainViewModel,
) {

    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()
    val storeInfoContent = rememberSaveable { mutableStateOf("") }
    val openingHourContent = rememberSaveable { mutableStateOf("") }
    val wallSocketContent = rememberSaveable { mutableStateOf("") }
    val restroomContent = rememberSaveable { mutableStateOf("") }
    val isDialogOpened = rememberSaveable { mutableStateOf(false) }
    val listState = rememberLazyListState()

    NetworkChecker(globalState)

    BackHandler(
        enabled = true,
        onBack = {
            when {
                isDialogOpened.value -> isDialogOpened.value = false
                else -> globalState.navController.popBackStack()
            }
        }
    )

    if (isDialogOpened.value) {
        CorrectionDialog(
            onDismiss = { isDialogOpened.value = false },
            onSubmitButtonClick = {
                mainViewModel.onEvent(
                        MainEvent.SubmitAdditionalCafeInfo(
                        globalState = globalState,
                        storeInfoContent = storeInfoContent.value,
                        openingHourContent = openingHourContent.value,
                        wallSocketContent = wallSocketContent.value,
                        restroomContent = restroomContent.value
                    )
                )
            }
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxWidth(),
        topBar = {
            BaseTopAppBar(
                isBackButtonEnable = true,
                onBackButtonClick = { globalState.navController.popBackStack() },
                title = "카페정보 제보하기",
            )
        }
    ) { paddingValue ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(White)
                .padding(top = paddingValue.calculateTopPadding()),
            horizontalAlignment = Alignment.Start,
            state = listState
        ) {

            item {

                VerticalSpacer(height = 40.dp)

                Row(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Image(
                        painter = painterResource(R.drawable.coffee_bean_marker),
                        contentDescription = "커피마커",
                        modifier = Modifier.size(32.dp).padding(top = 4.dp)
                    )

                    HorizontalSpacer(width = 8.dp)

                    Text(
                        text = globalState.modalCafeInfo.value.name,
                        style = MaterialTheme.typography.subtitle2,
                        color = MaterialTheme.colors.primary
                    )
                }

                VerticalSpacer(height = 20.dp)
            }

            item {

                CorrectionTextField(
                    text = storeInfoContent.value,
                    labelText = "영업 여부",
                    commentText = "ex-1) 이 카페는 현재 영업중이 아니에요\nex-2) 이 자리에 다른 이름을 가진 카페가 생겼어요",
                    imeAction = ImeAction.Next,
                    onTextChange = { storeInfoContent.value = it },
                    onFocusIn = {
                        scope.launch { listState.scrollToItem(0) }
                    },
                    keyboardAction = KeyboardActions(
                        onNext = {
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                    )
                )
            }

            item {

                CorrectionTextField(
                    text = openingHourContent.value,
                    labelText = "영업 시간",
                    commentText = "ex-1) 매일 9~22시\n" + "ex-2) 평일: 9~22시, 주말: 10~18시",
                    imeAction = ImeAction.Next,
                    onTextChange = { openingHourContent.value = it },
                    onFocusIn = {
                        scope.launch { listState.scrollToItem(1) }
                    },
                    keyboardAction = KeyboardActions(
                        onNext = {
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                    )
                )
            }

            item {

                CorrectionTextField(
                    text = wallSocketContent.value,
                    labelText = "콘센트 정보",
                    commentText = "ex-1) 테이블 대비 80%정도 보급됨\n" + "ex-2) 1층: 테이블대비 70%, 2층: 테이블대비 90%",
                    imeAction = ImeAction.Next,
                    onTextChange = { wallSocketContent.value = it },
                    onFocusIn = {
                        scope.launch { listState.scrollToItem(2) }
                    },
                    keyboardAction = KeyboardActions(
                        onNext = {
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                    )
                )
            }

            item {

                CorrectionTextField(
                    text = restroomContent.value,
                    labelText = "화장실 정보",
                    commentText = "ex-1) 1층에 공용화장실이 있어요\n" + "ex-2) 1층 남자화장실, 2층 여자화장실 있음",
                    imeAction = ImeAction.Done,
                    onTextChange = { restroomContent.value = it },
                    onFocusIn = {
                        scope.launch { listState.scrollToItem(3) }
                    },
                    keyboardAction = KeyboardActions(
                        onDone = { focusManager.clearFocus() }
                    )
                )
            }

            item {

                VerticalSpacer(height = 40.dp)

                PrimaryCtaButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    text = "제보하기",
                    onClick = { isDialogOpened.value = true },
                    enabled = storeInfoContent.value.isNotBlank() || openingHourContent.value.isNotBlank() || wallSocketContent.value.isNotBlank() || restroomContent.value.isNotBlank()
                )

                VerticalSpacer(height = 300.dp)
            }
        }
    }
}


@Composable
fun CorrectionTextField(
    text: String,
    labelText: String,
    commentText: String,
    imeAction: ImeAction,
    onTextChange: (String) -> Unit,
    onFocusIn: () -> Unit,
    keyboardAction: KeyboardActions,
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {

        CustomOutlinedField(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            value = text,
            onValueChange = { onTextChange(it) },
            label = labelText,
            singleLine = false,
            onFocusIn = onFocusIn,
            onFocusOut = {},
            keyboardType = KeyboardType.Text,
            imeAction = imeAction,
            keyboardActions = keyboardAction,
            isError = false
        )

        Comment(
            visible = true,
            text = commentText,
            textColor = LightGray
        )
    }

    VerticalSpacer(height = 32.dp)
}


@Composable
fun CorrectionDialog(
    onDismiss: () -> Unit,
    onSubmitButtonClick: () -> Unit
) {

    CustomAlertDialog(
        onDismiss = onDismiss,
        positiveButtonText = "제출",
        onPositiveButtonClick = onSubmitButtonClick,
        negativeButtonText = "",
        onNegativeButtonClick = { },
        isNegativeButtonEnabled = false,
        content = {
            Text(
                text = "제보하신 정보가 적용되면",
                style = MaterialTheme.typography.subtitle1
            )

            Text(
                text = "100P를 지급해드립니다!",
                style = MaterialTheme.typography.subtitle1
            )

            Text(
                text = "적용 여부는 알림으로 알려드리고,",
                style = MaterialTheme.typography.subtitle1
            )

            Text(
                text = "포인트 내역에서도 확인할 수 있습니다.",
                style = MaterialTheme.typography.subtitle1
            )
        }
    )
}