package com.software.cafejariapp.presentation.feature.login.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.software.cafejariapp.presentation.theme.HeavyGray
import com.software.cafejariapp.presentation.theme.LightGray
import com.software.cafejariapp.presentation.theme.White

@Composable
fun SmsButton(
    modifier: Modifier,
    onClick: () -> Unit,
    enabled: Boolean,
    text: String
) {

    Button(
        modifier = modifier.fillMaxHeight(),
        onClick = onClick,
        shape = MaterialTheme.shapes.small,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = HeavyGray,
            disabledBackgroundColor = LightGray,
        ),
        elevation = ButtonDefaults.elevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp
        ),
        enabled = enabled
    ) {

        Text(
            text = text,
            color = White,
            style = MaterialTheme.typography.caption
        )
    }
}