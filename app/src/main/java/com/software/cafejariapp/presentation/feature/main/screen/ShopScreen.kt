package com.software.cafejariapp.presentation.feature.main.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ListAlt
import androidx.compose.material.icons.rounded.LocalParking
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.skydoves.landscapist.glide.GlideImage
import com.software.cafejariapp.R
import com.software.cafejariapp.core.customPlaceholder
import com.software.cafejariapp.presentation.GlobalState
import com.software.cafejariapp.presentation.util.Screen
import com.software.cafejariapp.domain.entity.Item
import com.software.cafejariapp.presentation.component.*
import com.software.cafejariapp.presentation.feature.main.event.MainEvent
import com.software.cafejariapp.presentation.feature.main.event.ShopEvent
import com.software.cafejariapp.presentation.feature.main.viewModel.ShopViewModel
import com.software.cafejariapp.presentation.theme.HeavyGray
import com.software.cafejariapp.presentation.theme.MoreHeavyGray
import com.software.cafejariapp.presentation.theme.MoreLightGray
import com.software.cafejariapp.presentation.theme.White

@Composable
fun ShopScreen(
    globalState: GlobalState,
    shopViewModel: ShopViewModel,
) {

    val shopState = shopViewModel.state.value
    val selectedCategoryName = rememberSaveable { mutableStateOf("") }
    val selectedItem: MutableState<Item?> = rememberSaveable { mutableStateOf(null) }

    NetworkChecker(globalState)

    LaunchedEffect(Unit) {
        shopViewModel.onEvent(ShopEvent.GetItems(globalState))
    }

    if (selectedItem.value != null) {
        PurchaseDialog(
            selectedItem = selectedItem.value,
            userPoint = globalState.user.value.point,
            onDismiss = { selectedItem.value = null },
            onPurchaseButtonClick = {
                if (globalState.user.value.point < (selectedItem.value?.price ?: 5000)) {
                    globalState.showSnackBar("포인트가 부족합니다")
                } else {
                    shopViewModel.onEvent(ShopEvent.RequestPurchase(
                        globalState = globalState,
                        item = selectedItem.value!!
                    ))
                }
            }
        )
    }

    BackHandler(
        enabled = true,
        onBack = {
            when {
                selectedItem.value != null -> selectedItem.value = null
                else -> globalState.navController.popBackStack()
            }
        }
    )

    Scaffold(
        topBar = {
            BaseTopAppBar(
                isBackButtonEnable = false,
                trailingIconAction = {
                    IconButton(
                        modifier = Modifier.size(24.dp),
                        onClick = { globalState.navController.navigate(Screen.ShoppingBagScreen.route) }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.ListAlt,
                            contentDescription = "구매 이력",
                            tint = MaterialTheme.colors.primary
                        )
                    }
                },
                title = "상점"
            )
        }
    ) { paddingValue ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(White)
                .padding(top = paddingValue.calculateTopPadding()),
        ) {

            CategoryRow(
                selectedCategoryName = selectedCategoryName.value,
                onCategoryClick = { selectedCategoryName.value = it }
            )

            BaseDivider()

            ItemListColumn(
                selectedCategoryName = selectedCategoryName.value,
                items = shopState.items,
                isLoading = shopState.isItemLoading,
                onRefresh = { shopViewModel.onEvent(ShopEvent.GetItems(globalState)) },
                onItemClick = { selectedItem.value = it }
            )
        }
    }
}

@Composable
fun PurchaseDialog(
    selectedItem: Item?,
    userPoint: Int,
    onDismiss: () -> Unit,
    onPurchaseButtonClick: () -> Unit,
) {

    CustomAlertDialog(
        onDismiss = onDismiss,
        positiveButtonText = "구매하기",
        onPositiveButtonClick = onPurchaseButtonClick,
        negativeButtonText = "취소",
        onNegativeButtonClick = { },
        isNegativeButtonEnabled = false,
        content = {

            Text(
                text = "${selectedItem?.brand ?: ""} ${selectedItem?.name ?: ""}",
                style = MaterialTheme.typography.h3,
                color = MaterialTheme.colors.primary
            )

            VerticalSpacer(height = 8.dp)

            Row {

                Text(
                    text = "필요 포인트 : ",
                    color = MaterialTheme.colors.primary
                )

                Text(
                    text = "${selectedItem?.price ?: ""}P",
                    color = MaterialTheme.colors.primary,
                    style = MaterialTheme.typography.body2
                )
            }

            Row {

                Text(
                    text = "보유 포인트 : ",
                    color = MaterialTheme.colors.primary
                )

                Text(
                    text = "${userPoint}P",
                    color = MaterialTheme.colors.primary,
                    style = MaterialTheme.typography.body2
                )
            }

            VerticalSpacer(height = 8.dp)

            Text(
                text = "'구매하기' 버튼을 누르시면 포인트 확인 후",
                color = HeavyGray
            )

            Text(
                text = "기프티콘을 발송해드려요",
                color = HeavyGray
            )
        }
    )
}

