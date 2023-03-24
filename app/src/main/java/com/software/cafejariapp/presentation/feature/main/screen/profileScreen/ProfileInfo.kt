package com.software.cafejariapp.presentation.feature.main.screen.profileScreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.NavigateNext
import androidx.compose.material.icons.rounded.ThumbUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.skydoves.landscapist.glide.GlideImage
import com.software.cafejariapp.R
import com.software.cafejariapp.domain.entity.Event
import com.software.cafejariapp.domain.entity.User
import com.software.cafejariapp.presentation.component.HorizontalSpacer
import com.software.cafejariapp.presentation.component.VerticalSpacer
import com.software.cafejariapp.presentation.feature.main.component.ProfileImage
import com.software.cafejariapp.presentation.theme.Gray
import com.software.cafejariapp.presentation.theme.HalfTransparentBlack
import com.software.cafejariapp.presentation.theme.White

@Composable
fun ProfileInfo(
    user: User,
    onProfileImageClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = White)
            .padding(
                horizontal = 15.dp,
                vertical = 25.dp
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Box {

            ProfileImage(
                modifier = Modifier
                    .size(80.dp)
                    .clickable { onProfileImageClick() },
                image = user.image
            )

            Row(
                modifier = Modifier.matchParentSize(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.End
            ) {

                Card(
                    shape = RoundedCornerShape(50),
                    backgroundColor = Color.White,
                    border = BorderStroke(1.5.dp, MaterialTheme.colors.primary)
                ) {

                    IconButton(
                        modifier = Modifier.size(25.dp),
                        onClick = onProfileImageClick
                    ) {

                        Icon(
                            modifier = Modifier.size(16.dp),
                            imageVector = Icons.Rounded.Edit,
                            contentDescription = "프로필 편집",
                            tint = MaterialTheme.colors.primary,
                        )
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Column {

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        modifier = Modifier.padding(bottom = 2.dp),
                        text = user.nickname,
                        style = MaterialTheme.typography.h2
                    )

                    HorizontalSpacer(width = 8.dp)

                    Row(
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colors.primary,
                                shape = RoundedCornerShape(40)
                            )
                            .padding(
                                horizontal = 12.dp,
                                vertical = 4.dp
                            ),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {

                        Icon(
                            modifier = Modifier.size(16.dp),
                            imageVector = Icons.Rounded.ThumbUp,
                            contentDescription = "좋아요 수",
                            tint = White
                        )

                        HorizontalSpacer(width = 4.dp)

                        Text(
                            text = user.grade.toString(),
                            style = MaterialTheme.typography.button,
                            color = White,
                            letterSpacing = (-1).sp
                        )
                    }
                }
                
                VerticalSpacer(height = 4.dp)

                Text(
                    text = user.email,
                    color = Gray
                )
            }
        }
    }
}