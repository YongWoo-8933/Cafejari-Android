package com.software.cafejariapp.presentation.feature.map.screen.masterRoom

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.software.cafejariapp.R
import com.software.cafejariapp.core.isNearBy
import com.software.cafejariapp.presentation.GlobalState
import com.software.cafejariapp.presentation.component.ClockCard
import com.software.cafejariapp.presentation.component.HorizontalSpacer
import com.software.cafejariapp.presentation.component.PrimaryCtaButton
import com.software.cafejariapp.presentation.component.VerticalSpacer
import com.software.cafejariapp.core.toCrowded
import com.software.cafejariapp.core.toFloor
import com.software.cafejariapp.presentation.util.Time
import com.software.cafejariapp.presentation.theme.Gray
import com.software.cafejariapp.presentation.theme.HeavyGray
import com.software.cafejariapp.presentation.theme.MoreLightGray

@Composable
fun ActivatedMasterRoom(
    globalState: GlobalState,
    onCrowdedUpdateButtonClick: (Int) -> Unit,
    onCrowdedHistoryClick: (Int) -> Unit,
    onExpireMasterTextClick: () -> Unit
) {

    val selectedCrowdedIndex = rememberSaveable { mutableStateOf(1) }

    LaunchedEffect(globalState.masterCafeLog.value) {
        if (globalState.masterCafeLog.value.cafeDetailLogs.isNotEmpty()) {
            selectedCrowdedIndex.value =
                globalState.masterCafeLog.value.cafeDetailLogs.first().crowded
        }
    }

    LazyColumn {

        item {

            Column(
                modifier = Modifier.padding(20.dp)
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                        .background(
                            color = MoreLightGray,
                            shape = MaterialTheme.shapes.medium
                        )
                        .padding(horizontal = 20.dp)
                        .height(96.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(
                        modifier = Modifier.fillMaxHeight(),
                        verticalArrangement = Arrangement.Bottom
                    ) {

                        Image(
                            painter = painterResource(id = R.drawable.master_activated_person_image),
                            contentDescription = "마스터사람이미지",
                            modifier = Modifier.height(84.dp)
                        )
                    }

                    HorizontalSpacer(width = 20.dp)

                    Column(
                        modifier = Modifier.fillMaxHeight(),
                        verticalArrangement = Arrangement.Center
                    ) {

                        Text(
                            text = "${globalState.masterCafeLog.value.master.nickname}님은 현재",
                            color = MaterialTheme.colors.primary
                        )

                        Text(
                            text = "${globalState.masterCafeLog.value.floor.toFloor()}층의 마스터 입니다",
                            style = MaterialTheme.typography.subtitle2,
                            color = MaterialTheme.colors.primary
                        )
                    }
                }

                VerticalSpacer(height = 20.dp)

                Text(
                    text = "혼잡도 설정",
                    style = MaterialTheme.typography.subtitle2
                )

                VerticalSpacer(height = 4.dp)

                Row {

                    Text(
                        text = if (globalState.masterCafeLog.value.cafeDetailLogs.isNotEmpty()) {
                            val minute = Time.getMinuteFrom(globalState.masterCafeLog.value.cafeDetailLogs.first().update)
                            if (globalState.masterCafeLog.value.updatePeriod - minute > 3) {
                                (globalState.masterCafeLog.value.updatePeriod - minute).toString() + "분"
                            } else {
                                "잠시"
                            }
                        } else "잠시",
                        style = MaterialTheme.typography.button,
                        color = HeavyGray
                    )

                    Text(
                        text = " 후, 혼잡도 공유활동 자동종료",
                        style = MaterialTheme.typography.caption,
                        color = Gray
                    )
                }

                VerticalSpacer(height = 20.dp)

                CrowdedEditor(
                    selectedCrowdedIndex = selectedCrowdedIndex.value,
                    onCrowdedChange = { selectedCrowdedIndex.value = it }
                )

                VerticalSpacer(height = 20.dp)

                PrimaryCtaButton(
                    text = "혼잡도 업데이트",
                    onClick = {
                        when {
                            globalState.userLocation.value == null -> {
                                globalState.showSnackBar("위치 정보가 없습니다")
                            }
                            !globalState.userLocation.value!!.isNearBy(
                                globalState.masterCafeLog.value.latitude,
                                globalState.masterCafeLog.value.longitude
                            ) -> globalState.showSnackBar("카페와 거리가 너무 멉니다. 현재 위치를 확인해주세요")
                            else -> onCrowdedUpdateButtonClick(selectedCrowdedIndex.value)
                        }
                    }
                )

                VerticalSpacer(height = 20.dp)
            }
        }

        item {

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
                    .background(color = MaterialTheme.colors.background)
            )
        }

        item {

            Column(
                modifier = Modifier.padding(
                    vertical = 40.dp,
                    horizontal = 20.dp
                )
            ) {

                Text(
                    text = "혼잡도 변경 기록",
                    style = MaterialTheme.typography.subtitle2
                )

                VerticalSpacer(height = 4.dp)

                Text(
                    text = "* 기록을 터치하면 삭제 가능",
                    style = MaterialTheme.typography.caption,
                    color = HeavyGray
                )

                VerticalSpacer(height = 20.dp)

                LazyRow {

                    items(globalState.masterCafeLog.value.cafeDetailLogs) { cafeDetailLog ->

                        ClockCard(
                            timeString = cafeDetailLog.update,
                            crowded = cafeDetailLog.crowded.toCrowded(),
                            onClick = { onCrowdedHistoryClick(cafeDetailLog.id) }
                        )

                        HorizontalSpacer(width = 8.dp)
                    }
                }

                VerticalSpacer(height = 60.dp)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {

                    Text(
                        modifier = Modifier.clickable { onExpireMasterTextClick() },
                        text = "혼잡도 공유활동을 종료하시겠어요?",
                        textDecoration = TextDecoration.Underline,
                        style = MaterialTheme.typography.subtitle1,
                        color = HeavyGray
                    )
                }

                VerticalSpacer(height = 40.dp)
            }
        }
    }
}