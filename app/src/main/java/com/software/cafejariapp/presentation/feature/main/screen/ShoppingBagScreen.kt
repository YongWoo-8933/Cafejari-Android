package com.software.cafejariapp.presentation.feature.main.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.skydoves.landscapist.glide.GlideImage
import com.software.cafejariapp.R
import com.software.cafejariapp.presentation.GlobalState
import com.software.cafejariapp.domain.entity.PurchaseHistory
import com.software.cafejariapp.presentation.component.*
import com.software.cafejariapp.presentation.feature.main.event.ShopEvent
import com.software.cafejariapp.presentation.feature.main.viewModel.ShopViewModel
import com.software.cafejariapp.presentation.theme.*

@Composable
fun ShoppingBagScreen(
    globalState: GlobalState,
    shopViewModel: ShopViewModel,
) {

    val shopState = shopViewModel.state.value
    val selectedHistory: MutableState<PurchaseHistory?> = rememberSaveable { mutableStateOf(null) }

    NetworkChecker(globalState)

    LaunchedEffect(Unit) {
        shopViewModel.onEvent(ShopEvent.GetPurchaseHistories(globalState))
    }

    if (selectedHistory.value != null) {
        DeletePurchaseHistoryDialog(
            onDismiss = { selectedHistory.value = null },
            onDeleteButtonClick = {
                shopViewModel.onEvent(
                    ShopEvent.DeletePurchaseHistory(
                        globalState = globalState,
                        history = selectedHistory.value!!
                    )
                )
            },
            selectedHistory = selectedHistory.value
        )
    }

    BackHandler(
        enabled = true,
        onBack = {
            when {
                selectedHistory.value != null -> selectedHistory.value = null
                else -> globalState.navController.popBackStack()
            }
        }
    )

    Scaffold(
        topBar = {
            BaseTopAppBar(
                isBackButtonEnable = true,
                onBackButtonClick = { globalState.navController.popBackStack() },
                title = "구매 이력"
            )
        }
    ) { paddingValue ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(White)
                .padding(top = paddingValue.calculateTopPadding()),
        ) {

            if (shopState.isPurchaseHistoryLoading) {
                FullSizeLoadingScreen()
            } else {
                if (shopState.purchaseHistories.isEmpty()) {
                    EmptyScreen("구매 이력이 없어요")
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {

                        items(shopState.purchaseHistories) { purchaseHistory ->

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(color = White)
                                    .height(120.dp)
                                    .clickable {
                                        if (purchaseHistory.state != PurchaseRequestStateType.progress) {
                                            selectedHistory.value = purchaseHistory
                                        } else {
                                            globalState.showSnackBar("처리중인 내역은 취소/삭제가 불가합니다")
                                        }
                                    }
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
                                        .padding(
                                            top = 16.dp,
                                            bottom = 16.dp
                                        )
                                        .weight(2f),
                                    elevation = 0.dp,
                                    backgroundColor = White
                                ) {

                                    GlideImage(
                                        modifier = Modifier.fillMaxHeight(),
                                        imageModel = purchaseHistory.item_image,
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
                                        text = purchaseHistory.item_name,
                                        style = MaterialTheme.typography.subtitle2,
                                    )

                                    VerticalSpacer(height = 4.dp)

                                    Text(
                                        text = "( ${purchaseHistory.item_brand} )",
                                        color = HeavyGray
                                    )
                                }

                                Row(
                                    modifier = Modifier
                                        .weight(2f)
                                        .fillMaxHeight()
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Start
                                ) {

                                    Icon(
                                        imageVector = when (purchaseHistory.state) {
                                            PurchaseRequestStateType.request -> Icons.Rounded.Send
                                            PurchaseRequestStateType.complete -> Icons.Rounded.CheckCircle
                                            PurchaseRequestStateType.reject -> Icons.Rounded.Cancel
                                            else -> Icons.Rounded.Pending
                                        },
                                        contentDescription = "구매 이력 확인",
                                        tint = when (purchaseHistory.state) {
                                            PurchaseRequestStateType.request -> CrowdedGray
                                            PurchaseRequestStateType.complete -> CrowdedGreen
                                            PurchaseRequestStateType.reject -> CrowdedRed
                                            else -> HeavyGray
                                        }
                                    )

                                    HorizontalSpacer(width = 12.dp)

                                    Text(
                                        text = when (purchaseHistory.state) {
                                            PurchaseRequestStateType.request -> "요청됨"
                                            PurchaseRequestStateType.complete -> "지급완료"
                                            PurchaseRequestStateType.reject -> "거절"
                                            else -> "처리중"
                                        }
                                    )
                                }
                            }

                            BaseDivider()
                        }

                        item {
                            VerticalSpacer(height = 60.dp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DeletePurchaseHistoryDialog(
    onDismiss: () -> Unit,
    onDeleteButtonClick: () -> Unit,
    selectedHistory: PurchaseHistory?
) {

    CustomAlertDialog(
        onDismiss = onDismiss,
        positiveButtonText = "삭제",
        onPositiveButtonClick = onDeleteButtonClick,
        negativeButtonText = "취소",
        onNegativeButtonClick = { },
        content = {
            when (selectedHistory?.state) {
                PurchaseRequestStateType.complete -> {
                    Text(
                        text = "구매 완료 상품을 내역에서",
                        style = MaterialTheme.typography.subtitle1
                    )

                    Text(
                        text = "삭제하시겠습니까?",
                        style = MaterialTheme.typography.subtitle1
                    )
                }
                PurchaseRequestStateType.reject -> {
                    Text(
                        text = "요청 거절된 상품을 내역에서",
                        style = MaterialTheme.typography.subtitle1
                    )

                    Text(
                        text = "삭제하시겠습니까?",
                        style = MaterialTheme.typography.subtitle1
                    )
                }
                else -> {
                    Text(
                        text = "내역을 삭제하면 해당 요청이 취소됩니다",
                        style = MaterialTheme.typography.subtitle1
                    )
                    Text(
                        text = "삭제하시겠습니까?",
                        style = MaterialTheme.typography.subtitle1
                    )
                }
            }
        }
    )
}

sealed class PurchaseRequestStateType {
    companion object {
        val complete = 2
        val progress = 1
        val request = 0
        val reject = -1
    }
}