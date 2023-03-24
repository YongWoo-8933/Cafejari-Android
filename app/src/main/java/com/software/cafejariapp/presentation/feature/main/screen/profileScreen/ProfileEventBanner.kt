package com.software.cafejariapp.presentation.feature.main.screen.profileScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.skydoves.landscapist.glide.GlideImage
import com.software.cafejariapp.R
import com.software.cafejariapp.domain.entity.Event
import com.software.cafejariapp.presentation.component.HorizontalSpacer
import com.software.cafejariapp.presentation.theme.HalfTransparentBlack
import com.software.cafejariapp.presentation.theme.White

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ProfileEventBanner(
    pagerState: PagerState,
    events: List<Event>,
    onEventClick: (Event) -> Unit
) {

    val bannerHeight = 142.dp

    Box {

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .background(White)
                .height(bannerHeight)
                .padding(vertical = 15.dp),
            itemSpacing = 5.dp,
            dragEnabled = true,
            verticalAlignment = Alignment.CenterVertically,
            horizontalAlignment = Alignment.CenterHorizontally
        ) { pageIndex ->

            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp),
                shape = MaterialTheme.shapes.medium,
                backgroundColor = White
            ) {

                if (events.isNotEmpty()) {
                    val event = try {
                        events[pageIndex]
                    } catch (thr: Throwable) {
                        null
                    }

                    if (event != null) {
                        GlideImage(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onEventClick(event) },
                            imageModel = event.image,
                            contentScale = ContentScale.Crop,
                            placeHolder = painterResource(id = R.drawable.glide_image_placeholder)
                        )
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .height(bannerHeight)
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            contentAlignment = Alignment.BottomCenter
        ) {

            Row {

                events.forEachIndexed { index, _ ->
                    if (pagerState.currentPage == index) {
                        Card(
                            backgroundColor = White,
                            elevation = 1.dp,
                            modifier = Modifier.size(8.dp),
                            shape = RoundedCornerShape(50)
                        ) {}
                    } else {
                        Card(
                            backgroundColor = HalfTransparentBlack,
                            elevation = 0.dp,
                            modifier = Modifier.size(8.dp),
                            shape = RoundedCornerShape(50)
                        ) {}
                    }

                    if (index != events.lastIndex) {
                        HorizontalSpacer(width = 4.dp)
                    }
                }
            }
        }
    }
}