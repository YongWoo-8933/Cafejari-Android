package com.software.cafejariapp.presentation.feature.main.screen.profileEditScreen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.software.cafejariapp.presentation.GlobalState
import com.software.cafejariapp.presentation.component.*
import com.software.cafejariapp.presentation.feature.main.event.ProfileEvent
import com.software.cafejariapp.presentation.feature.main.viewModel.ProfileViewModel
import com.software.cafejariapp.presentation.theme.White

@Composable
fun ProfileEditScreen(
    globalState: GlobalState,

    profileViewModel: ProfileViewModel,
) {

    val profileState = profileViewModel.state.value
    val isDeleteAccountDialogOpened = rememberSaveable { mutableStateOf(false) }

    NetworkChecker(globalState)

    LaunchedEffect(Unit) {
        profileViewModel.onEvent(ProfileEvent.ProfileEditScreenInit(globalState))
    }

    BackHandler(
        enabled = true,
        onBack = {
            when {
                isDeleteAccountDialogOpened.value -> isDeleteAccountDialogOpened.value = false
                else -> globalState.navController.popBackStack()
            }
        }
    )

    if (isDeleteAccountDialogOpened.value) {
        DeleteAccountDialog(
            onDismiss = { isDeleteAccountDialogOpened.value = false },
            onDeleteAccountButtonClick = {
                profileViewModel.onEvent(ProfileEvent.DeleteAccount(globalState))
            }
        )
    }

    if (profileState.isProfileUpdateLoading) {
        FullSizeLoadingScreen(loadingText = "변경사항 저장중")
    }

    Scaffold(
        topBar = {
            BaseTopAppBar(
                isBackButtonEnable = true,
                onBackButtonClick = { globalState.navController.popBackStack() },
                trailingIconAction = {
                    if (profileState.isProfileEditAuthed) {
                        Text(
                            text = "탈퇴",
                            style = MaterialTheme.typography.caption,
                            textDecoration = TextDecoration.Underline,
                            color = MaterialTheme.colors.error,
                            modifier = Modifier.clickable {
                                isDeleteAccountDialogOpened.value = true
                            }
                        )
                    }
                },
                title = "프로필 편집"
            )
        }
    ) { paddingValue ->

        if (profileState.isSocialUserTypeLoading) {
            FullSizeLoadingScreen(loadingText = "유저 정보 로드중..")
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = White)
                .padding(top = paddingValue.calculateTopPadding())
                .padding(
                    vertical = 40.dp,
                    horizontal = 20.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            AnimatedVisibility(
                visible = !profileState.isProfileEditAuthed,
                enter = fadeIn(),
                exit = fadeOut()
            ) {

                BeforeAuth(
                    globalState = globalState,
                    profileViewModel = profileViewModel
                )
            }

            AnimatedVisibility(
                visible = profileState.isProfileEditAuthed,
                enter = fadeIn(),
                exit = fadeOut()
            ) {

                AfterAuth(
                    globalState = globalState,
                    profileViewModel = profileViewModel
                )
            }
        }
    }
}

@Composable
fun DeleteAccountDialog(
    onDismiss: () -> Unit,
    onDeleteAccountButtonClick: () -> Unit
) {

    CustomAlertDialog(
        onDismiss = onDismiss,
        positiveButtonText = "더 생각해볼게요",
        onPositiveButtonClick = { },
        negativeButtonText = "탈퇴할래요",
        onNegativeButtonClick = onDeleteAccountButtonClick,
        content = {

            Text(
                text = "카페자리에서 탈퇴합니다",
                style = MaterialTheme.typography.h3,
                color = MaterialTheme.colors.primary
            )

            VerticalSpacer(height = 8.dp)

            Text(
                text = "계정 삭제는 3일 이내에 처리되며",
                color = MaterialTheme.colors.primary
            )

            Text(
                text = "이 기간동안 다시 로그인하여",
                color = MaterialTheme.colors.primary
            )

            Text(
                text = "1:1 문의내역을 통해 취소할 수 있습니다",
                color = MaterialTheme.colors.primary
            )

            Text(
                text = "3일 이후에는 모든 계정정보가 삭제됩니다",
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.error
            )

            VerticalSpacer(height = 4.dp)

            Text(
                text = "정말 탈퇴하시겠습니까?",
                style = MaterialTheme.typography.subtitle1,
                color = MaterialTheme.colors.primary
            )
        }
    )
}

















