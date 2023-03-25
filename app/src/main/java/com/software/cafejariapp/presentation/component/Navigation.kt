package com.software.cafejariapp.presentation.component

import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.software.cafejariapp.presentation.GlobalState
import com.software.cafejariapp.presentation.util.Screen
import com.software.cafejariapp.presentation.util.TransitionAnimations
import com.software.cafejariapp.presentation.feature.login.screen.AuthScreen
import com.software.cafejariapp.presentation.feature.login.screen.LoginScreen
import com.software.cafejariapp.presentation.feature.login.viewModel.LoginViewModel
import com.software.cafejariapp.presentation.feature.login.screen.PermissionScreen
import com.software.cafejariapp.presentation.feature.login.screen.RegisterScreen
import com.software.cafejariapp.presentation.feature.login.screen.ResetScreen
import com.software.cafejariapp.presentation.feature.login.screen.CheckLoginScreen
import com.software.cafejariapp.presentation.feature.main.screen.FaqScreen
import com.software.cafejariapp.presentation.feature.main.screen.InquiryScreen
import com.software.cafejariapp.presentation.feature.main.screen.InquiryAnswerScreen
import com.software.cafejariapp.presentation.feature.main.screen.LeaderBoardScreen
import com.software.cafejariapp.presentation.feature.main.screen.CafeCorrectionScreen
import com.software.cafejariapp.presentation.feature.main.screen.profileEditScreen.ProfileEditScreen
import com.software.cafejariapp.presentation.feature.main.screen.EventScreen
import com.software.cafejariapp.presentation.feature.main.screen.RegisterCafeScreen
import com.software.cafejariapp.presentation.feature.main.screen.RegisterCafeResultScreen
import com.software.cafejariapp.presentation.feature.main.screen.ShopScreen
import com.software.cafejariapp.presentation.feature.main.screen.ShoppingBagScreen
import com.software.cafejariapp.presentation.feature.main.screen.*
import com.software.cafejariapp.presentation.feature.main.screen.profileScreen.ProfileScreen
import com.software.cafejariapp.presentation.feature.main.viewModel.LeaderBoardViewModel
import com.software.cafejariapp.presentation.feature.main.viewModel.MainViewModel
import com.software.cafejariapp.presentation.feature.main.viewModel.ProfileViewModel
import com.software.cafejariapp.presentation.feature.main.viewModel.ShopViewModel
import com.software.cafejariapp.presentation.feature.map.screen.PointResultScreen
import com.software.cafejariapp.presentation.feature.map.screen.mapScreen.MapScreen
import com.software.cafejariapp.presentation.feature.map.screen.masterRoom.MasterRoomScreen
import com.software.cafejariapp.presentation.feature.map.util.PointResultType
import com.software.cafejariapp.presentation.feature.map.viewModel.AdViewModel
import com.software.cafejariapp.presentation.feature.map.viewModel.MapViewModel
import com.software.cafejariapp.presentation.feature.map.viewModel.MasterRoomViewModel

