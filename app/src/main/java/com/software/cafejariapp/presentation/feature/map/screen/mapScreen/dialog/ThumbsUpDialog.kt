package com.software.cafejariapp.presentation.feature.map.screen.mapScreen.dialog

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.software.cafejariapp.presentation.GlobalState
import com.software.cafejariapp.presentation.component.CustomAlertDialog
import com.software.cafejariapp.presentation.component.VerticalSpacer
import com.software.cafejariapp.presentation.feature.map.event.AdEvent
import com.software.cafejariapp.presentation.feature.map.event.MapEvent
import com.software.cafejariapp.presentation.feature.map.util.PointResultType
import com.software.cafejariapp.presentation.feature.map.viewModel.AdViewModel
import com.software.cafejariapp.presentation.feature.map.viewModel.MapViewModel
import com.software.cafejariapp.presentation.theme.HeavyGray

@Composable
fun ThumbsUpDialog(
    globalState: GlobalState,
    mapViewModel: MapViewModel,
    adViewModel: AdViewModel,
    adLoadingState: MutableState<Boolean>,
    thumbsUpRecentLogId: Int,
    onDismiss: () -> Unit,
    onNavigateToPointResultScreen: (Int, PointResultType) -> Unit
){

    val context = LocalContext.current

    CustomAlertDialog(
        onDismiss = onDismiss,
        positiveButtonText = "광고보고추천",
        onPositiveButtonClick = {
            onDismiss()
            adViewModel.onEvent(AdEvent.ShowRewardedAd(
                globalState = globalState,
                context = context,
                loadingState = adLoadingState,
                onSuccess = {
                    mapViewModel.onEvent(
                        MapEvent.ThumbsUp(
                            globalState,
                            thumbsUpRecentLogId,
                            true,
                            onSuccess = onNavigateToPointResultScreen
                        )
                    )
                }
            ))
        },
        negativeButtonText = "그냥추천하기",
        onNegativeButtonClick = {
            mapViewModel.onEvent(MapEvent.ThumbsUp(
                globalState,
                thumbsUpRecentLogId,
                false,
                onSuccess = onNavigateToPointResultScreen
            ))
        },
        content = {

            Row {

                Text(
                    text = "광고 보고 추천하면 ",
                    style = MaterialTheme.typography.subtitle1
                )
            }

            Row {

                Text(
                    text = "추가 포인트",
                    style = MaterialTheme.typography.subtitle2
                )

                Text(
                    text = "를 드려요!",
                    style = MaterialTheme.typography.subtitle1
                )
            }

            VerticalSpacer(height = 8.dp)

            Text(
                text = "* 커피 기프티콘 구매 가능",
                color = HeavyGray
            )
        }
    )
}