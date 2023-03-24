package com.software.cafejariapp.presentation.feature.login.screen

import android.util.Patterns
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.software.cafejariapp.R
import com.software.cafejariapp.data.source.remote.HttpRoutes
import com.software.cafejariapp.presentation.GlobalState
import com.software.cafejariapp.presentation.component.*
import com.software.cafejariapp.presentation.feature.login.component.BaseColumn
import com.software.cafejariapp.presentation.feature.login.event.LoginEvent
import com.software.cafejariapp.presentation.feature.login.viewModel.LoginViewModel
import com.software.cafejariapp.presentation.component.Comment
import com.software.cafejariapp.presentation.feature.login.component.SmsAuthRow
import com.software.cafejariapp.presentation.feature.login.component.SmsButton
import com.software.cafejariapp.presentation.feature.login.component.SmsSendRow
import com.software.cafejariapp.presentation.theme.HeavyGray
import com.software.cafejariapp.presentation.theme.White

@Composable
fun AuthScreen(
    globalState: GlobalState,
    loginViewModel: LoginViewModel
) {

    val isAgreementCheckComplete = rememberSaveable { mutableStateOf(false) }

    NetworkChecker(globalState)

    AnimatedVisibility(
        visible = !isAgreementCheckComplete.value,
        enter = fadeIn(),
        exit = fadeOut()
    ) {

        BeforeAgreement(
            onTosClick = {
                globalState.navigateToWebView(
                    topAppBarTitle = "위치기반서비스 이용약관",
                    url = HttpRoutes.TOS
                )
            },
            onPrivacyPolicyClick = {
                globalState.navigateToWebView(
                    topAppBarTitle = "개인정보 처리방침",
                    url = HttpRoutes.PRIVACY_POLICY_AGREEMENT
                )
            },
            onNextClick = {
                isAgreementCheckComplete.value = true
                globalState.showSnackBar("약관에 동의했습니다. 인증을 완료해주세요!")
            }
        )
    }

    AnimatedVisibility(
        visible = isAgreementCheckComplete.value,
        enter = fadeIn(),
        exit = fadeOut()
    ) {

        AfterAgreement(
            globalState = globalState,
            loginViewModel = loginViewModel
        )
    }
}



@Composable
private fun AgreementCheckRow(
    selected: Boolean, onCheckBoxClick: () -> Unit, onRowClick: () -> Unit, text: String,

    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth().padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "필수", style = MaterialTheme.typography.subtitle2, color = HeavyGray
            )
            HorizontalSpacer(width = 20.dp)

            Row(
                modifier = Modifier.clickable { onRowClick() },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = text, style = MaterialTheme.typography.subtitle1, color = HeavyGray
                )
                HorizontalSpacer(width = 8.dp)
                Icon(
                    painter = painterResource(id = R.drawable.arrow_forward_ios),
                    contentDescription = "url이동",
                    modifier = Modifier.size(12.dp),
                    tint = HeavyGray
                )
            }
        }
        Checkbox(
            modifier = Modifier.border(
                width = (1.5).dp, color = if (selected) {
                    MaterialTheme.colors.secondary
                } else {
                    MaterialTheme.colors.primary
                }
            ).size(20.dp),
            checked = selected,
            onCheckedChange = { onCheckBoxClick() },
            colors = CheckboxDefaults.colors(
                checkedColor = White,
                checkmarkColor = MaterialTheme.colors.secondary,
                uncheckedColor = White,
            ),
        )
    }
}

