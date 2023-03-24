package com.software.cafejariapp.presentation.feature.main.screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.software.cafejariapp.R
import com.software.cafejariapp.data.source.remote.HttpRoutes
import com.software.cafejariapp.presentation.GlobalState
import com.software.cafejariapp.presentation.component.BaseTopAppBar
import com.software.cafejariapp.presentation.component.CustomAdBanner
import com.software.cafejariapp.presentation.theme.White

@ExperimentalPagerApi
@Composable
fun GuideListScreen(
    globalState: GlobalState
) {

    Scaffold(
        topBar = {
            BaseTopAppBar(
                isBackButtonEnable = true,
                onBackButtonClick = { globalState.navController.popBackStack() },
                title = "유저 가이드북",
                isScrolled = true,
            )
        },
        backgroundColor = White
    ) { paddingValue ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White)
                .padding(paddingValue.calculateTopPadding())
        ) {

            LazyVerticalGrid(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.Top,
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(0.dp),
                userScrollEnabled = false,
                content = {
                    items(GuideCategory.list) { guideCategory ->

                        Card(
                            modifier = Modifier
                                .size(200.dp)
                                .padding(12.dp),
                            backgroundColor = White,
                            shape = MaterialTheme.shapes.small,
                            elevation = 3.dp,
                        ) {

                            Image(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clickable {
                                        globalState.navigateToWebView(
                                            topAppBarTitle = guideCategory.name,
                                            url = guideCategory.url
                                        )
                                    },
                                painter = painterResource(id = guideCategory.mainImage),
                                contentDescription = "가이드 이미지",
                                contentScale = ContentScale.FillBounds
                            )
                        }
                    }
                }
            )
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {

        CustomAdBanner()
    }
}

data class GuideCategory(
    val name: String,
    val mainImage: Int,
    val url: String
) {
    companion object {
        private val masterGuide = GuideCategory(
            "마스터 가이드",
            R.drawable.master_guide_0,
            HttpRoutes.MASTER_GUIDE
        )
        private val crowdedGuide = GuideCategory(
            "카페 혼잡도 활용 가이드",
            R.drawable.cafe_crowded_guide_0,
            HttpRoutes.CROWDED_GUIDE
        )
        private val pointGuide = GuideCategory(
            "포인트 사용 가이드",
            R.drawable.point_use_guide_0,
            HttpRoutes.POINT_GUIDE
        )
        private val cafeRegisterGuide = GuideCategory(
            "카페 등록 요청 가이드",
            R.drawable.cafe_register_request_guide_0,
            HttpRoutes.CAFE_REGISTRATION_GUIDE
        )

        val list = listOf(masterGuide, crowdedGuide, pointGuide, cafeRegisterGuide)
    }
}