@Composable
fun CategoryRow(
    selectedCategoryName: String,
    onCategoryClick: (String) -> Unit
) {

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        item {

            HorizontalSpacer(width = 20.dp)

            Button(
                onClick = { onCategoryClick("") },
                shape = RoundedCornerShape(50),
                elevation = ButtonDefaults.elevation(
                    defaultElevation = 1.dp,
                    pressedElevation = 0.dp
                ),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = if (selectedCategoryName.isBlank()) {
                        MaterialTheme.colors.primary
                    } else {
                        MoreLightGray
                    }
                ),
                contentPadding = PaddingValues(
                    vertical = 8.dp,
                    horizontal = 12.dp
                )
            ) {

                Text(
                    text = "전체보기",
                    color = if (selectedCategoryName.isBlank()) {
                        White
                    } else {
                        MoreHeavyGray
                    },
                    style = MaterialTheme.typography.body2
                )
            }

            HorizontalSpacer(width = 8.dp)
        }

        items(ItemCategory.itemCategoryList) { itemCategory ->

            Button(
                onClick = { onCategoryClick(itemCategory.name) },
                shape = RoundedCornerShape(50),
                elevation = ButtonDefaults.elevation(
                    defaultElevation = 1.dp,
                    pressedElevation = 0.dp
                ),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = if (selectedCategoryName == itemCategory.name) {
                        MaterialTheme.colors.primary
                    } else {
                        MoreLightGray
                    }
                ),
                contentPadding = PaddingValues(
                    vertical = 8.dp,
                    horizontal = 12.dp
                )
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {

                    Icon(
                        painter = painterResource(id = itemCategory.painterResourceId),
                        contentDescription = "카테고리 아이콘",
                        tint = if (selectedCategoryName == itemCategory.name) {
                            White
                        } else {
                            MoreHeavyGray
                        }
                    )

                    HorizontalSpacer(width = 5.dp)

                    Text(
                        text = itemCategory.name,
                        style = MaterialTheme.typography.body2,
                        textAlign = TextAlign.Center,
                        color = if (selectedCategoryName == itemCategory.name) {
                            White
                        } else {
                            MoreHeavyGray
                        }
                    )
                }
            }

            HorizontalSpacer(width = 8.dp)
        }

        item {

            HorizontalSpacer(width = 20.dp)
        }
    }
}

@Composable
fun ItemListColumn(
    selectedCategoryName: String,
    items: List<Item>,
    isLoading: Boolean,
    onRefresh: () -> Unit,
    onItemClick: (Item) -> Unit
) {

    when (selectedCategoryName) {
        "" -> {
            if (items.isEmpty()) {
                EmptyScreen("아직 올라온 상품이 없어요")
            }
        }
        else -> {
            if (items.none { it.category == selectedCategoryName }) {
                EmptyScreen("아직 올라온 상품이 없어요")
            }
        }
    }

    CustomSwipeRefresh(
        isLoading = isLoading,
        onRefresh = onRefresh
    ) {

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {

            items(
                when (selectedCategoryName) {
                    "" -> items
                    else -> items.filter { it.category == selectedCategoryName }
                }
            ) { item ->

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = White)
                        .height(120.dp)
                        .clickable { onItemClick(item) }
                        .padding(
                            top = 0.dp,
                            start = 4.dp,
                            end = 16.dp,
                            bottom = 0.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {

                    Card(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(vertical = 16.dp)
                            .weight(2f),
                        elevation = 0.dp,
                        backgroundColor = White
                    ) {

                        GlideImage(
                            modifier = Modifier
                                .fillMaxHeight()
                                .customPlaceholder(isLoading),
                            imageModel = item.image,
                            contentScale = ContentScale.FillHeight,
                            placeHolder = painterResource(id = R.drawable.glide_image_placeholder)
                        )
                    }

                    Column(
                        modifier = Modifier
                            .weight(3f)
                            .padding(12.dp),
                        verticalArrangement = Arrangement.Center,
                    ) {

                        Text(
                            modifier = Modifier.customPlaceholder(isLoading),
                            text = item.name,
                            style = MaterialTheme.typography.subtitle2,
                        )

                        VerticalSpacer(height = 4.dp)

                        Text(
                            modifier = Modifier.customPlaceholder(isLoading),
                            text = "( ${item.brand} )",
                            color = HeavyGray
                        )
                    }

                    Row(
                        modifier = Modifier
                            .weight(2f)
                            .padding(12.dp)
                            .customPlaceholder(isLoading),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End
                    ) {

                        Card(
                            backgroundColor = White,
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(
                                width = 2.dp,
                                color = MaterialTheme.colors.secondary
                            ),
                            modifier = Modifier.size(16.dp),
                            elevation = 0.dp
                        ) {

                            Icon(
                                modifier = Modifier.padding(2.dp),
                                imageVector = Icons.Rounded.LocalParking,
                                contentDescription = "가격 표시",
                                tint = MaterialTheme.colors.secondary
                            )
                        }

                        HorizontalSpacer(width = 12.dp)

                        Text(
                            text = item.price.toString(),
                            style = MaterialTheme.typography.body1,
                        )
                    }
                }

                BaseDivider()
            }

            item {

                VerticalSpacer(height = 120.dp)
            }
        }
    }
}


data class ItemCategory(
    val name: String,
    val painterResourceId: Int
){
    companion object{
        val itemCategoryList = listOf(
            ItemCategory("커피", R.drawable.coffee_icon),
            ItemCategory("케이크", R.drawable.cake_icon),
            ItemCategory("금액권", R.drawable.payments_icon),
            ItemCategory("아이스크림", R.drawable.icecream_icon),
            ItemCategory("디저트", R.drawable.bakery_dining_icon),
            ItemCategory("논커피", R.drawable.local_bar_icon),
        )
    }
}