package com.software.cafejariapp.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.software.cafejariapp.presentation.theme.TextBlack


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseTopAppBar(
    isBackButtonEnable: Boolean = false,
    onBackButtonClick: () -> Unit = {},
    trailingIconAction: @Composable () -> Unit = {},
    title: String = "",
    backgroundColor: Color = MaterialTheme.colors.background,
    titleColor: Color = MaterialTheme.colors.primary,
    iconColor: Color = TextBlack,
    isScrolled: Boolean = false
){

    Column(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ){
            CenterAlignedTopAppBar(
                modifier = Modifier
                    .background(color = backgroundColor)
                    .statusBarsPadding(),
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = backgroundColor,
                    navigationIconContentColor = iconColor,
                    titleContentColor = titleColor,
                    actionIconContentColor = iconColor
                ),
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically){
                        Text(
                            style = MaterialTheme.typography.h5,
                            text = title,
                            color = titleColor
                        )
                    }
                },
                navigationIcon = {
                    if (isBackButtonEnable) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(start = 15.dp)
                                .clickable { onBackButtonClick() }
                        ) {
                            Icon(
                                Icons.Rounded.NavigateBefore,
                                "뒤로가기",
                                tint = iconColor,
                                modifier = Modifier.size(30.dp)
                            )
                            HorizontalSpacer(width = 1.dp)
                        }
                    }
                },
                actions = {
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(15.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.End
                    ) {
                        trailingIconAction()
                    }
                }
            )
        }
        if(isScrolled) Divider(color = backgroundColor, thickness = 1.dp)
    }
}