@Composable
private fun BeforeAgreement(
    onTosClick: () -> Unit,
    onPrivacyPolicyClick: () -> Unit,
    onNextClick: () -> Unit
) {

    val isTosAgreed = rememberSaveable { mutableStateOf(false) }
    val isPrivacyPolicyAgreed = rememberSaveable { mutableStateOf(false) }

    BaseColumn {

        Text(
            text = "약관 전체 동의",
            style = MaterialTheme.typography.h5
        )

        VerticalSpacer(height = 40.dp)

        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                text = "전체 동의",
                style = MaterialTheme.typography.subtitle2,
                color = MaterialTheme.colors.primary
            )

            Checkbox(
                modifier = Modifier
                    .border(
                        width = (1.5).dp,
                        color = if (isTosAgreed.value && isPrivacyPolicyAgreed.value) {
                            MaterialTheme.colors.secondary
                        } else {
                            MaterialTheme.colors.primary
                        }
                    )
                    .size(20.dp),
                checked = isTosAgreed.value && isPrivacyPolicyAgreed.value,
                onCheckedChange = {
                    isTosAgreed.value = it
                    isPrivacyPolicyAgreed.value = it
                },
                colors = CheckboxDefaults.colors(
                    checkedColor = White,
                    checkmarkColor = MaterialTheme.colors.secondary,
                    uncheckedColor = White,
                ),
            )
        }

        VerticalSpacer(height = 10.dp)

        BaseDivider()

        VerticalSpacer(height = 10.dp)

        AgreementCheckRow(
            selected = isTosAgreed.value,
            onCheckBoxClick = { isTosAgreed.value = !isTosAgreed.value },
            onRowClick = onTosClick,
            text = "서비스 이용약관 동의(필수)"
        )

        AgreementCheckRow(
            selected = isPrivacyPolicyAgreed.value,
            onCheckBoxClick = { isPrivacyPolicyAgreed.value = !isPrivacyPolicyAgreed.value },
            onRowClick = onPrivacyPolicyClick,
            text = "개인정보 수집 동의(필수)"
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom
        ) {

            PrimaryCtaButton(
                text = "다음",
                onClick = onNextClick,
                enabled = isTosAgreed.value && isPrivacyPolicyAgreed.value
            )
        }
    }
}


