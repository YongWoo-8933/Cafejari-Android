package com.software.cafejariapp.presentation.feature.map.screen.mapScreen.dialog

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.software.cafejariapp.core.findActivity
import com.software.cafejariapp.domain.util.DisableDateId
import com.software.cafejariapp.presentation.GlobalState
import com.software.cafejariapp.presentation.component.CustomAlertDialog
import com.software.cafejariapp.presentation.component.VerticalSpacer
import com.software.cafejariapp.presentation.feature.map.event.MapEvent
import com.software.cafejariapp.presentation.feature.map.viewModel.MapViewModel
import com.software.cafejariapp.presentation.theme.HeavyGray
import com.software.cafejariapp.presentation.util.AdId
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LocationPermissionDialog(
    onTodayInvisibleButtonClick: () -> Unit,
    onDismiss: () -> Unit
){

    val activityLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { }
    )

    CustomAlertDialog(
        onDismiss = onDismiss,
        positiveButtonText = "설정하러 가기",
        onPositiveButtonClick = { // 설정으로 보내기
            val appDetailIntent = Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:com.software.cafejariapp")
            )
            activityLauncher.launch(appDetailIntent)
        },
        negativeButtonText = "오늘하루보지않기",
        onNegativeButtonClick = onTodayInvisibleButtonClick,
        content = {

            Text(
                text = "위치 권한이 거절되어있습니다",
                style = MaterialTheme.typography.subtitle2,
                color = MaterialTheme.colors.primary
            )

            VerticalSpacer(height = 8.dp)

            Text("권한을 허용하지 않으시면 전반적인")

            Text("서비스 사용이 불가합니다")

            Text("설정에서 권한을 설정하시겠습니까?")
        }
    )
}