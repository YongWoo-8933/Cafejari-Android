package com.software.cafejariapp.presentation.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.software.cafejariapp.core.isNetworkAvailable
import com.software.cafejariapp.presentation.GlobalState

@Composable
fun NetworkChecker(
    globalState: GlobalState
) {

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        if (!context.isNetworkAvailable()) {
            globalState.showSnackBar("원활한 앱 사용을 위해 네트워크 연결을 확인해주세요")
        }
    }
}