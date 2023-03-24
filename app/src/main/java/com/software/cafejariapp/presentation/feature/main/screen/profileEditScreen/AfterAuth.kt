package com.software.cafejariapp.presentation.feature.main.screen.profileEditScreen

import android.Manifest
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowForwardIos
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.software.cafejariapp.presentation.GlobalState
import com.software.cafejariapp.presentation.component.*
import com.software.cafejariapp.presentation.feature.main.component.ProfileImage
import com.software.cafejariapp.presentation.feature.main.event.ProfileEvent
import com.software.cafejariapp.presentation.feature.main.viewModel.ProfileViewModel
import com.software.cafejariapp.presentation.theme.HeavyGray
import com.software.cafejariapp.presentation.theme.LightGray
import com.software.cafejariapp.presentation.theme.White

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun AfterAuth(
    globalState: GlobalState,
    profileViewModel: ProfileViewModel
) {

    val nickname = rememberSaveable { mutableStateOf("") }
    val image = rememberSaveable { mutableStateOf<Uri?>(null) }
    val focusManager = LocalFocusManager.current
    val imagePickActivityLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) {
        image.value = it
    }
    val permissionsState = rememberMultiplePermissionsState(
        permissions = if (Build.VERSION.SDK_INT >= 33) {
            listOf(Manifest.permission.READ_MEDIA_IMAGES)
        } else {
            listOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        },
        onPermissionsResult = {
            if (it[Manifest.permission.READ_EXTERNAL_STORAGE] == true) {
                imagePickActivityLauncher.launch("image/*")
            }
        }
    )

    LaunchedEffect(Unit) {
        nickname.value = globalState.user.value.nickname
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            ProfileImage(
                modifier = Modifier.size(120.dp),
                image = globalState.user.value.image
            )

            if (image.value != null) {
                HorizontalSpacer(width = 24.dp)

                Icon(
                    modifier = Modifier.size(20.dp),
                    imageVector = Icons.Rounded.ArrowForwardIos,
                    contentDescription = "프로필 사진 변화",
                    tint = HeavyGray
                )

                HorizontalSpacer(width = 24.dp)

                ProfileImage(
                    modifier = Modifier.size(120.dp),
                    image = image.value
                )
            }
        }

        VerticalSpacer(12.dp)

        CustomButton(
            modifier = Modifier
                .width(120.dp)
                .height(32.dp),
            shape = RoundedCornerShape(8.dp),
            text = "프로필 사진 수정",
            textStyle = MaterialTheme.typography.button,
            textColor = MaterialTheme.colors.primary,
            backgroundColor = White,
            borderColor = LightGray,
            elevation = 0.dp,
            onClick = {
                when {
                    permissionsState.allPermissionsGranted -> {
                        imagePickActivityLauncher.launch("image/*")
                    }
                    permissionsState.shouldShowRationale -> {
                        permissionsState.launchMultiplePermissionRequest()
                    }
                    else -> {
                        globalState.showSnackBar("프로필 사진 변경을 위해 저장소 권한이 필요합니다. 권한 요청이 자동으로 뜨지 않는다면 설정에서 수동으로 허가해주세요")
                        permissionsState.launchMultiplePermissionRequest()
                    }
                }
            }
        )

        VerticalSpacer(30.dp)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            CustomOutlinedField(
                modifier = Modifier.fillMaxWidth(),
                value = nickname.value,
                onValueChange = { nickname.value = it },
                label = "닉네임을 적어주세요",
                onFocusIn = { },
                onFocusOut = { },
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done,
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                isError = nickname.value.length !in 2..9,
                trailingIcon = {
                    if (nickname.value.isNotBlank()) {
                        IconButton(
                            onClick = { nickname.value = "" }
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Cancel,
                                contentDescription = "싹 지우기"
                            )
                        }
                    }
                }
            )

            VerticalSpacer(height = 2.dp)

            Comment(
                visible = nickname.value.length !in 2..9,
                text = "닉네임은 2 ~ 9자 이내여야하며, 공백/특수문자는 사용불가"
            )
        }

        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.Bottom
        ) {

            PrimaryCtaButton(
                text = "변경사항 저장",
                onClick = {
                    focusManager.clearFocus()
                    if (nickname.value == globalState.user.value.nickname && image.value == null) {
                        globalState.showSnackBar("변경사항이 없습니다")
                    } else {
                        profileViewModel.onEvent(
                            ProfileEvent.UpdateProfile(
                                globalState = globalState,
                                nickname = nickname.value,
                                image = image.value
                            )
                        )
                    }
                }
            )
        }
    }
}