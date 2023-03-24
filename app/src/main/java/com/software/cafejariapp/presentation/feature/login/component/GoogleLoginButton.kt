package com.software.cafejariapp.presentation.feature.login.component

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.software.cafejariapp.R
import com.software.cafejariapp.presentation.component.HorizontalSpacer
import com.software.cafejariapp.presentation.util.AuthResult

@Composable
fun GoogleLoginButton(
    modifier: Modifier = Modifier,
    text: String = "구글 로그인",
    authResult: AuthResult,
    isProgress: MutableState<Boolean>,
    googleSignInClient: GoogleSignInClient,
    onActivitySuccess: (email: String, code: String) -> Unit,
    onActivityFailure: () -> Unit
) {

    val authResultLauncher = rememberLauncherForActivityResult(authResult) { task ->
        try {
            val account = task?.getResult(ApiException::class.java)
            if (account != null) {
                onActivitySuccess(account.email!!, account.serverAuthCode!!)
            } else {
                onActivityFailure()
                isProgress.value = false
            }
        } catch (e: ApiException) {
            onActivityFailure()
            isProgress.value = false
        }
    }

    Surface(
        modifier = modifier
            .height(48.dp)
            .padding(
                start = 12.dp,
                end = 12.dp,
            ),
        shape = MaterialTheme.shapes.medium,
        color = Color.White, elevation = 1.dp
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    isProgress.value = true
                    googleSignInClient.revokeAccess().addOnCompleteListener {
                        authResultLauncher.launch(0)
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
                painter = painterResource(id = R.drawable.google_logo),
                contentDescription = "Google Button",
                tint = Color.Unspecified
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = if (isProgress.value) "로그인 중.." else text,
                    color = Color.Black.copy(alpha = 0.54f),
                    style = MaterialTheme.typography.body2
                )

                if (isProgress.value) {

                    HorizontalSpacer(width = 16.dp)

                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        color = Color.Black.copy(alpha = 0.54f),
                        strokeWidth = 2.dp
                    )
                }
            }
        }
    }
}