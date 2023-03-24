package com.software.cafejariapp.presentation.feature.login.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.software.cafejariapp.R
import com.software.cafejariapp.core.LocationTrackingNotPermitted
import com.software.cafejariapp.presentation.GlobalState
import com.software.cafejariapp.presentation.util.Screen
import com.software.cafejariapp.core.isNetworkAvailable
import com.software.cafejariapp.presentation.component.FullSizeLoadingScreen
import com.software.cafejariapp.presentation.component.NetworkChecker
import com.software.cafejariapp.presentation.component.VerticalSpacer
import com.software.cafejariapp.presentation.feature.login.event.LoginEvent
import com.software.cafejariapp.presentation.feature.login.viewModel.LoginViewModel
import com.software.cafejariapp.presentation.feature.login.component.GoogleLoginButton
import com.software.cafejariapp.presentation.feature.login.component.KakaoLoginButton
import com.software.cafejariapp.presentation.theme.HalfTransparentBlack
import com.software.cafejariapp.presentation.theme.MoreLightGray
import com.software.cafejariapp.presentation.theme.White
import kotlinx.coroutines.delay

@ExperimentalPagerApi
@Composable
fun CheckLoginScreen(
    globalState: GlobalState,
    loginViewModel: LoginViewModel
) {

    val context = LocalContext.current
    val isGoogleLoginProgress = rememberSaveable { mutableStateOf(false) }
    val isKakaoLoginProgress = rememberSaveable { mutableStateOf(false) }
    val isLoginDialogOpened = rememberSaveable { mutableStateOf(false) }
    val animationColor by rememberInfiniteTransition().animateColor(
        initialValue = Color.Black.copy(0.75f),
        targetValue = Color.Transparent,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    NetworkChecker(globalState)

    LaunchedEffect(Unit) {
        if (globalState.isLoggedIn.value) {
            globalState.navController.navigate(Screen.MapScreen.route) {
                popUpTo(Screen.SplashScreen.route) { inclusive = true }
            }
        } else {
            while (true) {
                delay(1000L)
                if (globalState.isLoggedIn.value) {
                    globalState.navController.navigate(Screen.MapScreen.route) {
                        popUpTo(Screen.SplashScreen.route) { inclusive = true }
                    }
                    break
                }
                if (globalState.initiated.value) {
                    break
                }
            }
        }
    }

    if (!globalState.initiated.value) {
        FullSizeLoadingScreen(loadingText = "초기 설정 중..")
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MoreLightGray)
            .navigationBarsPadding(),
        contentAlignment = Alignment.Center
    ) {

        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = R.drawable.check_login_background),
            contentDescription = "처음 이미지",
            contentScale = ContentScale.Crop
        )

        if (!isLoginDialogOpened.value) {
            Row(
                modifier = Modifier.fillMaxSize()
                    .background(color = animationColor)
                    .clickable { isLoginDialogOpened.value = true },
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "아무곳이나 터치해보세요!",
                    color = White,
                    style = MaterialTheme.typography.h5,
                    fontSize = 24.sp
                )
            }
        }
    }

    AnimatedVisibility(
        visible = isLoginDialogOpened.value,
        enter = fadeIn(),
        exit = fadeOut()
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = HalfTransparentBlack),
            contentAlignment = Alignment.Center
        ) {

            Column(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colors.primary,
                        shape = MaterialTheme.shapes.large
                    )
                    .padding(
                        vertical = 36.dp,
                        horizontal = 16.dp
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "카페 혼잡도를 확인하고 싶다면?",
                    color = White,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.subtitle2
                )

                VerticalSpacer(height = 4.dp)

                Text(
                    text = "10초만에 회원가입 해보세요",
                    style = MaterialTheme.typography.h5,
                    color = White,
                    textAlign = TextAlign.Center,
                )

                VerticalSpacer(height = 28.dp)

                KakaoLoginButton(
                    modifier = Modifier.width(300.dp),
                    isProgress = isKakaoLoginProgress,
                    onActivitySuccess = {
                        loginViewModel.onEvent(
                            LoginEvent.KakaoLogin(
                                accessToken = it,
                                globalState = globalState,
                                onFinish = {
                                    isKakaoLoginProgress.value = false
                                    try {
                                        globalState.startLocationTracking(context)
                                    } catch (e: LocationTrackingNotPermitted) {
                                    }
                                }
                            )
                        )
                    },
                    onActivityFailure = { globalState.showSnackBar("로그인 실패") }
                )

                VerticalSpacer(height = 12.dp)

                GoogleLoginButton(
                    modifier = Modifier.width(300.dp),
                    authResult = loginViewModel.authResult,
                    googleSignInClient = loginViewModel.googleSignInClient,
                    isProgress = isGoogleLoginProgress,
                    onActivitySuccess = { email, code ->
                        loginViewModel.onEvent(
                            LoginEvent.GoogleLogin(
                                email = email,
                                code = code,
                                globalState = globalState,
                                onFinish = {
                                    isGoogleLoginProgress.value = false
                                    try {
                                        globalState.startLocationTracking(context)
                                    } catch (e: LocationTrackingNotPermitted) {
                                    }
                                }
                            )
                        )
                    },
                    onActivityFailure = {
                        globalState.showSnackBar("로그인 오류, 잠시 후에 다시 시도해주세요")
                    }
                )

                VerticalSpacer(height = 12.dp)

                Button(
                    onClick = {
                        globalState.navController.navigate(Screen.LoginScreen.route)
                        isLoginDialogOpened.value = false
                    },
                    modifier = Modifier.width(300.dp).height(48.dp).padding(
                        start = 12.dp,
                        end = 12.dp,
                    ),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.background
                    ),
                    contentPadding = PaddingValues(horizontal = 12.dp),
                    shape = MaterialTheme.shapes.medium
                ) {

                    Icon(
                        modifier = Modifier.size(18.dp),
                        painter = painterResource(id = R.drawable.cafejari_login_button_logo),
                        contentDescription = "카페자리 로그인 버튼",
                        tint = Color.Unspecified
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = "카페자리 로그인",
                            style = MaterialTheme.typography.body2,
                            color = MaterialTheme.colors.primary
                        )
                    }
                }
            }
        }
    }
}