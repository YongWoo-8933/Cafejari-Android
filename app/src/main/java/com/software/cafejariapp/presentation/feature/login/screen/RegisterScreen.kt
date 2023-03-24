package com.software.cafejariapp.presentation.feature.login.screen

import android.util.Patterns
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.rounded.Cancel
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
import com.software.cafejariapp.presentation.component.CustomOutlinedField
import com.software.cafejariapp.presentation.component.NetworkChecker
import com.software.cafejariapp.presentation.component.PrimaryCtaButton
import com.software.cafejariapp.presentation.component.VerticalSpacer
import com.software.cafejariapp.presentation.feature.login.component.BaseColumn
import com.software.cafejariapp.presentation.component.Comment
import com.software.cafejariapp.presentation.feature.login.event.LoginEvent
import com.software.cafejariapp.presentation.feature.login.viewModel.LoginViewModel

@Composable
fun RegisterScreen(
    globalState: GlobalState,
    loginViewModel: LoginViewModel
) {

    val focusManager = LocalFocusManager.current

    val email = rememberSaveable { mutableStateOf("") }
    val password1 = rememberSaveable { mutableStateOf("") }
    val password2 = rememberSaveable { mutableStateOf("") }

    val isEmailValid = rememberSaveable { mutableStateOf(true) }
    val isPassword1Valid = rememberSaveable { mutableStateOf(true) }
    val isPassword2Valid = rememberSaveable { mutableStateOf(true) }
    val isPasswordVisible = rememberSaveable { mutableStateOf(false) }

    val isRegisterProgress = rememberSaveable { mutableStateOf(false) }

    NetworkChecker(globalState)

    BaseColumn {

        Text(
            text = "회원가입",
            style = MaterialTheme.typography.h5
        )

        VerticalSpacer(height = 20.dp)

        CustomOutlinedField(
            modifier = Modifier.fillMaxWidth(),
            value = email.value,
            onValueChange = { email.value = it },
            label = "이메일",
            onFocusIn = { isEmailValid.value = true },
            onFocusOut = {
                isEmailValid.value =
                    email.value.isBlank() || Patterns.EMAIL_ADDRESS.matcher(email.value).matches()
            },
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next,
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
            isError = !isEmailValid.value,
            trailingIcon = {
                if (email.value.isNotBlank()) {
                    IconButton(onClick = { email.value = "" }) {
                        Icon(
                            imageVector = Icons.Rounded.Cancel,
                            contentDescription = "싹 지우기"
                        )
                    }
                }
            },
        )

        Comment(
            visible = !Patterns.EMAIL_ADDRESS.matcher(email.value).matches(),
            text = "이메일 형식에 맞춰주세요(sample123@email.com)"
        )

        VerticalSpacer(height = 20.dp)

        CustomOutlinedField(
            modifier = Modifier.fillMaxWidth(),
            value = password1.value,
            onValueChange = { password1.value = it },
            label = "비밀번호",
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

        VerticalSpacer(height = 20.dp)

        CustomOutlinedField(
            modifier = Modifier.fillMaxWidth(),
            value = password2.value,
            onValueChange = { password2.value = it },
            label = "비밀번호(확인)",
            onFocusIn = { isPassword2Valid.value = true },
            onFocusOut = {
                isPassword2Valid.value = password2.value.isBlank() || password2.value.length > 7
            },
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done,
            keyboardActions = KeyboardActions(
                onDone = {
                    isRegisterProgress.value = true
                    focusManager.clearFocus()
                    loginViewModel.onEvent(
                        LoginEvent.VerifyEmailPassword(
                            globalState = globalState,
                            email = email.value,
                            password1 = password1.value,
                            password2 = password2.value,
                            onFinish = { isRegisterProgress.value = false }
                        )
                    )
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

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom
        ) {
            PrimaryCtaButton(
                text = "회원가입",
                enabled = Patterns.EMAIL_ADDRESS.matcher(email.value)
                    .matches() && password1.value.length > 7 && password2.value.length > 7 && password1.value == password2.value,
                onClick = {
                    isRegisterProgress.value = true
                    focusManager.clearFocus()
                    loginViewModel.onEvent(
                        LoginEvent.VerifyEmailPassword(
                            globalState = globalState,
                            email = email.value,
                            password1 = password1.value,
                            password2 = password2.value,
                            onFinish = { isRegisterProgress.value = false }
                        )
                    )
                },
            )
        }
    }
}

