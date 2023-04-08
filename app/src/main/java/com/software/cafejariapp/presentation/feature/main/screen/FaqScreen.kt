package com.software.cafejariapp.presentation.feature.main.screen

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ExpandLess
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.software.cafejariapp.core.customPlaceholder
import com.software.cafejariapp.presentation.GlobalState
import com.software.cafejariapp.presentation.component.*
import com.software.cafejariapp.presentation.feature.main.event.MainEvent
import com.software.cafejariapp.presentation.feature.main.viewModel.MainViewModel
import com.software.cafejariapp.presentation.theme.MoreHeavyGray
import com.software.cafejariapp.presentation.theme.White

@Composable
fun FaqScreen(
    globalState: GlobalState,
    mainViewModel: MainViewModel,
){

    val mainState = mainViewModel.state.value
    val selectedEventIndex = rememberSaveable { mutableStateOf(-1) }

    NetworkChecker(globalState)

    LaunchedEffect(Unit) {
        mainViewModel.onEvent(MainEvent.GetFAQs(globalState))
    }


    Scaffold(
        topBar = {
            BaseTopAppBar(
                isBackButtonEnable = true,
                onBackButtonClick = { globalState.navController.popBackStack() },
                title = "FAQ",
                isScrolled = true
            )
        },
        backgroundColor = White
    ) { paddingValue ->

        CustomSwipeRefresh(
            isLoading = mainState.isFaqsLoading,
            onRefresh = { mainViewModel.onEvent(MainEvent.GetFAQs(globalState)) }
        ) {

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = White)
                    .padding(paddingValue.calculateTopPadding()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                when {
                    mainState.faqs.isEmpty() -> item {

                        EmptyScreen("불러올 수 있는 문의가 없습니다")
                    }
                    else -> itemsIndexed(mainState.faqs) { index, faq ->

                        Column(
                            modifier = Modifier
                                .clickable {
                                    if (selectedEventIndex.value == index) {
                                        selectedEventIndex.value = -1
                                    } else {
                                        selectedEventIndex.value = index
                                    }
                                }
                                .padding(
                                    horizontal = 20.dp,
                                    vertical = 24.dp
                                )
                        ) {

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .customPlaceholder(visible = mainState.isFaqsLoading),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Text(
                                    text = "Q. ${faq.sub_title}",
                                    style = MaterialTheme.typography.subtitle2
                                )

                                Icon(
                                    imageVector = if (selectedEventIndex.value == index) {
                                        Icons.Rounded.ExpandLess
                                    } else {
                                        Icons.Rounded.ExpandMore
                                    },
                                    contentDescription = "faq답변",
                                    tint = MoreHeavyGray,
                                    modifier = Modifier.size(28.dp)
                                )
                            }

                            AnimatedVisibility(
                                visible = selectedEventIndex.value == index,
                                enter = fadeIn() + expandVertically(),
                                exit = fadeOut() + shrinkVertically()
                            ) {
                                Text(
                                    text = "A. ${faq.sub_content}",
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                            }
                        }

                        BaseDivider()
                    }
                }
            }
        }
    }
}

