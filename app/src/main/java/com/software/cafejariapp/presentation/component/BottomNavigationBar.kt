package com.software.cafejariapp.presentation.component

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Map
import androidx.compose.material.icons.rounded.MilitaryTech
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.compose.currentBackStackEntryAsState
import com.software.cafejariapp.presentation.GlobalState
import com.software.cafejariapp.presentation.util.BottomNavItem
import com.software.cafejariapp.presentation.util.Screen
import com.software.cafejariapp.presentation.theme.Gray
import com.software.cafejariapp.presentation.theme.White
import kotlinx.coroutines.delay

@Composable
fun BottomNavigationBar(
    globalState: GlobalState
) {

    val isScreenTransitionLimited = rememberSaveable { mutableStateOf(false) }
    val backStackEntry = globalState.navController.currentBackStackEntryAsState()
    val currentScreenRoute = backStackEntry.value?.destination?.route

    when (currentScreenRoute) {
        Screen.MapScreen.route,
        Screen.ShopScreen.route,
        Screen.LeaderBoardScreen.route,
        Screen.ProfileScreen.route -> {
            globalState.isBottomBarVisible.value = true
        }
        else -> globalState.isBottomBarVisible.value = false
    }

    LaunchedEffect(isScreenTransitionLimited.value) {
        if (isScreenTransitionLimited.value) {
            delay(200L)
            isScreenTransitionLimited.value = false
        }
    }

    AnimatedVisibility(
        visible = globalState.isBottomBarVisible.value,
        enter = fadeIn() + expandVertically(expandFrom = Alignment.Bottom),
        exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.Bottom)
    ) {

        BottomNavigation(
            modifier = Modifier.zIndex(9f),
            backgroundColor = White,
            contentColor = Color.Black,
            elevation = 5.dp
        ) {

            val items = listOf(
                BottomNavItem(
                    name = "지도", route = Screen.MapScreen.route, imageVector = Icons.Rounded.Map
                ),
                BottomNavItem(
                    name = "랭킹",
                    route = Screen.LeaderBoardScreen.route,
                    imageVector = Icons.Rounded.MilitaryTech
                ),
                BottomNavItem(
                    name = "상점",
                    route = Screen.ShopScreen.route,
                    imageVector = Icons.Rounded.ShoppingCart
                ),
                BottomNavItem(
                    name = "프로필",
                    route = Screen.ProfileScreen.route,
                    imageVector = Icons.Rounded.AccountCircle
                ),
            )

            HorizontalSpacer(width = 15.dp)

            items.forEach { item ->
                val selected = currentScreenRoute == item.route
                val contentColor = if (selected) MaterialTheme.colors.primary else Gray

                BottomNavigationItem(
                    selected = selected,
                    onClick = {
                        if (!selected && !isScreenTransitionLimited.value) {
                            if (item.route == Screen.MapScreen.route) {
                                globalState.navController.popBackStack()
                            } else {
                                globalState.navController.navigate(item.route) {
                                    popUpTo(Screen.MapScreen.route)
                                }
                            }
                        }
                    },
                    icon = {
                        Column(
                            horizontalAlignment = CenterHorizontally
                        ) {
                            Icon(
                                modifier = Modifier.size(28.dp),
                                imageVector = item.imageVector,
                                contentDescription = item.name,
                                tint = contentColor
                            )
                            VerticalSpacer(height = 2.dp)
                            Text(
                                text = item.name,
                                textAlign = TextAlign.Center,
                                fontSize = 9.sp,
                                color = contentColor,
                                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
                            )
                        }
                    }
                )
            }

            HorizontalSpacer(width = 15.dp)
        }
    }
}