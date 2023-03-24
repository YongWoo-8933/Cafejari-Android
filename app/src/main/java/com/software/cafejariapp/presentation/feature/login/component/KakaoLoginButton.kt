package com.software.cafejariapp.presentation.feature.login.component

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.software.cafejariapp.R
import com.software.cafejariapp.presentation.component.HorizontalSpacer
import com.software.cafejariapp.presentation.theme.KakaoBackground

@Composable
fun KakaoLoginButton(
    modifier: Modifier = Modifier,
    text: String = "카카오 로그인",
    isProgress: MutableState<Boolean>,
    onActivitySuccess: (String) -> Unit,
    onActivityFailure: () -> Unit,
    context: Context = LocalContext.current
) {

    Surface(
        modifier = modifier
            .height(48.dp)
            .padding(
                start = 12.dp,
                end = 12.dp,
            ),
        shape = MaterialTheme.shapes.medium,
        color = KakaoBackground
    ) {

        Row(
            modifier = Modifier
                .clickable {
                    isProgress.value = true
                    UserApiClient.instance.logout {
                        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                            if (error != null) {
                                onActivityFailure()
                                isProgress.value = false
                            } else if (token != null) {
                                onActivitySuccess(token.accessToken)
                            }
                        }

                        // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
                        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
                            UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                                if (error != null) {
                                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                                        isProgress.value = false
                                        return@loginWithKakaoTalk
                                    }
                                    UserApiClient.instance.loginWithKakaoAccount(
                                        context, callback = callback
                                    )
                                } else if (token != null) {
                                    onActivitySuccess(token.accessToken)
                                }
                            }
                        } else {
                            UserApiClient.instance.loginWithKakaoAccount(
                                context, callback = callback
                            )
                        }
                    }
                }
                .padding(
                    start = 12.dp,
                    end = 12.dp,
                    top = 6.dp,
                    bottom = 6.dp
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {

            Icon(
                modifier = Modifier.size(18.dp),
                painter = painterResource(id = R.drawable.kakao_logo),
                contentDescription = "카카오 버튼",
                tint = Color.Unspecified
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = if (isProgress.value) "로그인 중.." else text,
                    color = Color.Black.copy(alpha = 0.85f),
                    style = MaterialTheme.typography.body2
                )

                if (isProgress.value) {

                    HorizontalSpacer(width = 16.dp)

                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp,
                        color = Color.Black.copy(alpha = 0.85f)
                    )
                }
            }
        }
    }
}