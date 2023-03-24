package com.software.cafejariapp.presentation.feature.login.component

import android.util.Patterns
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.software.cafejariapp.presentation.component.Comment
import com.software.cafejariapp.presentation.component.CustomOutlinedField
import com.software.cafejariapp.presentation.theme.HeavyGray

@Composable
fun SmsSendRow(
    modifier: Modifier = Modifier,
    phoneNumber: String,
    isPhoneNumberError: Boolean,
    onPhoneNumberChange: (String) -> Unit,
    onPhoneNumberFocusIn: () -> Unit,
    onPhoneNumberFocusOut: () -> Unit,

    isSmsSent: Boolean,
    onSendButtonClick: () -> Unit
) {

    val focusManager = LocalFocusManager.current

    LaunchedEffect(key1 = phoneNumber) {
        if (Patterns.PHONE.matcher(phoneNumber).matches() && phoneNumber.length == 8) {
            focusManager.clearFocus()
        }
    }

    Column(
        modifier = modifier.fillMaxWidth()
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
        ) {

            CustomOutlinedField(
                modifier = Modifier
                    .weight(6f)
                    .align(Alignment.CenterVertically),
                value = "010",
                onValueChange = { },
                label = "",
                onFocusIn = { },
                onFocusOut = { },
                enabled = false,
                readOnly = true,
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.None,
                keyboardActions = KeyboardActions(),
                isError = false
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

            CustomOutlinedField(
                modifier = Modifier
                    .weight(16f)
                    .fillMaxHeight()
                    .align(Alignment.CenterVertically),
                value = phoneNumber,
                onValueChange = { onPhoneNumberChange(it) },
                label = "8자리번호",
                onFocusIn = onPhoneNumberFocusIn,
                onFocusOut = onPhoneNumberFocusOut,
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Send,
                keyboardActions = KeyboardActions(onNext = {
                    focusManager.clearFocus()
                    onSendButtonClick()
                }),
                isError = isPhoneNumberError,
                readOnly = false,
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
                    onSendButtonClick()
                },
                enabled = phoneNumber.length == 8,
                text = if (isSmsSent) "재전송" else "전송"
            )
        }

        Comment(
            visible = phoneNumber.length != 8 || !Patterns.PHONE.matcher(phoneNumber).matches(),
            text = "전화번호 8자리를 확인해주세요"
        )
    }
}