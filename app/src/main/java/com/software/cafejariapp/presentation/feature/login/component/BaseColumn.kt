package com.software.cafejariapp.presentation.feature.login.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.software.cafejariapp.presentation.theme.White

@Composable
fun BaseColumn(
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    content: @Composable () -> Unit
) {

    Column(
        modifier = modifier.fillMaxSize()
            .background(color = White)
            .statusBarsPadding()
            .padding(horizontal = 20.dp, vertical = 40.dp),
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment
    ) {
        content()
    }
}