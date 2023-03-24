package com.software.cafejariapp.presentation.feature.map.screen.mapScreen.bottomSheet

import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ExpandLess
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.google.android.libraries.places.api.model.Place
import com.software.cafejariapp.R
import com.software.cafejariapp.core.toFloor
import com.software.cafejariapp.presentation.GlobalState
import com.software.cafejariapp.presentation.component.CustomButton
import com.software.cafejariapp.presentation.component.HorizontalSpacer
import com.software.cafejariapp.presentation.component.VerticalSpacer
import com.software.cafejariapp.presentation.theme.Gray
import com.software.cafejariapp.presentation.theme.HeavyGray
import com.software.cafejariapp.presentation.theme.LightGray
import com.software.cafejariapp.presentation.theme.White
import com.software.cafejariapp.presentation.util.Screen

@Composable
fun CafeInformationPart(
    globalState: GlobalState,
    modalCafePlaceInfo: Place?
) {

    val isOpeningHourOpened = rememberSaveable { mutableStateOf(false) }
    val isWallSocketInfoOpened = rememberSaveable { mutableStateOf(false) }
    val clipboardManager = LocalClipboardManager.current

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {

        val address = "${globalState.modalCafeInfo.value.city} " + "${globalState.modalCafeInfo.value.gu} ${globalState.modalCafeInfo.value.address}"
        
        Icon(
            painter = painterResource(id = R.drawable.content_copy),
            contentDescription = "주소 복사",
            tint = LightGray,
            modifier = Modifier.clickable {
                clipboardManager.setText(AnnotatedString((address)))
                globalState.showSnackBar("주소가 클립보드에 복사되었습니다")
            }
        )
        
        HorizontalSpacer(width = 8.dp)
        
        Text(
            text = address + "  (${globalState.modalCafeInfo.value.firstFloor.toFloor()}층 ~ " + "${globalState.modalCafeInfo.value.getTopFloor()}층)",
            overflow = TextOverflow.Visible,
            maxLines = 1,
            color = Gray
        )
    }

    VerticalSpacer(height = 12.dp)
    
    if (modalCafePlaceInfo?.openingHours?.weekdayText?.isNotEmpty() == true) {
        Row(
            verticalAlignment = Alignment.Top
        ) {
            
            Icon(
                painter = painterResource(id = R.drawable.schedule),
                contentDescription = "영업시간",
                tint = LightGray
            )
            
            HorizontalSpacer(width = 8.dp)
            
            Column {
                
                if (modalCafePlaceInfo.openingHours?.weekdayText != null) {
                    Row(
                        modifier = Modifier.clickable {
                            isOpeningHourOpened.value = !isOpeningHourOpened.value
                        }
                    ) {
                        
                        Text(
                            text = modalCafePlaceInfo.openingHours?.weekdayText!![0],
                            overflow = TextOverflow.Visible,
                            maxLines = 1,
                            color = Gray
                        )
                        
                        HorizontalSpacer(width = 4.dp)
                        
                        Icon(
                            imageVector = if (isOpeningHourOpened.value) {
                                Icons.Rounded.ExpandLess
                            } else {
                                Icons.Rounded.ExpandMore
                            },
                            contentDescription = "영업시간 오픈 여부",
                            tint = Gray,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    
                    AnimatedVisibility(
                        visible = isOpeningHourOpened.value,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        
                        Column {
                            modalCafePlaceInfo.openingHours?.weekdayText?.forEachIndexed { index, weekDayText ->
                                if (index != 0) {
                                    Text(
                                        text = weekDayText,
                                        overflow = TextOverflow.Visible,
                                        maxLines = 1,
                                        color = Gray
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        VerticalSpacer(height = 12.dp)
    }

    if (globalState.modalCafeInfo.value.getWallSocketExistCafes().isNotEmpty()) {
        Row(
            verticalAlignment = Alignment.Top
        ) {
            
            Icon(
                painter = painterResource(id = R.drawable.wall_socket),
                contentDescription = "콘센트",
                tint = LightGray
            )
            
            HorizontalSpacer(width = 8.dp)
            
            Column {
                
                Row(
                    modifier = Modifier.clickable {
                        isWallSocketInfoOpened.value = !isWallSocketInfoOpened.value
                    }
                ) {
                    
                    Text(
                        text = "층별 콘센트 보급율",
                        overflow = TextOverflow.Visible,
                        maxLines = 1,
                        color = Gray
                    )
                    
                    HorizontalSpacer(width = 4.dp)
                    
                    Icon(
                        imageVector = if (isWallSocketInfoOpened.value) {
                            Icons.Rounded.ExpandLess
                        } else {
                            Icons.Rounded.ExpandMore
                        },
                        contentDescription = "콘센트 정보 오픈 여부",
                        tint = Gray,
                        modifier = Modifier.size(20.dp)
                    )
                }
                
                VerticalSpacer(height = 4.dp)
                
                AnimatedVisibility(
                    visible = isWallSocketInfoOpened.value,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    
                    Column {
                        
                        globalState.modalCafeInfo.value.getWallSocketExistCafes().forEach { cafe ->
                            Row {
                                
                                Text(
                                    text = "${cafe.floor.toFloor()}층:",
                                    overflow = TextOverflow.Visible,
                                    maxLines = 1,
                                    color = HeavyGray,
                                    modifier = Modifier.width(36.dp)
                                )

                                Text(
                                    text = cafe.wallSocket,
                                    overflow = TextOverflow.Visible,
                                    maxLines = 1,
                                    color = Gray
                                )
                            }
                        }
                    }
                }
            }
        }
        
        VerticalSpacer(height = 12.dp)
    }

    if (globalState.modalCafeInfo.value.getRestroomExistCafes().isNotEmpty()) {
        Row(
            verticalAlignment = Alignment.Top
        ) {
            
            Icon(
                painter = painterResource(id = R.drawable.restroom),
                contentDescription = "화장실",
                tint = LightGray
            )
            
            HorizontalSpacer(width = 8.dp)
            
            Column {
                
                globalState.modalCafeInfo.value.getRestroomExistCafes().forEach { cafe ->
                    Row {
                        
                        Text(
                            text = "${cafe.floor.toFloor()}층:",
                            overflow = TextOverflow.Visible,
                            maxLines = 1,
                            color = HeavyGray,
                            modifier = Modifier.width(36.dp)
                        )
                        
                        Text(
                            text = cafe.restroom,
                            overflow = TextOverflow.Visible,
                            maxLines = 1,
                            color = Gray
                        )
                    }
                }
            }
        }
        
        VerticalSpacer(height = 12.dp)
    }

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        
        Icon(
            painter = painterResource(id = R.drawable.info),
            contentDescription = "네이버검색정보",
            tint = LightGray,
        )
        
        HorizontalSpacer(width = 8.dp)
        
        Text(
            modifier = Modifier.clickable {
                globalState.navigateToWebView(
                    topAppBarTitle = "카페 상세정보",
                    url = "https://m.search.naver.com/search.naver?sm=mtp_hty.top&where=m&query=${globalState.modalCafeInfo.value.name}"
                )
            }, 
            text = "매장 상세정보", 
            textDecoration = TextDecoration.Underline, 
            color = Gray
        )
    }

    VerticalSpacer(height = 20.dp)

    CustomButton(
        contentPadding = 8.dp,
        shape = RoundedCornerShape(50),
        backgroundColor = White,
        borderColor = MaterialTheme.colors.primary,
        text = " 매장정보 알려주고 100P 받기 ",
        textColor = MaterialTheme.colors.primary,
        textStyle = MaterialTheme.typography.button,
        elevation = 0.dp,
        onClick = { globalState.navController.navigate(Screen.ModifyAdditionalCafeInfoScreen.route) }
    )
}