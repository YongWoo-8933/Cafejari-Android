package com.software.cafejariapp.presentation.feature.map.screen.mapScreen.bottomSheet

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.software.cafejariapp.R
import com.software.cafejariapp.core.isNearBy
import com.software.cafejariapp.presentation.GlobalState
import com.software.cafejariapp.presentation.component.CustomButton
import com.software.cafejariapp.presentation.theme.White
import com.software.cafejariapp.presentation.util.Screen

@Composable
fun CrowdedSharingOrThumbsUpButton(
    globalState: GlobalState,
    onCollapseBottomSheet: () -> Unit,
    thumbsUpRecentLogIdState: MutableState<Int>
) {

    when {
        globalState.modalCafe.value.master.userId == globalState.user.value.userId -> { // 혼잡도 공유 이어하기
            CustomButton(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = MaterialTheme.colors.primary,
                shape = RoundedCornerShape(50),
                elevation = 0.dp,
                contentPadding = 12.dp,
                iconImageResource = R.drawable.edit,
                iconColor = White,
                text = "혼잡도 공유 이어서 하기",
                textColor = White,
                textStyle = MaterialTheme.typography.button,
                onClick = {
                    globalState.navController.navigate(Screen.MasterRoomScreen.route)
                    onCollapseBottomSheet()
                }
            )
        }
        !globalState.modalCafe.value.isMasterAvailable() -> { // 좋아요 누르기
            CustomButton(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = MaterialTheme.colors.primary,
                shape = RoundedCornerShape(50),
                elevation = 0.dp,
                contentPadding = 12.dp,
                text = "도움이 되었다면 꾹 누르기!",
                textColor = White,
                textStyle = MaterialTheme.typography.button,
                iconImageResource = R.drawable.thumb_up,
                iconColor = White,
                onClick = {
                    when {
                        globalState.userLocation.value == null -> globalState.showSnackBar("위치 권한을 허용해주세요!")
                        !globalState.userLocation.value!!.isNearBy(
                            globalState.modalCafeInfo.value.latitude,
                            globalState.modalCafeInfo.value.longitude
                        ) -> {
                            globalState.showSnackBar("카페와 거리가 너무 멉니다. 위치 조정 후 다시 시도해주세요")
                        }
                        globalState.user.value.userId == globalState.modalCafe.value.master.userId -> {
                            globalState.showSnackBar("자신의 활동에는 좋아요를 누를 수 없어요")
                        }
                        else -> {
                            thumbsUpRecentLogIdState.value = globalState.modalCafe.value.recentUpdatedLogs[0].id
                        }
                    }
                }
            )
        }
        !globalState.isMasterActivated.value -> { // 마스터 하러가기
            CustomButton(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = MaterialTheme.colors.primary,
                shape = RoundedCornerShape(50),
                elevation = 0.dp,
                contentPadding = 12.dp,
                iconImageResource = R.drawable.edit,
                iconColor = White,
                text = "혼잡도 공유하고 포인트 받기",
                textColor = White,
                textStyle = MaterialTheme.typography.button,
                onClick = {
                    globalState.navController.navigate(Screen.MasterRoomScreen.route)
                    onCollapseBottomSheet()
                }
            )
        }
        else -> { // 다른 카페에서 마스터 활동중
            CustomButton(modifier = Modifier.fillMaxWidth(),
                disabledBackgroundColor = MaterialTheme.colors.primaryVariant,
                shape = RoundedCornerShape(50),
                elevation = 0.dp,
                contentPadding = 12.dp,
                text = "이미 다른 카페에서 활동중이에요",
                textColor = White,
                textStyle = MaterialTheme.typography.button,
                enabled = false,
                onClick = {}
            )
        }
    }
}