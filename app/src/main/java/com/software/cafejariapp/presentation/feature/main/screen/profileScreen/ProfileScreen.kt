package com.software.cafejariapp.presentation.feature.main.screen.profileScreen

import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.pager.ExperimentalPagerApi
import com.naver.maps.map.app.LegalNoticeActivity
import com.naver.maps.map.app.OpenSourceLicenseActivity
import com.software.cafejariapp.R
import com.software.cafejariapp.data.source.remote.HttpRoutes
import com.software.cafejariapp.presentation.GlobalState
import com.software.cafejariapp.domain.entity.Version
import com.software.cafejariapp.presentation.component.*
import com.software.cafejariapp.presentation.feature.main.event.ProfileEvent
import com.software.cafejariapp.presentation.feature.main.viewModel.ProfileViewModel
import com.software.cafejariapp.presentation.theme.Gray
import com.software.cafejariapp.presentation.theme.White
import com.software.cafejariapp.presentation.util.Screen
import com.software.cafejariapp.presentation.util.Time

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ProfileScreen(
    globalState: GlobalState,
    profileViewModel: ProfileViewModel,
) {

    val profileState = profileViewModel.state.value
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current
    val isLogoutDialogOpened = rememberSaveable { mutableStateOf(false) }
    val activityLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { }
    )
    val naverMapLegalNoticeIntent = Intent(
        context,
        LegalNoticeActivity::class.java
    )
    val naverMapOpenSourceLicenseIntent = Intent(
        context,
        OpenSourceLicenseActivity::class.java
    )

    NetworkChecker(globalState)

    LaunchedEffect(Unit) {
        profileViewModel.onEvent(ProfileEvent.ProfileScreenInit(globalState))
    }

    BackHandler(
        enabled = true,
        onBack = {
            when {
                isLogoutDialogOpened.value -> isLogoutDialogOpened.value = false
                else -> globalState.navController.popBackStack()
            }
        }
    )

    if (isLogoutDialogOpened.value) {
        CustomAlertDialog(
            onDismiss = { isLogoutDialogOpened.value = false },
            positiveButtonText = "로그아웃",
            onPositiveButtonClick = { profileViewModel.onEvent(ProfileEvent.Logout(globalState)) },
            negativeButtonText = "아니오",
            onNegativeButtonClick = { },
            content = {
                Text(
                    text = "정말 로그아웃 하시겠습니까?",
                    style = MaterialTheme.typography.subtitle1
                )
            }
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxWidth(),
        topBar = {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .height(80.dp)
                    .statusBarsPadding()
                    .background(color = MaterialTheme.colors.primary)
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom
            ) {

                Text(
                    text = "카페자리와 함께한지 ",
                    color = Color.White,
                    style = MaterialTheme.typography.subtitle1
                )

                Text(
                    text = if (globalState.isLoggedIn.value) {
                        "${Time.getPassingDayFrom(globalState.user.value.dateJoined)}일 째"
                    } else {
                        ""
                    },
                    style = MaterialTheme.typography.subtitle2,
                    color = Color.White
                )
            }
        }
    ) { paddingValue ->

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = paddingValue.calculateTopPadding())
                .background(color = Color.White),
        ) {

            item {

                ProfileInfo(
                    user = globalState.user.value,
                    onProfileImageClick = { globalState.navController.navigate(Screen.ProfileEditScreen.route) }
                )
            }

            item {

                ProfileNavigateButton(
                    leadingIconResourceId = R.drawable.coin_icon,
                    text = "${globalState.user.value.point}P  (내역보기)",
                    onClick = {
                        globalState.navController.navigate(Screen.PointHistoryScreen.route)
                    }
                )

                VerticalSpacer(height = 8.dp)
            }

            item {

                ProfileNavigateButton(
                    imageVector = Icons.Rounded.CalendarMonth,
                    text = "내 혼잡도 공유활동 보러가기",
                    onClick = {
                        globalState.navController.navigate(Screen.ProfileKalendarScreen.route)
                    }
                )

                VerticalSpacer(height = 20.dp)
            }

            item {

                ProfileEventBanner(
                    pagerState = profileState.pagerState,
                    events = profileState.events,
                    onEventClick = {
                        globalState.navigateToWebView(
                            topAppBarTitle = "이벤트 상세",
                            url = it.url
                        )
                    }
                )

                VerticalSpacer(height = 32.dp)
            }

            item {

                ProfileTitleRow("안내")

                VerticalSpacer(height = 16.dp)
            }

            items(
                listOf(
                    ProfileDetail(
                        text = "FAQ",
                        onClick = { globalState.navController.navigate(Screen.FaqScreen.route) }
                    ),
                    ProfileDetail(
                        text = "사용 가이드 보기",
                        onClick = { globalState.navController.navigate(Screen.GuideListScreen.route) }
                    ),
                    ProfileDetail(
                        text = "이벤트 모아보기",
                        onClick = { globalState.navController.navigate(Screen.EventScreen.route) }
                    ),
                    ProfileDetail(
                        text = "업데이트 소식",
                        onClick = { globalState.navController.navigate(Screen.UpdateScreen.route) }
                    ),
                    ProfileDetail(
                        text = "1:1 문의",
                        onClick = { globalState.navController.navigate(Screen.InquiryScreen.route) }
                    )
                )
            ) { profileDetail ->

                ProfileDetailRow(
                    text = profileDetail.text,
                    onClick = profileDetail.onClick
                )

                VerticalSpacer(height = 16.dp)
            }

            item {

                VerticalSpacer(height = 16.dp)
            }

            item {

                ProfileTitleRow("채널")

                VerticalSpacer(height = 16.dp)
            }

            items(
                listOf(
                    ProfileDetail(
                        text = "카페자리 인스타 구경하기",
                        onClick = { uriHandler.openUri(HttpRoutes.INSTAGRAM) }
                    ),
                    ProfileDetail(
                        text = "카페자리 네이버 블로그 구경하기",
                        onClick = { uriHandler.openUri(HttpRoutes.BLOG) }
                    )
                )
            ) { profileDetail ->

                ProfileDetailRow(
                    text = profileDetail.text,
                    onClick = profileDetail.onClick
                )

                VerticalSpacer(height = 16.dp)
            }

            item {

                VerticalSpacer(height = 16.dp)
            }

            item {

                ProfileTitleRow("약관 및 처리방침")

                VerticalSpacer(height = 16.dp)
            }

            items(
                listOf(
                    ProfileDetail(
                        text = "개인정보 처리방침",
                        onClick = { globalState.navigateToWebView(
                            topAppBarTitle = "개인정보 처리방침",
                            url = HttpRoutes.PRIVACY_POLICY
                        ) }
                    ),
                    ProfileDetail(
                        text = "위치기반 서비스 이용약관",
                        onClick = { globalState.navigateToWebView(
                            topAppBarTitle = "위치기반 서비스 이용약관",
                            url = HttpRoutes.TOS
                        ) }
                    ),
                    ProfileDetail(
                        text = "[네이버 지도] 법적공지 / 정보 제공처",
                        onClick = { activityLauncher.launch(naverMapLegalNoticeIntent) }
                    ),
                    ProfileDetail(
                        text = "[네이버 지도] 오픈소스 라이선스",
                        onClick = { activityLauncher.launch(naverMapOpenSourceLicenseIntent) }
                    ),
                )
            ) { profileDetail ->

                ProfileDetailRow(
                    text = profileDetail.text,
                    onClick = profileDetail.onClick
                )

                VerticalSpacer(height = 16.dp)
            }

            item {

                VerticalSpacer(height = 32.dp)
            }

            item {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = White)
                        .padding(20.dp),
                ) {

                    Text(
                        modifier = Modifier.clickable { isLogoutDialogOpened.value = true },
                        text = "로그아웃",
                        style = MaterialTheme.typography.subtitle1,
                        textDecoration = TextDecoration.Underline,
                        color = Gray
                    )

                    VerticalSpacer(height = 40.dp)

                    BaseDivider()

                    VerticalSpacer(height = 20.dp)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "ver " + "${Version.current.release}.${Version.current.major}.${Version.current.minor}",
                            color = Gray
                        )
                    }

                    VerticalSpacer(height = 80.dp)
                }
            }
        }
    }
}


@Composable
fun ProfileTitleRow(
    text: String
) {

    Row(
        modifier = Modifier
            .background(color = White)
            .fillMaxWidth()
            .padding(start = 20.dp, end = 25.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Text(
            text = text,
            style = MaterialTheme.typography.subtitle2
        )
    }
}

@Composable
fun ProfileDetailRow(
    text: String,
    onClick: () -> Unit,
) {

    Row(
        modifier = Modifier
            .background(color = White)
            .fillMaxWidth()
            .padding(start = 20.dp, end = 25.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Text(text)

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {

            Icon(
                modifier = Modifier.size(18.dp),
                imageVector = Icons.Rounded.NavigateNext,
                contentDescription = "해당 컨텐츠로 이동",
                tint = Gray
            )
        }
    }
}

data class ProfileDetail(
    val text: String,
    val onClick: () -> Unit
)
