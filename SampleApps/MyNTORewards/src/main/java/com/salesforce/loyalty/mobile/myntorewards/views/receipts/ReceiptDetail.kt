package com.salesforce.loyalty.mobile.myntorewards.views.receipts

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetState
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.gson.Gson
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.receiptscanning.models.AnalyzeExpenseResponse
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.*
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.TAB_ELIGIBLE_ITEM
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.TAB_ORIGINAL_RECEIPT_IMAGE
import com.salesforce.loyalty.mobile.myntorewards.utilities.Common.Companion.formatReceiptDetailDate
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.ReceiptListScreenPopupState
import com.salesforce.loyalty.mobile.myntorewards.views.navigation.ReceiptTabs
import com.salesforce.loyalty.mobile.sources.forceUtils.Logger
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun ReceiptDetail(navController: NavHostController) {

    var openBottomsheet by remember { mutableStateOf(false) }
    var openReceiptDetail by remember { mutableStateOf(ReceiptListScreenPopupState.RECEIPT_LIST_SCREEN) }
    var blurBG by remember { mutableStateOf(0.dp) }

    val processedAWSReponse =
        navController.previousBackStackEntry?.arguments?.getString(AppConstants.KEY_PROCESSED_AWS_RESPONSE)
    var analyzeExpenseResponse: AnalyzeExpenseResponse? = null
    processedAWSReponse?.let {
        val gson = Gson()
        analyzeExpenseResponse = gson.fromJson<AnalyzeExpenseResponse>(
            it,
            AnalyzeExpenseResponse::class.java
        )
    }
    Logger.d("ReceiptListArgs","analyzeExpenseResponse 1 : ${analyzeExpenseResponse}")
    val bottomSheetScaffoldState = androidx.compose.material.rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(initialValue = BottomSheetValue.Collapsed,
            confirmValueChange = {
                // Prevent collapsing by swipe down gesture
                it != BottomSheetValue.Collapsed
            })
    )
    val coroutineScope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current
    val openBottomSheet = {
        coroutineScope.launch {
            if (bottomSheetScaffoldState.bottomSheetState.isCollapsed) {
                bottomSheetScaffoldState.bottomSheetState.expand()
            }
        }
    }

    if (openBottomsheet) {
        openBottomSheet()
    }
    val closeBottomSheet = {
        //showBottomBar(true)
        keyboardController?.hide()
        blurBG = AppConstants.NO_BLUR_BG
        coroutineScope.launch {
            if (bottomSheetScaffoldState.bottomSheetState.isExpanded) {
                bottomSheetScaffoldState.bottomSheetState.collapse()
            }
        }
    }
    androidx.compose.material.BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,

        sheetContent = {
            Spacer(modifier = Modifier.height(1.dp))
                ManualReview(closePopup = {
                    openBottomsheet = false
                    closeBottomSheet()
                    openReceiptDetail = it

                })
        },
        sheetShape = RoundedCornerShape(AppConstants.POPUP_ROUNDED_CORNER_SIZE, AppConstants.POPUP_ROUNDED_CORNER_SIZE, 0.dp, 0.dp),
        sheetPeekHeight = 0.dp,
        sheetGesturesEnabled = false
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .blur(blurBG),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            var selectedTab by remember { mutableStateOf(0) }
            Column(
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .weight(0.45f)
                /*.verticalScroll(
                    rememberScrollState()
                )*/,
                horizontalAlignment = Alignment.Start
            ) {
                Spacer(modifier = Modifier.height(15.dp))
                Image(
                    painter = painterResource(id = R.drawable.back_arrow),
                    contentDescription = stringResource(id = R.string.cd_receipt_back_button),
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .padding(top = 16.dp, start = 8.dp)
                        .clickable {
                            navController.popBackStack()
                        }
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(id = R.string.header_receipt_detail),
                    fontFamily = font_archivo,
                    fontWeight = FontWeight.Bold,
                    color = LighterBlack,
                    textAlign = TextAlign.Start,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, start = 24.dp, end = 24.dp)

                ) {
                    Column(
                        modifier = Modifier.weight(0.6f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.field_receipt_number) + " " + analyzeExpenseResponse?.receiptNumber,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            textAlign = TextAlign.Start,
                            fontSize = 13.sp,
                        )
                        Text(
                            text = stringResource(R.string.field_date) + " " + analyzeExpenseResponse?.receiptDate?.let { receiptAPIDate ->
                                formatReceiptDetailDate(
                                    receiptAPIDate
                                )
                            },
                            fontFamily = font_sf_pro,
                            color = Color.Black,
                            textAlign = TextAlign.Start,
                            fontSize = 13.sp,
                        )

                    }
                    Column(
                        modifier = Modifier.weight(0.3f),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = analyzeExpenseResponse?.totalAmount ?: "",
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            textAlign = TextAlign.End,
                            fontSize = 13.sp,
                            modifier = Modifier
                        )
                        Text(
                            text = "434" + " Points",
                            color = Color.Black,
                            textAlign = TextAlign.End,
                            fontSize = 13.sp,
                            modifier = Modifier
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                Row(modifier = Modifier.background(Color.White)) {

                    val tabItems =
                        listOf(
                            ReceiptTabs.EligibleItems, ReceiptTabs.ReceiptImage
                        )

                    ScrollableTabRow(selectedTabIndex = selectedTab,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White),
                        containerColor = Color.White,
                        divider = {},
                        edgePadding = 0.dp,
                        indicator = { tabPositions ->
                            TabRowDefaults.Indicator(
                                Modifier
                                    .tabIndicatorOffset(tabPositions[selectedTab])
                                    .background(Color.White),
                                height = 2.dp,
                                color = VibrantPurple40
                            )
                        })
                    {
                        tabItems.forEachIndexed { index, it ->
                            Tab(
                                selected = selectedTab == index,
                                onClick = { selectedTab = index },

                                text = {
                                    if (selectedTab == index) {
                                        androidx.compose.material3.Text(
                                            text = stringResource(it.tabName),
                                            modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                                            fontFamily = font_archivo,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp
                                        )
                                    } else {
                                        androidx.compose.material3.Text(
                                            text = stringResource(it.tabName),
                                            fontFamily = font_archivo,
                                            modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp
                                        )
                                    }
                                },
                                selectedContentColor = VibrantPurple40,
                                unselectedContentColor = TextDarkGray,
                            )
                        }
                    }

                }
            }

            Column(
                modifier = Modifier
                    .background(TextPurpleLightBG)
                    .padding(16.dp)
                    .fillMaxWidth()
                    .weight(0.6f)
            ) {

                when (selectedTab) {

                    TAB_ELIGIBLE_ITEM -> {
                        val itemLists = analyzeExpenseResponse?.lineItems
                        ReceiptDetailTable(itemLists = itemLists)
                    }
                    TAB_ORIGINAL_RECEIPT_IMAGE -> {
                        Image(
                            painter = painterResource(id = R.drawable.receipt_dummy),
                            contentDescription = "image description",
                            contentScale = ContentScale.FillHeight,
                            modifier = Modifier
                                .height(280.dp)
                                .align(Alignment.CenterHorizontally)
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .padding(top = 24.dp)
                    .weight(0.2f)
                /*.align(Alignment.BottomCenter)*/,
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.manual_review_option),
                    style = TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 22.4.sp,
                        fontFamily = font_sf_pro,
                        fontWeight = FontWeight.Normal,
                        color = LighterBlack,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.clickable {
                        blurBG = AppConstants.BLUR_BG
                        openBottomsheet = true
//                        closePopup(ReceiptListScreenPopupState.MANUAL_REVIEW)
                    }
                )
                Text(
                    text = stringResource(id = R.string.download_option),
                    modifier = Modifier.padding(top = 16.dp),
                    style = TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 22.4.sp,
                        fontFamily = font_sf_pro,
                        fontWeight = FontWeight(400),
                        color = LighterBlack,
                        textAlign = TextAlign.Center
                    )
                )
            }
        }
    }



}