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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.software.cafejariapp.presentation.GlobalState
import com.software.cafejariapp.presentation.component.*
import com.software.cafejariapp.presentation.feature.main.event.MainEvent
import com.software.cafejariapp.presentation.feature.main.viewModel.MainViewModel
import com.software.cafejariapp.presentation.theme.White
import com.software.cafejariapp.presentation.util.Screen

@ExperimentalPagerApi
@Composable
fun InquiryScreen(
    globalState: GlobalState,
    mainViewModel: MainViewModel,
) {

    val focusManager = LocalFocusManager.current
    val content = rememberSaveable { mutableStateOf("") }
    val isInquirySubmitDialogOpened = rememberSaveable { mutableStateOf(false) }

    NetworkChecker(globalState)

    BackHandler(
        enabled = true,
        onBack = {
            when {
                isInquirySubmitDialogOpened.value -> isInquirySubmitDialogOpened.value = false
                else -> globalState.navController.popBackStack()
            }
        }
    )

    if (isInquirySubmitDialogOpened.value) {
        CustomAlertDialog(
            onDismiss = { isInquirySubmitDialogOpened.value = false },
            positiveButtonText = "제출",
            onPositiveButtonClick = {
                mainViewModel.onEvent(MainEvent.SubmitInquiryEtc(
                    globalState = globalState,
                    content = content.value
                ))
            },
            negativeButtonText = "",
            onNegativeButtonClick = { },
            isNegativeButtonEnabled = false,
            content = {
                Text(
                    text = "문의결과는 오른쪽 위 리스트 아이콘을",
                    style = MaterialTheme.typography.subtitle1
                )

                Text(
                    text = "통해 '내 문의 내용' 화면으로 가면",
                    style = MaterialTheme.typography.subtitle1
                )

                Text(
                    text = "확인하실 수 있습니다.",
                    style = MaterialTheme.typography.subtitle1
                )

                Text(
                    text = "문의를 제출하시겠습니까?",
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
                        onClick = { globalState.navController.navigate(Screen.InquiryAnswerScreen.route) }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.ListAlt,
                            contentDescription = "내 문의 확인",
                            tint = MaterialTheme.colors.primary
                        )
                    }
                },
                title = "1:1 문의"
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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            CustomOutlinedField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                value = content.value,
                onValueChange = { content.value = it },
                label = "문의하실 내용을 자세히 적어주세요",
                singleLine = false,
                onFocusIn = {},
                onFocusOut = {},
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done,
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                isError = false
            )

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Bottom
            ) {

                PrimaryCtaButton(
                    text = "문의하기",
                    onClick = { isInquirySubmitDialogOpened.value = true },
                    enabled = content.value.isNotBlank()
                )
            }
        }
    }
}