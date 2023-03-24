package com.software.cafejariapp.presentation.feature.main.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ListAlt
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.software.cafejariapp.presentation.GlobalState
import com.software.cafejariapp.presentation.component.*
import com.software.cafejariapp.presentation.util.Screen
import com.software.cafejariapp.presentation.component.Comment
import com.software.cafejariapp.presentation.feature.main.event.MainEvent
import com.software.cafejariapp.presentation.feature.main.viewModel.MainViewModel
import com.software.cafejariapp.presentation.theme.LightGray
import com.software.cafejariapp.presentation.theme.White

@ExperimentalPagerApi
@Composable
fun RegisterCafeScreen(
    globalState: GlobalState,
    mainViewModel: MainViewModel
) {

    val focusManager = LocalFocusManager.current
    val cafeName = rememberSaveable { mutableStateOf("") }
    val cafeAddress = rememberSaveable { mutableStateOf("") }
    val isSubmitDialogOpened = rememberSaveable { mutableStateOf(false) }

    NetworkChecker(globalState)

    BackHandler(
        enabled = true,
        onBack = {
            when {
                isSubmitDialogOpened.value -> isSubmitDialogOpened.value = false
                else -> globalState.navController.popBackStack()
            }
        }
    )

    if (isSubmitDialogOpened.value) {
        CustomAlertDialog(
            onDismiss = { isSubmitDialogOpened.value = false },
            positiveButtonText = "제출",
            onPositiveButtonClick = {
                mainViewModel.onEvent(MainEvent.SubmitInquiryCafe(
                    globalState = globalState,
                    cafeName = cafeName.value,
                    cafeAddress = cafeAddress.value
                ))
            },
            negativeButtonText = "",
            onNegativeButtonClick = { },
            isNegativeButtonEnabled = false,
            content = {
                Text(
                    text = "등록요청 하신 카페는 운영진의",
                    style = MaterialTheme.typography.subtitle1
                )

                Text(
                    text = "검토 후 증설여부를 결정하며,",
                    style = MaterialTheme.typography.subtitle1
                )

                Text(
                    text = "결과는 오른쪽 위 리스트 버튼을 통해",
                    style = MaterialTheme.typography.subtitle1
                )

                Text(
                    text = "'등록 요청한 카페'화면으로 이동하여",
                    style = MaterialTheme.typography.subtitle1
                )

                Text(
                    text = "확인하실 수 있습니다.",
                    style = MaterialTheme.typography.subtitle1
                )

                Text(
                    text = "카페를 등록하시겠습니까?",
                    style = MaterialTheme.typography.subtitle1
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
                trailingIconAction = {
                    IconButton(
                        modifier = Modifier.size(24.dp),
                        onClick = {
                            globalState.navController.navigate(Screen.RegisterCafeResultScreen.route)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.ListAlt,
                            contentDescription = "등록요청 카페",
                            tint = MaterialTheme.colors.primary
                        )
                    }
                },
                title = "카페 추가"
            )
        }
    ) { paddingValue ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(White)
                .padding(top = paddingValue.calculateTopPadding())
                .padding(
                    horizontal = 20.dp,
                    vertical = 40.dp
                ),
            horizontalAlignment = Alignment.Start
        ) {

            CustomOutlinedField(
                modifier = Modifier.fillMaxWidth(),
                value = cafeName.value,
                onValueChange = { cafeName.value = it },
                label = "등록할 카페(지점명 포함)",
                singleLine = true,
                onFocusIn = {},
                onFocusOut = {},
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next,
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                isError = false
            )

            VerticalSpacer(height = 20.dp)

            CustomOutlinedField(
                modifier = Modifier.fillMaxWidth(),
                value = cafeAddress.value,
                onValueChange = { cafeAddress.value = it },
                label = "주소(시, 구 포함)",
                singleLine = true,
                onFocusIn = {},
                onFocusOut = {},
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done,
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                isError = false
            )

            Comment(
                visible = cafeAddress.value.isBlank(),
                text = "카페 지점명이 정확하다면 생략가능\nex) 스타벅스 xx역점",
                textColor = LightGray
            )

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Bottom
            ) {

                PrimaryCtaButton(
                    text = "카페 추가하기",
                    onClick = { isSubmitDialogOpened.value = true },
                    enabled = cafeName.value.isNotBlank()
                )
            }
        }
    }
}