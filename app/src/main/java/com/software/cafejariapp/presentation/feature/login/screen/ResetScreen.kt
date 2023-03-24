package com.software.cafejariapp.presentation.feature.login.screen

import android.util.Patterns
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.software.cafejariapp.presentation.GlobalState
import com.software.cafejariapp.presentation.feature.login.component.BaseColumn
import com.software.cafejariapp.presentation.component.CustomOutlinedField
import com.software.cafejariapp.presentation.component.PrimaryCtaButton
import com.software.cafejariapp.presentation.component.VerticalSpacer
import com.software.cafejariapp.presentation.component.NetworkChecker
import com.software.cafejariapp.presentation.feature.login.event.LoginEvent
import com.software.cafejariapp.presentation.feature.login.viewModel.LoginViewModel
import com.software.cafejariapp.presentation.component.Comment
import com.software.cafejariapp.presentation.feature.login.component.SmsAuthRow
import com.software.cafejariapp.presentation.feature.login.component.SmsSendRow

@Composable
fun ResetScreen(
    globalState: GlobalState,
    loginViewModel: LoginViewModel
) {

    val focusManager = LocalFocusManager.current

    val phoneNumber = rememberSaveable { mutableStateOf("") }
    val authNumber = rememberSaveable { mutableStateOf("") }
    val password1 = rememberSaveable { mutableStateOf("") }
    val password2 = rememberSaveable { mutableStateOf("") }
    val email = rememberSaveable { mutableStateOf("") }

    val isPassword1Valid = rememberSaveable { mutableStateOf(true) }
    val isPassword2Valid = rememberSaveable { mutableStateOf(true) }
    val isPasswordVisible = rememberSaveable { mutableStateOf(false) }
    val isPhoneNumberValid = rememberSaveable { mutableStateOf(true) }
    val isAuthNumberValid = rememberSaveable { mutableStateOf(true) }
    val isSmsSent = rememberSaveable { mutableStateOf(false) }
    val isAuthNumberAuthed = rememberSaveable { mutableStateOf(false) }

    NetworkChecker(globalState)

    BaseColumn {

        if (!isAuthNumberAuthed.value) {
            Text(
                text = "회원정보 찾기", style = MaterialTheme.typography.h5
            )

            VerticalSpacer(height = 20.dp)

            SmsSendRow(
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
                        LoginEvent.SendSmsForReset(
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
                            LoginEvent.AuthSmsForReset(
                                globalState = globalState,
                                phoneNumber = phoneNumber.value,
                                authNumber = authNumber.value,
                                onSuccess = {
                                    isAuthNumberAuthed.value = true
                                    email.value = it
                                }
                            )
                        )
                    } else {
                        globalState.showSnackBar("인증번호를 먼저 전송해주세요")
                    }
                },
                isAuthed = isAuthNumberAuthed.value
            )
        } else {
            Text(
                text = "비밀번호 변경",
                style = MaterialTheme.typography.h5
            )

            VerticalSpacer(height = 20.dp)

            CustomOutlinedField(
                modifier = Modifier.fillMaxWidth(),
                value = email.value,
                onValueChange = { },
                label = "이메일",
                onFocusIn = { },
                onFocusOut = { },
                enabled = false,
                readOnly = true,
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.None,
                keyboardActions = KeyboardActions(),
                isError = false
            )

            VerticalSpacer(height = 8.dp)

            CustomOutlinedField(
                modifier = Modifier.fillMaxWidth(),
                value = password1.value,
                onValueChange = { password1.value = it },
                label = "새 비밀번호",
                onFocusIn = { isPassword1Valid.value = true },
                onFocusOut = {
                    isPassword1Valid.value = password1.value.isBlank() || password1.value.length > 7
                },
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next,
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                isError = !isPassword1Valid.value,
                trailingIcon = {
                    IconButton(
                        onClick = { isPasswordVisible.value = !isPasswordVisible.value }
                    ) {
                        Icon(
                            imageVector = if (isPasswordVisible.value) {
                                Icons.Default.Visibility
                            } else {
                                Icons.Default.VisibilityOff
                            },
                            contentDescription = "비밀번호 보임여부"
                        )
                    }
                },
                visualTransformation = if (isPasswordVisible.value) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
            )

            Comment(
                visible = password1.value.length <= 7,
                text = "비밀번호는 8자 이상이어야 합니다"
            )

            VerticalSpacer(height = 8.dp)

            CustomOutlinedField(
                modifier = Modifier.fillMaxWidth(),
                value = password2.value,
                onValueChange = { password2.value = it },
                label = "새 비밀번호(확인)",
                onFocusIn = { isPassword2Valid.value = true },
                onFocusOut = {
                    isPassword2Valid.value = password2.value.isBlank() || password2.value.length > 7
                },
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done,
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        if (!isSmsSent.value || !isAuthNumberAuthed.value) {
                            globalState.showSnackBar("번호인증을 먼저 진행해주세요")
                        } else {
                            loginViewModel.onEvent(
                                LoginEvent.ResetPassword(
                                    globalState = globalState,
                                    password1 = password1.value,
                                    password2 = password2.value,
                                )
                            )
                        }
                    }
                ),
                isError = !isPassword2Valid.value,
                trailingIcon = {
                    IconButton(
                        onClick = { isPasswordVisible.value = !isPasswordVisible.value }
                    ) {
                        Icon(
                            imageVector = if (isPasswordVisible.value) {
                                Icons.Default.Visibility
                            } else {
                                Icons.Default.VisibilityOff
                            },
                            contentDescription = "비밀번호 보임여부"
                        )
                    }
                },
                visualTransformation = if (isPasswordVisible.value) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
            )

            Comment(
                visible = password2.value.length <= 7,
                text = "비밀번호는 8자 이상이어야 합니다"
            )

            Comment(
                visible = password1.value != password2.value,
                text = "두 비밀번호가 일치하지 않습니다"
            )

            VerticalSpacer(height = 60.dp)

            PrimaryCtaButton(
                text = "비밀번호 변경",
                onClick = {
                    focusManager.clearFocus()
                    if (!isSmsSent.value || !isAuthNumberAuthed.value) {
                        globalState.showSnackBar("번호인증을 먼저 진행해주세요")
                    } else {
                        loginViewModel.onEvent(
                            LoginEvent.ResetPassword(
                                globalState = globalState,
                                password1 = password1.value,
                                password2 = password2.value,
                            )
                        )
                    }
                },
                enabled = password1.value.length > 7 && password2.value.length > 7 && password1.value == password2.value,
            )
        }
    }
}















