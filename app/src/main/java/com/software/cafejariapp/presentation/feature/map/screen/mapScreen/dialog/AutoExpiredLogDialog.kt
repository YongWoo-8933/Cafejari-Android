package com.software.cafejariapp.presentation.feature.map.screen.mapScreen.dialog

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.software.cafejariapp.core.findActivity
import com.software.cafejariapp.core.toFloor
import com.software.cafejariapp.presentation.GlobalState
import com.software.cafejariapp.presentation.component.CustomAlertDialog
import com.software.cafejariapp.presentation.component.VerticalSpacer
import com.software.cafejariapp.presentation.feature.map.event.AdEvent
import com.software.cafejariapp.presentation.feature.map.event.MapEvent
import com.software.cafejariapp.presentation.feature.map.viewModel.AdViewModel
import com.software.cafejariapp.presentation.feature.map.viewModel.MapViewModel
import com.software.cafejariapp.presentation.util.AdId
import com.software.cafejariapp.presentation.util.Time
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AutoExpiredLogDialog(
    globalState: GlobalState,
    mapViewModel: MapViewModel,
    adViewModel: AdViewModel,
    adLoadingState: MutableState<Boolean>,
){

    val context = LocalContext.current

    CustomAlertDialog(
        onDismiss = {
            mapViewModel.onEvent(MapEvent.DeleteAutoExpiredCafeLog(globalState))
        },
        onPositiveButtonClick = {
            adViewModel.onEvent(AdEvent.ShowRewardedAd(
                globalState = globalState,
                context = context,
                loadingState = adLoadingState,
                onSuccess = {
                    mapViewModel.onEvent(MapEvent.AddAdPoint(
                        globalState,
                        globalState.autoExpiredCafeLog.value.cafeLogId)
                    )
                }
            ))
        },
        positiveButtonText = "받을래요",
        negativeButtonText = "닫기",
        onNegativeButtonClick = {
            mapViewModel.onEvent(
                MapEvent.DeleteAutoExpiredCafeLog(globalState)
            )
        },
        content = {

            Text(
                text = "혼잡도 공유 활동 자동종료",
                style = MaterialTheme.typography.subtitle2,
                color = MaterialTheme.colors.primary
            )

            VerticalSpacer(height = 12.dp)

            Row {

                Text(
                    text = "${globalState.autoExpiredCafeLog.value.name} ${globalState.autoExpiredCafeLog.value.floor.toFloor()}층",
                    style = MaterialTheme.typography.body2
                )

                Text("에서의 활동이")
            }

            Text(
                "${
                    if (globalState.autoExpiredCafeLog.value.id != 0) {
                        Time.getYearMonthDay(globalState.autoExpiredCafeLog.value.time) + " " + Time.getHourMinute(
                            globalState.autoExpiredCafeLog.value.time
                        )
                    } else ""
                }에 "
            )

            Row {

                Text("자동 만료되었습니다. ")

                Text(
                    text = "*${globalState.autoExpiredCafeLog.value.point}P 적립",
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.primary
                )
            }

            Text("광고를 보고 추가포인트(${globalState.autoExpiredCafeLog.value.point / 2}P)를")

            Text("받으시겠습니까?")
        }
    )
}