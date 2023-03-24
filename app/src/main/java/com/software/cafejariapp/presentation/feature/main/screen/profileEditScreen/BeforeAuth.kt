package com.software.cafejariapp.presentation.feature.main.screen.profileEditScreen

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.software.cafejariapp.domain.util.SocialUserType
import com.software.cafejariapp.presentation.GlobalState
import com.software.cafejariapp.presentation.component.CustomOutlinedField
import com.software.cafejariapp.presentation.component.HorizontalSpacer
import com.software.cafejariapp.presentation.component.PrimaryCtaButton
import com.software.cafejariapp.presentation.component.VerticalSpacer
import com.software.cafejariapp.presentation.feature.login.component.GoogleLoginButton
import com.software.cafejariapp.presentation.feature.login.component.KakaoLoginButton
import com.software.cafejariapp.presentation.feature.main.event.ProfileEvent
import com.software.cafejariapp.presentation.feature.main.viewModel.ProfileViewModel

@Composable
fun BeforeAuth(
    globalState: GlobalState,
    profileViewModel: ProfileViewModel
) {

    val profileState = profileViewModel.state.value
    val focusManager = LocalFocusManager.current
    val password = rememberSaveable { mutableStateOf("") }
    val isPasswordValid = rememberSaveable { mutableStateOf(false) }
    val isPasswordVisible = rememberSaveable { mutableStateOf(false) }
    val isGoogleLoginProgress = rememberSaveable { mutableStateOf(false) }
    val isKakaoLoginProgress = rememberSaveable { mutableStateOf(false) }

    val buttonText = rememberSaveable { mutableStateOf("") }
    val animationDp = animateDpAsState(
        targetValue = if (password.value.length > 7) 80.dp else 0.dp,
        finishedListener = {
            if (it == 80.dp) buttonText.value = "확인"
        }
    )

    when (profileState.socialUserType) {
        SocialUserType.CafeJari -> {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text("프로필 정보 변경을 위해 인증을 진행해주세요")

                VerticalSpacer(height = 20.dp)

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.Bottom
                ) {
                    CustomOutlinedField(
                        modifier = Modifier.width(200.dp),
                        value = password.value,
                        onValueChange = { password.value = it },
                        label = "비밀번호",
                        onFocusIn = { isPasswordValid.value = true },
                        onFocusOut = {
                            isPasswordValid.value =
                                password.value.isBlank() || password.value.length > 7
                        },
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done,
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                                profileViewModel.onEvent(
                                    ProfileEvent.CafejariLoginForAuth(
                                        globalState,
                                        password.value
                                    )
                                )
                            }
                        ),
                        isError = !isPasswordValid.value,
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

                    LaunchedEffect(password.value.length) {
                        if (password.value.length <= 7) buttonText.value = ""
                    }

                    if (password.value.length > 7) HorizontalSpacer(width = 15.dp)

                    PrimaryCtaButton(
                        modifier = Modifier
                            .height(54.dp)
                            .width(animationDp.value)
                            .padding(bottom = 4.dp),
                        text = buttonText.value,
                        onClick = {
                            focusManager.clearFocus()
                            profileViewModel.onEvent(
                                ProfileEvent.CafejariLoginForAuth(
                                    globalState = globalState,
                                    password = password.value
                                )
                            )
                        }
                    )
                }
            }
        }
        SocialUserType.Kakao -> {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text("프로필 정보 변경을 위해 인증을 진행해주세요")

                VerticalSpacer(height = 20.dp)

                KakaoLoginButton(
                    modifier = Modifier.fillMaxWidth(),
                    isProgress = isKakaoLoginProgress,
                    onActivitySuccess = {
                        profileViewModel.onEvent(
                            ProfileEvent.KakaoLoginForAuth(
                            globalState = globalState,
                            accessToken = it,
                            onFinish = { isKakaoLoginProgress.value = false }
                        ))
                    },
                    onActivityFailure = {
                        globalState.showSnackBar("카카오 로그인 실패")
                        isKakaoLoginProgress.value = false
                    },
                )
            }
        }
        SocialUserType.Google -> {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text("프로필 정보 변경을 위해 인증을 진행해주세요")

                VerticalSpacer(height = 20.dp)

                GoogleLoginButton(modifier = Modifier.fillMaxWidth(),
                    authResult = profileViewModel.authResult,
                    isProgress = isGoogleLoginProgress,
                    googleSignInClient = profileViewModel.googleSignInClient,
                    onActivitySuccess = { email, code ->
                        profileViewModel.onEvent(
                            ProfileEvent.GoogleLoginForAuth(
                            globalState = globalState,
                            email = email,
                            code = code,
                            onFinish = { isGoogleLoginProgress.value = false }
                        ))
                    },
                    onActivityFailure = {
                        globalState.showSnackBar("구글 로그인 실패")
                        isGoogleLoginProgress.value = false
                    }
                )
            }
        }
    }
}