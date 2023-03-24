package com.software.cafejariapp.presentation.component

import androidx.compose.animation.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.software.cafejariapp.presentation.component.VerticalSpacer

@Composable
fun Comment(
    visible: Boolean,
    text: String,
    textColor: Color = MaterialTheme.colors.primaryVariant,
    error: Boolean = false
) {

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
    ) {

        Column(
            modifier = Modifier.padding(horizontal = 4.dp)
        ) {

            VerticalSpacer(height = 4.dp)

            Text(
                text = text,
                style = MaterialTheme.typography.body1,
                color = if (error) MaterialTheme.colors.error else textColor,
            )
        }
    }
}