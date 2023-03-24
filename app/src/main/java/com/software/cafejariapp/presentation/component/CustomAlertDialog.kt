package com.software.cafejariapp.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.software.cafejariapp.presentation.theme.Transparent
import com.software.cafejariapp.presentation.theme.White

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CustomAlertDialog(
    onDismiss: () -> Unit,
    positiveButtonText: String,
    onPositiveButtonClick: () -> Unit,
    negativeButtonText: String,
    onNegativeButtonClick: () -> Unit = {},
    content: @Composable ColumnScope.() -> Unit,

    isNegativeButtonEnabled: Boolean = true
) {

    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false,
        ),
    ) {

        Card(
            modifier = Modifier.fillMaxWidth(0.78f),
            backgroundColor = White,
            shape = MaterialTheme.shapes.large
        ) {

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Column(
                    modifier = Modifier.fillMaxWidth()
                        .padding(vertical = 36.dp, horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    content()
                }

                Row(
                    modifier = Modifier.fillMaxWidth()
                        .height(54.dp)
                        .background(MaterialTheme.colors.primary)
                ) {

                    if (isNegativeButtonEnabled) {
                        CustomButton(
                            modifier = Modifier.fillMaxHeight().weight(1f),
                            text = negativeButtonText,
                            textColor = MaterialTheme.colors.onPrimary,
                            textStyle = MaterialTheme.typography.subtitle2,
                            onClick = {
                                onNegativeButtonClick()
                                onDismiss()
                            },
                            backgroundColor = Transparent
                        )
                    }

                    CustomButton(
                        modifier = Modifier.fillMaxHeight().weight(1f),
                        text = positiveButtonText,
                        textColor = White,
                        textStyle = MaterialTheme.typography.subtitle2,
                        onClick = {
                            onPositiveButtonClick()
                            onDismiss()
                        },
                        backgroundColor = Transparent
                    )
                }
            }
        }
    }
}