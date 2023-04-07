package com.software.cafejariapp.presentation.component

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun CustomSwipeRefresh(
    isLoading: Boolean,
    onRefresh: () -> Unit,
    content: @Composable () -> Unit
) {

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = isLoading),
        indicator = { state, trigger ->
            SwipeRefreshIndicator(
                state = state,
                refreshTriggerDistance = trigger,
                contentColor = MaterialTheme.colors.primary,
                elevation = 2.dp
            )
        },
        onRefresh = onRefresh,
        content = content
    )
}