@Composable
private fun AfterAgreement(
    globalState: GlobalState,
    loginViewModel: LoginViewModel
) {

    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    val nickname = rememberSaveable { mutableStateOf("") }
    val recommendNickname = rememberSaveable { mutableStateOf("") }
    val phoneNumber = rememberSaveable { mutableStateOf("") }
    val authNumber = rememberSaveable { mutableStateOf("") }

    val isPhoneNumberValid = rememberSaveable { mutableStateOf(true) }
    val isNicknameValid = rememberSaveable { mutableStateOf(true) }
    val isRecommendNicknameValid = rememberSaveable { mutableStateOf(true) }
    val isAuthNumberValid = rememberSaveable { mutableStateOf(true) }

    val isSmsSent = rememberSaveable { mutableStateOf(false) }
    val isAuthNumberAuthed = rememberSaveable { mutableStateOf(false) }
    val isAuthProgress = rememberSaveable { mutableStateOf(false) }

    BaseColumn(
        modifier = Modifier
            .background(color = White)
            .statusBarsPadding()
            .fillMaxHeight()
    ) {

        Text(
            text = "본인인증",
            style = MaterialTheme.typography.h5
        )

        VerticalSpacer(height = 20.dp)

        CustomOutlinedField(
            modifier = Modifier.fillMaxWidth(),
            value = nickname.value,
            onValueChange = { nickname.value = it },
            label = "닉네임",
            onFocusIn = { isNicknameValid.value = true },
            onFocusOut = {
                isNicknameValid.value =
                    nickname.value.isBlank() || nickname.value.length in 2..10
            },
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next,
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
            isError = !isNicknameValid.value,
            trailingIcon = {
                if (nickname.value.isNotBlank()) {
                    IconButton(onClick = { nickname.value = "" }) {
                        Icon(
                            imageVector = Icons.Rounded.Cancel,
                            contentDescription = "싹 지우기"
                        )
                    }
                }
            }
        )

        Comment(
            visible = nickname.value.length !in 2..9,
            text = "닉네임은 2 ~ 9자 이내여야하며,\n공백/특수문자는 사용불가"
        )

        VerticalSpacer(height = 8.dp)

        SmsSendRow(
            modifier = Modifier.fillMaxWidth(),
            phoneNumber = phoneNumber.value,
            isPhoneNumberError = !isPhoneNumberValid.value,
            onPhoneNumberChange = { phoneNumber.value = it },
            onPhoneNumberFocusIn = { isPhoneNumberValid.value = true },
            onPhoneNumberFocusOut = {
                isPhoneNumberValid.value =
                    phoneNumber.value.isBlank() || (phoneNumber.value.length == 8 && Patterns.PHONE.matcher(
                        phoneNumber.value
                    ).matches())
            },
            isSmsSent = isSmsSent.value,
            onSendButtonClick = {
                loginViewModel.onEvent(
                    LoginEvent.SendSms(
                        globalState = globalState,
                        phoneNumber = phoneNumber.value,
                        onSuccess = {
                            isSmsSent.value = true
                            isAuthNumberAuthed.value = false
                        }
                    )
                )
            }
        )
        VerticalSpacer(height = 8.dp)

        SmsAuthRow(
            authNumber = authNumber.value,
            isAuthNumberError = !isAuthNumberValid.value,
            onNumberChange = { authNumber.value = it },
            onFocusIn = { isAuthNumberValid.value = true },
            onFocusOut = {
                isAuthNumberValid.value =
                    authNumber.value.isBlank() || (authNumber.value.length == 6 && Patterns.PHONE.matcher(
                        authNumber.value
                    ).matches())
            },
            onAuthButtonClick = {
                if (isSmsSent.value) {
                    loginViewModel.onEvent(
                        LoginEvent.AuthSms(
                            globalState = globalState,
                            phoneNumber = phoneNumber.value,
                            authNumber = authNumber.value,
                            onSuccess = { isAuthNumberAuthed.value = true },
                            onFinish = { }
                        )
                    )
                } else {
                    globalState.showSnackBar("인증번호를 먼저 전송해주세요")
                }
            },
            isAuthed = isAuthNumberAuthed.value
        )

        VerticalSpacer(height = 8.dp)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
        ) {

            CustomOutlinedField(
                modifier = Modifier
                    .weight(23f)
                    .fillMaxHeight()
                    .align(Alignment.CenterVertically),
                value = recommendNickname.value,
                onValueChange = { recommendNickname.value = it },
                label = "(선택) 추천인 닉네임",
                onFocusIn = { isRecommendNicknameValid.value = true },
                onFocusOut = {
                    isRecommendNicknameValid.value =
                        recommendNickname.value.isBlank() || recommendNickname.value.length in 2..10
                },
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next,
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                ),
                isError = !isRecommendNicknameValid.value
            )

            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = " ",
                    textAlign = TextAlign.Center,
                    color = HeavyGray
                )
            }

            SmsButton(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(7f)
                    .padding(0.dp, 10.dp, 0.dp, 3.dp),
                onClick = {
                    focusManager.clearFocus()
                    loginViewModel.onEvent(
                        LoginEvent.VerifyRecommendationNickname(
                            globalState = globalState,
                            nickname = recommendNickname.value
                        )
                    )
                },
                enabled = recommendNickname.value.length in 2..9,
                text = "확인"
            )
        }

        Comment(
            visible = recommendNickname.value.length !in 2..9,
            text = "추천인 작성시 500P 지급!\n'확인'을 눌러 유효한지 확인"
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom
        ) {

            PrimaryCtaButton(
                text = "본인인증 완료",
                isProgress = isAuthProgress.value,
                onClick = {
                    if (nickname.value == recommendNickname.value) {
                        globalState.showSnackBar("자신은 추천할 수 없습니다")
                    } else {
                        isAuthProgress.value = true
                        focusManager.clearFocus()
                        loginViewModel.onEvent(
                            LoginEvent.Authorize(
                                globalState = globalState,
                                nickname = nickname.value,
                                phoneNumber = phoneNumber.value,
                                onSuccess = {
                                    loginViewModel.onEvent(
                                        LoginEvent.Recommend(
                                            globalState = globalState,
                                            nickname = recommendNickname.value
                                        )
                                    )
                                },
                                onFinish = { isAuthProgress.value = false },
                                context = context
                            )
                        )
                    }
                },
                enabled = nickname.value.length in 2..9 && phoneNumber.value.length == 8 && Patterns.PHONE.matcher(
                    phoneNumber.value
                ).matches() && isAuthNumberAuthed.value,
            )
        }
    }
}