@ExperimentalPagerApi
@ExperimentalAnimationApi
@ExperimentalPermissionsApi
@ExperimentalMaterialApi
@Composable
fun Navigation(
    globalState: GlobalState,
    bottomPaddingValue: Dp
) {
    
    val loginViewModel: LoginViewModel = hiltViewModel()
    val mapViewModel: MapViewModel = hiltViewModel()
    val masterRoomViewModel: MasterRoomViewModel = hiltViewModel()
    val adViewModel: AdViewModel = hiltViewModel()
    val mainViewModel: MainViewModel = hiltViewModel()
    val leaderBoardViewModel: LeaderBoardViewModel = hiltViewModel()
    val profileViewModel: ProfileViewModel = hiltViewModel()
    val shopViewModel: ShopViewModel = hiltViewModel()

    val pointResultScreenPoint = rememberSaveable { mutableStateOf(0) }
    val pointResultScreenType: MutableState<PointResultType?> = rememberSaveable { mutableStateOf(null) }

    AnimatedNavHost(
        navController = globalState.navController,
        startDestination = Screen.SplashScreen.route
    ) {

        // login ///////////////////////////////////////////////////////////////////////////////////
        composable(
            route = Screen.AuthScreen.route,
            enterTransition = { TransitionAnimations.rightToLeftEnterTransition },
            exitTransition = { TransitionAnimations.fadeOutAnimation },
            popEnterTransition = { TransitionAnimations.fadeInAnimation },
            popExitTransition = { TransitionAnimations.leftToRightExitTransition }
        ) {

            AuthScreen(
                globalState = globalState,
                loginViewModel = loginViewModel
            )
        }

        composable(
            route = Screen.CheckLoginScreen.route,
            enterTransition = { TransitionAnimations.fadeInAnimation },
            exitTransition = { TransitionAnimations.fadeOutAnimation },
            popEnterTransition = { TransitionAnimations.fadeInAnimation },
            popExitTransition = { TransitionAnimations.fadeOutAnimation },
        ) {

            CheckLoginScreen(
                globalState = globalState,
                loginViewModel = loginViewModel
            )
        }

        composable(
            route = Screen.LoginScreen.route,
            enterTransition = { TransitionAnimations.rightToLeftEnterTransition },
            exitTransition = { TransitionAnimations.fadeOutAnimation },
            popEnterTransition = { TransitionAnimations.fadeInAnimation },
            popExitTransition = { TransitionAnimations.leftToRightExitTransition }
        ) {

            LoginScreen(
                globalState = globalState,
                loginViewModel = loginViewModel
            )
        }

        composable(
            route = Screen.PermissionScreen.route,
            enterTransition = { TransitionAnimations.rightToLeftEnterTransition },
            exitTransition = { TransitionAnimations.fadeOutAnimation },
            popEnterTransition = { TransitionAnimations.fadeInAnimation },
            popExitTransition = { TransitionAnimations.leftToRightExitTransition }
        ) {

            PermissionScreen(globalState)
        }

        composable(
            route = Screen.RegisterScreen.route,
            enterTransition = { TransitionAnimations.rightToLeftEnterTransition },
            exitTransition = { TransitionAnimations.fadeOutAnimation },
            popEnterTransition = { TransitionAnimations.fadeInAnimation },
            popExitTransition = { TransitionAnimations.leftToRightExitTransition }
        ) {

            RegisterScreen(
                globalState = globalState,
                loginViewModel = loginViewModel
            )
        }

        composable(
            route = Screen.ResetScreen.route,
            enterTransition = { TransitionAnimations.rightToLeftEnterTransition },
            popExitTransition = { TransitionAnimations.leftToRightExitTransition },
        ) {

            ResetScreen(
                globalState = globalState,
                loginViewModel = loginViewModel
            )
        }



        // map /////////////////////////////////////////////////////////////////////////////////////
        composable(
            route = Screen.MapScreen.route,
            enterTransition = { TransitionAnimations.fadeInAnimation },
            exitTransition = { TransitionAnimations.fadeOutAnimation },
            popEnterTransition = { TransitionAnimations.fadeInAnimation },
        ) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        bottom = animateDpAsState(
                            targetValue = if (globalState.isBottomBarVisible.value) bottomPaddingValue else 0.dp,
                            animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing)
                        ).value
                    )
            ) {

                MapScreen(
                    globalState = globalState,
                    mapViewModel = mapViewModel,
                    adViewModel = adViewModel,
                    onNavigateToPointResultScreen = { point, type ->
                        pointResultScreenPoint.value = point
                        pointResultScreenType.value = type
                        globalState.navController.navigate(Screen.PointResultScreen.route) {
                            popUpTo(Screen.MapScreen.route)
                        }
                    }
                )
            }
        }

        composable(
            route = Screen.MasterRoomScreen.route,
            enterTransition = { TransitionAnimations.rightToLeftEnterTransition },
            exitTransition = { TransitionAnimations.fadeOutAnimation },
            popExitTransition = { TransitionAnimations.leftToRightExitTransition }
        ) {

            MasterRoomScreen(
                globalState = globalState,
                masterRoomViewModel = masterRoomViewModel,
                adViewModel = adViewModel,
                onNavigateToPointResultScreen = { point, type ->
                    pointResultScreenPoint.value = point
                    pointResultScreenType.value = type
                    globalState.navController.navigate(Screen.PointResultScreen.route) {
                        popUpTo(Screen.MapScreen.route)
                    }
                }
            )
        }

        composable(
            route = Screen.PointResultScreen.route,
            enterTransition = { TransitionAnimations.rightToLeftEnterTransition },
            exitTransition = { TransitionAnimations.fadeOutAnimation },
            popExitTransition = { TransitionAnimations.leftToRightExitTransition }
        ) {

            PointResultScreen(
                globalState = globalState,
                point = pointResultScreenPoint.value,
                type = pointResultScreenType.value ?: PointResultType.MasterExpired
            )
        }



        // leaderboard /////////////////////////////////////////////////////////////////////////////
        composable(
            route = Screen.LeaderBoardScreen.route,
            enterTransition = { TransitionAnimations.fadeInAnimation },
            exitTransition = { TransitionAnimations.fadeOutAnimation },
            popEnterTransition = { TransitionAnimations.fadeInAnimation },
            popExitTransition = { TransitionAnimations.fadeOutAnimation },
        ) {

            LeaderBoardScreen(
                globalState = globalState,
                leaderBoardViewModel = leaderBoardViewModel
            )
        }


        // shop ////////////////////////////////////////////////////////////////////////////////////
        composable(
            route = Screen.ShopScreen.route,
            enterTransition = { TransitionAnimations.fadeInAnimation },
            exitTransition = { TransitionAnimations.fadeOutAnimation },
            popEnterTransition = { TransitionAnimations.fadeInAnimation },
            popExitTransition = { TransitionAnimations.fadeOutAnimation },
        ) {

            ShopScreen(
                globalState = globalState,
                shopViewModel = shopViewModel
            )
        }

        composable(
            route = Screen.ShoppingBagScreen.route,
            enterTransition = { TransitionAnimations.rightToLeftEnterTransition },
            popExitTransition = { TransitionAnimations.leftToRightExitTransition }
        ) {

            ShoppingBagScreen(
                globalState = globalState,
                shopViewModel = shopViewModel
            )
        }



        // profile /////////////////////////////////////////////////////////////////////////////////
        composable(
            route = Screen.ProfileScreen.route,
            enterTransition = { TransitionAnimations.fadeInAnimation },
            exitTransition = { TransitionAnimations.fadeOutAnimation },
            popEnterTransition = { TransitionAnimations.fadeInAnimation },
            popExitTransition = { TransitionAnimations.fadeOutAnimation },
        ) {

            ProfileScreen(
                globalState = globalState,
                profileViewModel = profileViewModel
            )
        }

        composable(
            route = Screen.ProfileEditScreen.route,
            enterTransition = { TransitionAnimations.rightToLeftEnterTransition },
            popExitTransition = { TransitionAnimations.leftToRightExitTransition }
        ) {

            ProfileEditScreen(
                globalState = globalState,
                profileViewModel = profileViewModel
            )
        }

        composable(
            route = Screen.ProfileKalendarScreen.route,
            enterTransition = { TransitionAnimations.rightToLeftEnterTransition },
            popExitTransition = { TransitionAnimations.leftToRightExitTransition }
        ) {

            ProfileKalendarScreen(
                globalState = globalState,
                profileViewModel = profileViewModel
            )
        }

        composable(
            route = Screen.PointHistoryScreen.route,
            enterTransition = { TransitionAnimations.rightToLeftEnterTransition },
            popExitTransition = { TransitionAnimations.leftToRightExitTransition }
        ) {

            PointHistoryScreen(
                globalState = globalState,
                profileViewModel = profileViewModel
            )
        }



        // main ////////////////////////////////////////////////////////////////////////////////////
        composable(
            route = Screen.EventScreen.route,
            enterTransition = { TransitionAnimations.rightToLeftEnterTransition },
            exitTransition = { TransitionAnimations.fadeOutAnimation },
            popEnterTransition = { TransitionAnimations.fadeInAnimation },
            popExitTransition = { TransitionAnimations.leftToRightExitTransition },
        ) {

            EventScreen(
                globalState = globalState,
                mainViewModel = mainViewModel
            )
        }

        composable(
            route = Screen.FaqScreen.route,
            enterTransition = { TransitionAnimations.rightToLeftEnterTransition },
            exitTransition = { TransitionAnimations.fadeOutAnimation },
            popEnterTransition = { TransitionAnimations.fadeInAnimation },
            popExitTransition = { TransitionAnimations.leftToRightExitTransition },
        ) {

            FaqScreen(
                globalState = globalState,
                mainViewModel = mainViewModel
            )
        }

        composable(
            route = Screen.RegisterCafeScreen.route,
            enterTransition = { TransitionAnimations.rightToLeftEnterTransition },
            exitTransition = { TransitionAnimations.fadeOutAnimation },
            popEnterTransition = { TransitionAnimations.fadeInAnimation },
            popExitTransition = { TransitionAnimations.leftToRightExitTransition }
        ) {

            RegisterCafeScreen(
                globalState = globalState,
                mainViewModel = mainViewModel
            )
        }

        composable(
            route = Screen.InquiryScreen.route,
            enterTransition = { TransitionAnimations.rightToLeftEnterTransition },
            exitTransition = { TransitionAnimations.fadeOutAnimation },
            popEnterTransition = { TransitionAnimations.fadeInAnimation },
            popExitTransition = { TransitionAnimations.leftToRightExitTransition }
        ) {

            InquiryScreen(
                globalState = globalState,
                mainViewModel = mainViewModel
            )
        }

        composable(
            route = Screen.RegisterCafeResultScreen.route,
            enterTransition = { TransitionAnimations.rightToLeftEnterTransition },
            popExitTransition = { TransitionAnimations.leftToRightExitTransition },
        ) {

            RegisterCafeResultScreen(
                globalState = globalState,
                mainViewModel = mainViewModel
            )
        }

        composable(
            route = Screen.InquiryAnswerScreen.route,
            enterTransition = { TransitionAnimations.rightToLeftEnterTransition },
            popExitTransition = { TransitionAnimations.leftToRightExitTransition },
        ) {

            InquiryAnswerScreen(
                globalState = globalState,
                mainViewModel = mainViewModel
            )
        }

        composable(
            route = Screen.ModifyAdditionalCafeInfoScreen.route,
            enterTransition = { TransitionAnimations.rightToLeftEnterTransition },
            popExitTransition = { TransitionAnimations.leftToRightExitTransition },
        ) {

            CafeCorrectionScreen(
                globalState = globalState,
                mainViewModel = mainViewModel
            )
        }



        // no viewModel ////////////////////////////////////////////////////////////////////////////
        composable(
            route = Screen.SplashScreen.route,
            enterTransition = { TransitionAnimations.fadeInAnimation },
            exitTransition = { TransitionAnimations.fadeOutAnimation }
        ) {

            SplashScreen(globalState = globalState)
        }

        composable(
            route = Screen.GuideListScreen.route,
            enterTransition = { TransitionAnimations.rightToLeftEnterTransition },
            exitTransition = { TransitionAnimations.fadeOutAnimation },
            popEnterTransition = { TransitionAnimations.fadeInAnimation },
            popExitTransition = { TransitionAnimations.leftToRightExitTransition },
        ) {

            GuideListScreen(globalState = globalState)
        }

        composable(
            route = Screen.UpdateScreen.route,
            enterTransition = { TransitionAnimations.rightToLeftEnterTransition },
            popExitTransition = { TransitionAnimations.leftToRightExitTransition }
        ) {

            UpdateScreen(globalState)
        }

        composable(
            route = Screen.WebViewScreen.route,
            enterTransition = { TransitionAnimations.rightToLeftEnterTransition },
            popExitTransition = { TransitionAnimations.leftToRightExitTransition },
        ) {

            WebViewScreen(globalState)
        }
    }
}