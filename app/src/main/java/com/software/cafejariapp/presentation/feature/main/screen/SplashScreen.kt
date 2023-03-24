package com.software.cafejariapp.presentation.feature.main.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.software.cafejariapp.R
import com.software.cafejariapp.core.LocationTrackingNotPermitted
import com.software.cafejariapp.presentation.GlobalState
import com.software.cafejariapp.presentation.util.Screen
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    globalState: GlobalState
) {

    val context = LocalContext.current
    val isLightOn = rememberSaveable { mutableStateOf(false) }

    BackHandler(enabled = true, onBack = {})

    LaunchedEffect(Unit) {
        delay(1000L)
        isLightOn.value = true
        delay(100L)
        isLightOn.value = false
        delay(300L)
        isLightOn.value = true
        delay(100L)
        isLightOn.value = false
        delay(300L)
        isLightOn.value = true
        delay(1500L)
        try {
            globalState.startLocationTracking(context)
        } catch (e: LocationTrackingNotPermitted) {
        }
        delay(500L)
        globalState.navController.navigate(Screen.CheckLoginScreen.route) {
            popUpTo(Screen.SplashScreen.route) {
                inclusive = true
            }
            launchSingleTop = true
        }
    }

    Box(
        modifier = Modifier
            .zIndex(10f)
            .fillMaxSize()
            .background(color = MaterialTheme.colors.onError),
        contentAlignment = Alignment.Center
    ) {

        Image(
            painter = painterResource(id = if (isLightOn.value) {
                R.drawable.splash_screen_light_on
            } else {
                R.drawable.splash_screen_light_off
            }),
            contentDescription = "초기 로딩 화면",
            modifier = Modifier.width(346.dp)
        )
    }
}