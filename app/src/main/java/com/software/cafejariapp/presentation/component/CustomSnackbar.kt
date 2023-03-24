package com.software.cafejariapp.presentation.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.software.cafejariapp.presentation.theme.White

@Composable
fun CustomSnackbar(
    snackbarData: SnackbarData,
    modifier: Modifier = Modifier
) {

    Snackbar(
        modifier = modifier.padding(12.dp),
        content = {
            Text(text = snackbarData.message, color = White)
        },
        action = if (snackbarData.actionLabel != null) {
            @Composable {
                TextButton(
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = SnackbarDefaults.primaryActionColor
                    ),
                    onClick = { snackbarData.performAction() },
                    content = { Text(
                        text = snackbarData.actionLabel!!,
                        color = White
                    ) }
                )
            }
        } else {
            null
        },
        actionOnNewLine = true,
        shape = MaterialTheme.shapes.large,
        backgroundColor = Color.Black.copy(alpha = 0.7f),
        elevation = 0.dp
    )
}