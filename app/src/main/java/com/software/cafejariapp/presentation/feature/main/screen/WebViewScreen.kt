package com.software.cafejariapp.presentation.feature.main.screen

import android.annotation.SuppressLint
import android.webkit.WebResourceRequest
import android.webkit.WebView
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.google.accompanist.web.*
import com.software.cafejariapp.presentation.GlobalState
import com.software.cafejariapp.presentation.component.BaseTopAppBar
import com.software.cafejariapp.presentation.theme.White

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewScreen(
    globalState: GlobalState
) {

    val webViewState = rememberWebViewState(url = globalState.webViewUrl.value)
    val navigator = rememberWebViewNavigator()

    BackHandler(enabled = true) {
        if (navigator.canGoBack) {
            navigator.navigateBack()
        } else {
            globalState.navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            BaseTopAppBar(
                isBackButtonEnable = true,
                onBackButtonClick = { globalState.navController.popBackStack() },
                title = globalState.webViewTitle.value,
                isScrolled = true,
            )
        },
        backgroundColor = White
    ) { paddingValue ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = White)
                .padding(paddingValue.calculateTopPadding())
        ) {

            Column {
                val loadingState = webViewState.loadingState

                if (loadingState is LoadingState.Loading) {
                    LinearProgressIndicator(
                        progress = loadingState.progress,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                val webClient = remember {
                    object : AccompanistWebViewClient() {
                        override fun shouldOverrideUrlLoading(
                            view: WebView?, request: WebResourceRequest?
                        ): Boolean {
                            return false
                        }
                    }
                }

                WebView(
                    state = webViewState,
                    modifier = Modifier.fillMaxSize(),
                    navigator = navigator,
                    captureBackPresses = false,
                    client = webClient,
                    onCreated = { webView ->
                        webView.settings.javaScriptEnabled = true
                        webView.settings.domStorageEnabled = true
                        webView.settings.javaScriptCanOpenWindowsAutomatically = false
                    }
                )
            }
        }
    }
}
