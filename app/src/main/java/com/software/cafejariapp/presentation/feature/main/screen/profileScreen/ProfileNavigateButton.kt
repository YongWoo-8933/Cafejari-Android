package com.software.cafejariapp.presentation.feature.main.screen.profileScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.software.cafejariapp.presentation.component.HorizontalSpacer
import com.software.cafejariapp.presentation.theme.White

@Composable
fun ProfileNavigateButton(
    leadingIconResourceId: Int? = null,
    imageVector: ImageVector? = null,
    text: String,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxSize()
            .background(color = White)
            .padding(horizontal = 15.dp)
            .clickable { onClick() },
        backgroundColor = MaterialTheme.colors.background,
        shape = MaterialTheme.shapes.medium,
        elevation = 0.dp
    ) {

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {

            if (leadingIconResourceId != null) {
                Icon(
                    painter = painterResource(id = leadingIconResourceId),
                    contentDescription = "포인트 획득양",
                    tint = MaterialTheme.colors.primary
                )
            }
            if (imageVector != null) {
                Icon(
                    imageVector = imageVector,
                    contentDescription = "캘린더로 이동",
                    tint = MaterialTheme.colors.primary
                )
            }

            HorizontalSpacer(width = 16.dp)

            Text(
                text = text,
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.primary
            )
        }
    }
}