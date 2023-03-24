package com.software.cafejariapp.presentation.feature.main.screen

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.software.cafejariapp.presentation.GlobalState
import com.software.cafejariapp.presentation.component.BaseDivider
import com.software.cafejariapp.presentation.component.BaseTopAppBar
import com.software.cafejariapp.presentation.feature.main.screen.Update.Companion.updateList
import com.software.cafejariapp.presentation.theme.LightGray
import com.software.cafejariapp.presentation.theme.MoreHeavyGray
import com.software.cafejariapp.presentation.theme.White

@ExperimentalPagerApi
@Composable
fun UpdateScreen(
    globalState: GlobalState,
) {

    val selectedPatchVersionOrder = rememberSaveable { mutableStateOf(-1) }

    Scaffold(
        topBar = {
            BaseTopAppBar(
                isBackButtonEnable = true,
                onBackButtonClick = { globalState.navController.popBackStack() },
                title = "업데이트 소식",
                isScrolled = true
            )
        },
        backgroundColor = White
    ) { paddingValue ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(color = White)
                .padding(paddingValue.calculateTopPadding()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            itemsIndexed(updateList) { index, update ->

                Column(
                    modifier = Modifier
                        .clickable {
                            if (selectedPatchVersionOrder.value == index) {
                                selectedPatchVersionOrder.value = -1
                            } else {
                                selectedPatchVersionOrder.value = index
                            }
                        }
                        .padding(
                            horizontal = 20.dp,
                            vertical = 24.dp
                        )
                ) {
                    Text(
                        text = update.date,
                        color = LightGray
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = "[ver ${update.releaseVersionCode}.${update.majorVersionCode}.${update.minorVersionCode}] 업데이트 내용을 알려드려요",
                            style = MaterialTheme.typography.subtitle2
                        )

                        Icon(
                            imageVector = if (selectedPatchVersionOrder.value == index) {
                                Icons.Rounded.ExpandLess
                            } else {
                                Icons.Rounded.ExpandMore
                            },
                            contentDescription = "업데이트 추가 정보",
                            tint = MoreHeavyGray,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    AnimatedVisibility(
                        visible = selectedPatchVersionOrder.value == index,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {

                        Text(
                            text = update.content,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }

                BaseDivider()
            }
        }
    }
}


data class Update(
    val releaseVersionCode: Int,
    val majorVersionCode: Int,
    val minorVersionCode: Int,
    val date: String,
    val content: String,
) {
    companion object {
        val update0 =
            Update(2, 0, 0, "22.12.08", "앱 전체 Ui 리뉴얼, 랭킹기능 추가, 추가 지역 등록(홍대입구역, 이대역, 노량진역)")
        val update1 =
            Update(2, 0, 1, "22.12.09", "일부 버그 수정, 정식 2.0출시, 일부 ui수정, 자동 종료 로그 확인기능추가, 랭킹수정")
        val update2 =
            Update(2, 0, 2, "22.12.10", "마스터활동 종료 후 획득한 포인트가 0으로 나오던 오류 개선, 일부 기기에서 알림못받던 오류 수정")
        val update3 = Update(2, 0, 3, "22.12.16", "최초 맵 진입시 온보딩 화면 출력")
        val update4 = Update(2, 0, 4, "22.12.21", "신규지역 추가 - 혜화, 건대입구")
        val update5 = Update(
            2,
            0,
            5,
            "22.12.25",
            "마스터 활동 / 카페 정보가 제때 업데이트 되지 않아 발생하던 에러 수정, 카페 정보 확인할때 스크롤 프레임드랍 현상 수정, 카페별 이벤트 및 사장님의 한마디 추가"
        )
        val update6 = Update(2, 1, 0, "22.12.26", "문구 수정, 의무 업데이트 추가")
        val update7 = Update(2, 1, 1, "22.12.28", "카페 추가시 정보가 잘못 표기되던 오류 해결")
        val update8 =
            Update(2, 1, 2, "23.01.06", "매장 홍보정보 띄울 때 빈공간이 보이던 오류 해결, 홍보 배너 이미지 짤리는 오류 해결")
        val update9 = Update(2, 1, 3, "23.01.08", "버그 수정 및 카페정보 로딩속도 개선, 광고 로드를 못하던 오류 개선")
        val update10 = Update(
            2,
            2,
            5,
            "23.01.26",
            "UI개선(구글지도 > 네이버지도), 카페 이미지 로드 에러 개선, 영업시간 정보 추가, 콘센트 정보 추가, 화장실 정보 추가, 마커 움직임 개선, 광고로드 에러 개선, 회원탈퇴 추가, 제휴카페 보기 기능 추가, 팝업광고 추가, 이벤트 중인 카페 정리, 애니메이션 개선, 웹뷰 추가, bottom sheet이 잘 열리지 않던 오류 개선"
        )
        val update11 = Update(2, 2, 6, "23.01.28", "팝업순서 조정, 스크린 이동시 위치가 초기화 되던 오류 개선")
        val update12 = Update(2, 2, 7, "23.01.28", "팝업 계속 올라오던 오류 개선, 일부 ui 수정")
        val update13 = Update(2, 2, 8, "23.01.30", "이벤트 카페의 문구잘림 에러 개선")
        val update14 = Update(2, 2, 9, "23.02.01", "신규지역 추가 - 흑석, 층 선택 에러 개선, 위치 제한 개선")
        val update15 = Update(
            2,
            3,
            3,
            "23.02.13",
            "프로필 편집기능 추가, 거리제한 완화, 카페정보 로딩 속도 개선(현 지도에서 검색), 포인트 지급내역 확인 화면 추가, 할인중인 카페 거리순 정렬, 신규지역 추가 - 노량진/안암/신림/서울대입구/왕십리/외대앞/회기, 일부기기 ui 오류개선"
        )
        val update16 = Update(2, 3, 5, "23.02.18", "ui일부 수정, 메인화면 버튼배치 조정, fcm토큰 로직 수정")
        val update17 =
            Update(2, 4, 1, "23.02.23", "추천인 이벤트 추가, 카페 검색기능 추가, 카페정보 제보기능 추가, 자동종료된 마스터활동 광고보기 추가")
        val update18 = Update(2, 4, 2, "23.02.27", "검색 ui 개선")
        val update19 = Update(2, 4, 3, "23.03.02", "추천인 에러 개선, 알림 에러 개선")

        val updateList = listOf(
            update0,
            update1,
            update2,
            update3,
            update4,
            update5,
            update6,
            update7,
            update8,
            update9,
            update10,
            update11,
            update12,
            update13,
            update14,
            update15,
            update16,
            update17,
            update18,
            update19
        ).reversed()
    }
}



