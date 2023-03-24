package com.software.cafejariapp.presentation.feature.map.screen.mapScreen.bottomSheet

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreHoriz
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.software.cafejariapp.presentation.component.HorizontalSpacer
import com.software.cafejariapp.presentation.feature.map.event.MapEvent
import com.software.cafejariapp.presentation.feature.map.viewModel.MapViewModel
import com.software.cafejariapp.presentation.theme.HalfTransparentBlack
import com.software.cafejariapp.presentation.theme.HeavyGray
import com.software.cafejariapp.presentation.theme.White

@Composable
fun CafeImagePart(
    mapViewModel: MapViewModel,
    onImageClick: (Bitmap) -> Unit
) {

    val mapState = mapViewModel.state.value

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = White)
            .height(
                if (mapState.modalCafeBitmaps != null && mapState.modalCafeBitmaps.isEmpty()) {
                    0.dp
                } else {
                    200.dp
                }
            ),
    ) {

        when {
            mapState.modalCafeBitmaps == null -> {
                item {

                    Column(
                        modifier = Modifier.fillMaxHeight(),
                        verticalArrangement = Arrangement.Center
                    ) {

                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(80.dp)
                                .padding(20.dp),
                            color = HeavyGray
                        )
                    }
                }
            }
            mapState.modalCafeBitmaps.isEmpty() -> {

            }
            mapState.modalCafeMoreBitmaps == null -> {
                items(mapState.modalCafeBitmaps) { bitmap ->
                    Card(
                        shape = MaterialTheme.shapes.medium
                    ) {

                        Image(
                            modifier = Modifier.clickable { onImageClick(bitmap) },
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = "카페 이미지",
                            contentScale = ContentScale.FillHeight
                        )
                    }

                    HorizontalSpacer(width = 4.dp)
                }
            }
            mapState.modalCafeMoreBitmaps.isEmpty() -> {
                itemsIndexed(mapState.modalCafeBitmaps) { index, bitmap ->

                    if (index != mapState.modalCafeBitmaps.lastIndex) {
                        Card(shape = MaterialTheme.shapes.medium) {

                            Image(
                                modifier = Modifier.clickable { onImageClick(bitmap) },
                                bitmap = bitmap.asImageBitmap(),
                                contentDescription = "카페 이미지",
                                contentScale = ContentScale.FillHeight
                            )
                        }

                        HorizontalSpacer(width = 4.dp)
                    } else {
                        if (mapState.isImageLoading) {
                            Column(
                                modifier = Modifier.fillMaxHeight(),
                                verticalArrangement = Arrangement.Center
                            ) {

                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .size(80.dp)
                                        .padding(20.dp),
                                    color = HeavyGray
                                )
                            }
                        } else {
                            Box(
                                modifier = Modifier.width(80.dp),
                                contentAlignment = Alignment.Center
                            ) {

                                Card(
                                    modifier = Modifier.fillMaxHeight(),
                                    shape = MaterialTheme.shapes.medium
                                ) {

                                    Image(
                                        modifier = Modifier.fillMaxHeight(),
                                        contentScale = ContentScale.Crop,
                                        bitmap = bitmap.asImageBitmap(),
                                        contentDescription = "카페 이미지"
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(
                                            color = HalfTransparentBlack,
                                            shape = MaterialTheme.shapes.medium
                                        )
                                        .clickable {
                                            mapViewModel.onEvent(
                                                MapEvent.FetchMoreImages(
                                                    photoMetadatas = mapState.modalPhotoMetadatas!!
                                                )
                                            )
                                        }
                                )

                                Column(
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.MoreHoriz,
                                        contentDescription = "더보기",
                                        tint = White
                                    )
                                    Text(
                                        text = "더보기", color = White
                                    )
                                }
                            }
                        }

                        HorizontalSpacer(width = 4.dp)
                    }
                }
            }
            else -> {
                items(mapState.modalCafeBitmaps) { bitmap ->

                    Card(
                        shape = MaterialTheme.shapes.medium
                    ) {

                        Image(
                            modifier = Modifier.clickable { onImageClick(bitmap) },
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = "카페 이미지"
                        )
                    }

                    HorizontalSpacer(width = 4.dp)
                }
                items(mapState.modalCafeMoreBitmaps) { bitmap ->

                    Card(
                        shape = MaterialTheme.shapes.medium
                    ) {

                        Image(
                            modifier = Modifier.clickable { onImageClick(bitmap) },
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = "카페 이미지"
                        )
                    }

                    HorizontalSpacer(width = 4.dp)
                }
            }
        }
    }
}