package com.software.cafejariapp.presentation.feature.login.screen

import android.util.Patterns
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.software.cafejariapp.core.LocationTrackingNotPermitted
import com.software.cafejariapp.presentation.GlobalState
import com.software.cafejariapp.presentation.util.Screen
import com.software.cafejariapp.presentation.component.CustomOutlinedField
import com.software.cafejariapp.presentation.component.NetworkChecker
import com.software.cafejariapp.presentation.component.PrimaryCtaButton
import com.software.cafejariapp.presentation.component.VerticalSpacer
import com.software.cafejariapp.presentation.feature.login.component.BaseColumn
import com.software.cafejariapp.presentation.feature.login.event.LoginEvent
import com.software.cafejariapp.presentation.feature.login.viewModel.LoginViewModel
import com.software.cafejariapp.presentation.component.Comment

@Composable
fun LoginScreen(
    globalState: GlobalState,
    loginViewModel: LoginViewModel
) {

    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val email = rememberSaveable { mutableStateOf("") }
    val password = rememberSaveable { mutableStateOf("") }
    val isEmailValid = rememberSaveable { mutableStateOf(true) }
    val isPasswordValid = rememberSaveable { mutableStateOf(true) }
    val isPasswordVisible = rememberSaveable { mutableStateOf(false) }
    val isCafejariLoginProgress = rememberSaveable { mutableStateOf(false) }

    NetworkChecker(globalState)

    BaseColumn {

        Text(
            text = "로그인", style = MaterialTheme.typography.h5
        )

        VerticalSpacer(height = 20.dp)

        LazyColumn(
            state = LazyListState(0, 0),
            modifier = Modifier.heightIn(max = TextFieldDefaults.MinHeight * 6)
        ) {

            item {

                CustomOutlinedField(
                    modifier = Modifier.fillMaxWidth(),
                    value = email.value,
                    onValueChange = {
                        email.value = it
                        loginViewModel.onEvent(LoginEvent.ChangePredictions(it))
                    },
                    label = "이메일",
                    onFocusIn = { isEmailValid.value = true },
                    onFocusOut = {
                        isEmailValid.value =
                            email.value.isBlank() || Patterns.EMAIL_ADDRESS.matcher(email.value)
                                .matches()
                    },
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next,
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusManager.moveFocus(FocusDirection.Down)
                        }
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
                    visible = !isEmailValid.value,
                    text = "유효한 형태의 이메일을 입력해주세요",
                    error = true
                )
            }

            if (loginViewModel.predictions.value.isNotEmpty()) {
                items(loginViewModel.predictions.value) { login ->

                    Row(
                        modifier = Modifier
                            .padding(15.dp)
                            .fillMaxWidth()
                            .clickable {
                                focusManager.clearFocus()
                                email.value = login.email
                                password.value = login.password
                                isEmailValid.value =
                                    Patterns.EMAIL_ADDRESS.matcher(login.email).matches()
                                isPasswordValid.value = login.password.length > 7
                                loginViewModel.onEvent(LoginEvent.ClearPredictions)
                            },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(login.email)

                        Text(
                            text = "삭제",
                            style = MaterialTheme.typography.overline,
                            color = Color.Red,
                            modifier = Modifier.clickable {
                                loginViewModel.onEvent(LoginEvent.DeletePrediction(login))
                                loginViewModel.onEvent(LoginEvent.ChangePredictions(email.value))
                            }
                        )
                    }
                }
            }
        }

        VerticalSpacer(height = 8.dp)

        CustomOutlinedField(
            modifier = Modifier.fillMaxWidth(),
            value = password.value,
            onValueChange = { password.value = it },
            label = "비밀번호",
            onFocusIn = { isPasswordValid.value = true },
            onFocusOut = {
                isPasswordValid.value = password.value.isBlank() || password.value.length > 7
            },
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done,
            keyboardActions = KeyboardActions(onDone = {
                focusManager.clearFocus()
                isCafejariLoginProgress.value = true
                loginViewModel.onEvent(
                    LoginEvent.CafejariLogin(
                        globalState = globalState,
                        email = email.value,
                        password = password.value,
                        onFinish = { isCafejariLoginProgress.value = false }
                    )
                )
            }),
            isError = !isPasswordValid.value,
            trailingIcon = {
                IconButton(onClick = { isPasswordVisible.value = !isPasswordVisible.value }) {
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
            visible = !isPasswordValid.value,
            text = "비밀번호는 8자 이상이어야 합니다",
            error = true
        )

        VerticalSpacer(height = 40.dp)

        PrimaryCtaButton(
            text = "로그인",
            enabled = Patterns.EMAIL_ADDRESS.matcher(email.value)
                .matches() && password.value.length > 7,
            isProgress = isCafejariLoginProgress.value,
            onClick = {
                focusManager.clearFocus()
                isCafejariLoginProgress.value = true
                loginViewModel.onEvent(
                    LoginEvent.CafejariLogin(
                        globalState = globalState,
                        email = email.value,
                        password = password.value,
                        onFinish = {
                            isCafejariLoginProgress.value = false
                            try {
                                globalState.startLocationTracking(context)
                            } catch (e: LocationTrackingNotPermitted) {
                            }
                        }
                    )
                )
            }
        )

        VerticalSpacer(height = 40.dp)

        Text(
            text = "카페자리가 처음이라면 가입해주세요!",
            textDecoration = TextDecoration.Underline,
            color = MaterialTheme.colors.primary,
            style = MaterialTheme.typography.subtitle2,
            modifier = Modifier.clickable {
                globalState.navController.navigate(Screen.RegisterScreen.route)
            }
        )

        VerticalSpacer(height = 8.dp)

        Text(
            text = "이메일, 비밀번호를 잊어버리셨나요?",
            color = MaterialTheme.colors.primaryVariant,
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.clickable {
                globalState.navController.navigate(Screen.ResetScreen.route)
            }
        )
    }
}



