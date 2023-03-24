package com.software.cafejariapp.presentation.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.software.cafejariapp.presentation.theme.HeavyGray
import com.software.cafejariapp.presentation.theme.Transparent

@Composable
fun BottomSheetHandle(
    modifier: Modifier = Modifier
){

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        VerticalSpacer(height = 8.dp)

        Divider(
            modifier = Modifier
                .width(60.dp)
                .align(Alignment.CenterHorizontally)
                .border(6.dp, HeavyGray, shape = RoundedCornerShape(50)),
            color = Transparent,
            thickness = 5.dp
        )

        VerticalSpacer(height = 3.dp)
    }
}