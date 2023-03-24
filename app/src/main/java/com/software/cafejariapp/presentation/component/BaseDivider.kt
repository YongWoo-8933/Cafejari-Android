package com.software.cafejariapp.presentation.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.software.cafejariapp.presentation.theme.MoreLightGray

@Composable
fun BaseDivider(
    modifier: Modifier = Modifier
){
    Divider(
        modifier = modifier.fillMaxWidth(),
        color = MoreLightGray,
        thickness = 1.dp
    )
}