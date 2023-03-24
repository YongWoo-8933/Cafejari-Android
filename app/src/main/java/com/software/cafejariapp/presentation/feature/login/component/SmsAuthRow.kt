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
fun SmsAuthRow(
    authNumber: String,
    isAuthNumberError: Boolean,
    onNumberChange: (String) -> Unit,
    onFocusIn: () -> Unit,
    onFocusOut: () -> Unit,
    onAuthButtonClick: () -> Unit,
    isAuthed: Boolean
) {

    val focusManager = LocalFocusManager.current

    LaunchedEffect(key1 = authNumber) {
        if (authNumber.length == 6 && Patterns.PHONE.matcher(authNumber).matches()) {
            focusManager.clearFocus()
        }
    }

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
            value = authNumber,
            onValueChange = { onNumberChange(it) },
            label = "인증번호",
            onFocusIn = onFocusIn,
            onFocusOut = onFocusOut,
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done,
            keyboardActions = KeyboardActions(onDone = {
                focusManager.clearFocus()
                onAuthButtonClick()
            }),
            isError = isAuthNumberError,
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
                onAuthButtonClick()
            },
            enabled = authNumber.length == 6 && Patterns.PHONE.matcher(authNumber).matches(),
            text = "인증"
        )
    }

    Comment(
        visible = !isAuthed, text = "인증을 완료해주세요"
    )
}