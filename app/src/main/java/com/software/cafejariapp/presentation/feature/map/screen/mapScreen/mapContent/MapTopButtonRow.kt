package com.software.cafejariapp.presentation.feature.map.screen.mapScreen.mapContent

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ExpandLess
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.naver.maps.map.CameraUpdate
import com.software.cafejariapp.R
import com.software.cafejariapp.domain.entity.Event
import com.software.cafejariapp.presentation.component.HorizontalSpacer
import com.software.cafejariapp.presentation.component.VerticalSpacer
import com.software.cafejariapp.presentation.feature.map.event.MapEvent
import com.software.cafejariapp.presentation.feature.map.util.Locations
import com.software.cafejariapp.presentation.theme.*
import com.software.cafejariapp.presentation.util.Screen
import kotlinx.coroutines.launch

@Composable
fun MapTopButtonRow(
    isMenuOpened: Boolean,

    onSearchButtonClick: () -> Unit,
    onOnSaleCafeButtonClick: () -> Unit,
    onRegisterCafeButtonClick: () -> Unit,
    onMenuButtonClick: () -> Unit,
    onMenuItemButtonClick: (Locations) -> Unit,
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Transparent),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Row {

            Button(
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = White
                ),
                onClick = onSearchButtonClick,
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier.size(36.dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = "검색화면으로이동",
                    modifier = Modifier.size(18.dp),
                    tint = MaterialTheme.colors.primary
                )
            }

            HorizontalSpacer(width = 8.dp)

            Button(
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = White
                ),
                onClick = onOnSaleCafeButtonClick,
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier.height(36.dp),
                contentPadding = PaddingValues(start = 10.dp, end = 12.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.discount_tag),
                    contentDescription = "할인아이콘",
                    modifier = Modifier.height(18.dp)
                )
                HorizontalSpacer(width = 6.dp)
                Text(
                    text = "할인중", style = MaterialTheme.typography.body1
                )
            }

            HorizontalSpacer(width = 8.dp)

            Button(
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = White
                ),
                onClick = onRegisterCafeButtonClick,
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier.height(36.dp),
                contentPadding = PaddingValues(start = 8.dp, end = 12.dp),
            ) {
                Image(
                    painter = painterResource(id = R.drawable.stamp_icon),
                    contentDescription = "카페추가 앞아이콘",
                    modifier = Modifier.size(18.dp)
                )
                HorizontalSpacer(width = 6.dp)
                Text(
                    text = "카페추가",
                    style = MaterialTheme.typography.body1,
                )
            }
        }

        Button(
            colors = ButtonDefaults.buttonColors(
                backgroundColor = White,
            ),
            onClick = onMenuButtonClick,
            shape = MaterialTheme.shapes.medium,
            border = BorderStroke(
                width = 1.dp, color = if (isMenuOpened) HeavyGray else Transparent
            ),
            modifier = Modifier.height(36.dp),
            contentPadding = PaddingValues(start = 12.dp, end = 8.dp)
        ) {
            Text(
                text = "지역", style = MaterialTheme.typography.body1
            )
            HorizontalSpacer(width = 6.dp)
            Icon(
                imageVector = if (isMenuOpened) Icons.Rounded.ExpandLess else Icons.Rounded.ExpandMore,
                contentDescription = "메뉴오픈여부",
                tint = MoreHeavyGray,
                modifier = Modifier.size(20.dp)
            )
        }
    }

    VerticalSpacer(height = 8.dp)

    Box(
        modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopCenter
    ) {

        val menuItemHeight = 48.dp
        val menuWidth = 160.dp

        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End
        ) {
            AnimatedVisibility(
                visible = isMenuOpened,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Card(
                    modifier = Modifier,
                    elevation = 2.dp,
                    shape = RoundedCornerShape(12.dp),
                    backgroundColor = Transparent
                ) {
                    Column {
                        LazyColumn(
                            modifier = Modifier.width(menuWidth)
                                .height(menuItemHeight * 6 + menuItemHeight / 2),
                            verticalArrangement = Arrangement.Top,
                            contentPadding = PaddingValues(0.dp),
                            userScrollEnabled = true
                        ) {
                            items(Locations.locationList) { location ->
                                MenuItem(width = menuWidth,
                                    height = menuItemHeight,
                                    text = location.name,
                                    onClick = { onMenuItemButtonClick(location) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}



@Composable
private fun MenuItem(
    modifier: Modifier = Modifier,
    width: Dp,
    height: Dp,
    text: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .width(width)
            .height(height)
            .background(color = White)
            .clickable { onClick() }
            .border(width = (0.2).dp, color = MoreLightGray)
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Image(
            painter = painterResource(id = R.drawable.gray_coffee_bean_marker),
            contentDescription = "메뉴아이콘",
            modifier = Modifier.size(20.dp)
        )
        Text(
            text = text,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}