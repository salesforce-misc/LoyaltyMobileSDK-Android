package com.salesforce.loyalty.mobile.myntorewards.views.receipts

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.google.gson.Gson
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.receiptscanning.api.ReceiptScanningConfig
import com.salesforce.loyalty.mobile.myntorewards.receiptscanning.models.AnalyzeExpenseResponse
import com.salesforce.loyalty.mobile.myntorewards.receiptscanning.models.ConfidenceStatus
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.LighterBlack
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.MyProfileScreenBG
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.VibrantPurple40
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.font_sf_pro
import com.salesforce.loyalty.mobile.myntorewards.utilities.*
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_RECEIPT_TABLE_SCREEN
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_ROW_STORE_DETAILS
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_TRY_AGAIN_SCANNED_RECEIPT
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.ReceiptListScreenPopupState
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint.ScanningViewModelInterface
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.CreateTransactionJournalViewState
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.ReceiptStatusUpdateViewState
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.UploadRecieptCancelledViewState
import com.salesforce.loyalty.mobile.myntorewards.views.navigation.MoreScreens
import com.salesforce.loyalty.mobile.sources.PrefHelper
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun ShowScannedReceiptScreen(
    navHostController: NavHostController,
    scanningViewModel: ScanningViewModelInterface
) {
    var congPopupState by remember { mutableStateOf(false) }
    var submissionTryAgainState by remember { mutableStateOf(false) }
    val createTransactionJournalViewState by scanningViewModel.createTransactionJournalViewStateLiveData.observeAsState()
    val cancelSubmissionViewState by scanningViewModel.cancellingSubmissionLiveData.observeAsState()
    val receiptStatusUpdateViewState by scanningViewModel.receiptStatusUpdateViewStateLiveData.observeAsState()
    var cancelInProgress by remember { mutableStateOf(false) }
    var inProgress by remember { mutableStateOf(false) }
    var statusUpdateInProgress by remember { mutableStateOf(false) }
    var manualReviewOption  by remember { mutableStateOf(false) }
    var submitOption  by remember { mutableStateOf(true) }

    val analyzeExpenseResponseStr = navHostController.previousBackStackEntry?.savedStateHandle?.get<String>(
        AppConstants.KEY_ANALYZED_AWS_RESPONSE
    )
    val analyzeExpenseResponse = Gson().fromJson(analyzeExpenseResponseStr, AnalyzeExpenseResponse::class.java)

    val itemLists = analyzeExpenseResponse?.lineItems
    val eligibleItems = itemLists?.filter { it.isEligible == true }
    val inEligibleItems = itemLists?.filter { it.isEligible == false }
    val context: Context = LocalContext.current
    var openBottomsheet by remember { mutableStateOf(false) }
    var blurBG by remember { mutableStateOf(0.dp) }
    var isSubmittedForManualReview by remember { mutableStateOf(false) }
    var currentPopupState: ReceiptScanningBottomSheetType? by remember { mutableStateOf(null) }
    var totalPoints: String? by remember {
        mutableStateOf(null)
    }
    val confidenceStatus = analyzeExpenseResponse?.confidenceStatus
    confidenceStatus?.let {
        if (ConfidenceStatus.PARTIAL.status == it) {
            manualReviewOption = true
            submitOption = false
        }
    }
    if (eligibleItems?.isEmpty() == true) {
        manualReviewOption = true
        submitOption = false
    }
    if (inEligibleItems?.isNotEmpty() == true) {
        manualReviewOption = true
    }

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
    keyboardController?.hide()
    val closeBottomSheet = {
        //showBottomBar(true)
        keyboardController?.hide()
        blurBG = AppConstants.NO_BLUR_BG
        coroutineScope.launch {
            if (bottomSheetScaffoldState.bottomSheetState.isExpanded) {
                bottomSheetScaffoldState.bottomSheetState.collapse()
            }
        }
        currentPopupState = null
    }
    androidx.compose.material.BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,

        sheetContent = {
            Spacer(modifier = Modifier.height(1.dp))
            analyzeExpenseResponse?.let { analyzeExpenseResponse ->
                analyzeExpenseResponse.receiptId?.let {
                    currentPopupState?.let { bottomSheetType ->
                        openSheet(
                            bottomSheetType,
                            scanningViewModel,navHostController,
                            analyzeExpenseResponse,
                            totalPoints,
                            closePopup = {
                                openBottomsheet = false
                                closeBottomSheet()

                            }) { isSubmittedForManualReview = true }
                    }
                }
                }
        },
        sheetShape = RoundedCornerShape(AppConstants.POPUP_ROUNDED_CORNER_SIZE, AppConstants.POPUP_ROUNDED_CORNER_SIZE, 0.dp, 0.dp),
        sheetPeekHeight = 0.dp,
        sheetGesturesEnabled = false
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .testTag(TEST_TAG_RECEIPT_TABLE_SCREEN),

                ) {
                Image(
                    painter = painterResource(id = R.drawable.back_arrow),
                    contentDescription = stringResource(id = R.string.cd_receipt_back_button),
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .padding(top = 40.dp, start = 12.dp, bottom = 16.dp)
                        .clickable {
                            navHostController.popBackStack()
                        }
                )
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MyProfileScreenBG),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
//                Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.field_receipt_number) + " " + analyzeExpenseResponse?.receiptNumber,
                        modifier = Modifier.padding(16.dp),
                        style = TextStyle(
                            fontSize = 16.sp,
                            lineHeight = 20.sp,
                            fontFamily = font_sf_pro,
                            fontWeight = FontWeight.Bold,
                            color = LighterBlack,
                        )
                    )
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag(TEST_TAG_ROW_STORE_DETAILS)
                            .padding(start = 16.dp, end = 16.dp)
                    ) {

                        Text(
                            text = stringResource(R.string.field_store) + " " + analyzeExpenseResponse?.storeName,
                            color = Color.Black,
                            textAlign = TextAlign.Start,
                            fontSize = 13.sp,
                            modifier = Modifier.weight(0.5f)
                        )

                        Text(
                            text = stringResource(R.string.field_date_colon) + " " + analyzeExpenseResponse?.receiptDate?.let {
                                Common.formatReceiptListAPIDate(
                                    it, context
                                )
                            },
                            color = Color.Black,
                            textAlign = TextAlign.End,
                            fontSize = 13.sp,
                            modifier = Modifier.weight(0.5f)
                        )
                    }
                    Column(
                        modifier = Modifier
                            .weight(0.7f)
                            .padding(start = 8.dp, end = 8.dp, top = 8.dp),
                    ) {
                        ReceiptDetailTable(itemLists = itemLists)
                    }


                    Column(
                        modifier = Modifier
                            .weight(0.3f)
                            .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 16.dp)
                        /*.fillMaxHeight()*/,
                        verticalArrangement = Arrangement.Bottom,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if(submitOption) {
                            Button(
                                modifier = Modifier
                                    .fillMaxWidth(), onClick = {
                                    congPopupState = true
//                        navHostController.navigate(MoreScreens.ScannedCongratsScreen.route)
                                },
                                colors = ButtonDefaults.buttonColors(VibrantPurple40),
                                shape = RoundedCornerShape(100.dp)

                            ) {
                                Text(
                                    text = stringResource(id = R.string.button_submit_receipt),
                                    fontFamily = font_sf_pro,
                                    textAlign = TextAlign.Center,
                                    fontSize = 16.sp,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier
                                        .padding(top = 3.dp, bottom = 3.dp)
                                )
                            }
                            Text(
                                text = stringResource(id = R.string.button_try_again),
                                fontFamily = font_sf_pro,
                                modifier = Modifier
                                    .padding(top = 12.dp, bottom = 3.dp)
                                    .testTag(
                                        TEST_TAG_TRY_AGAIN_SCANNED_RECEIPT
                                    )
                                    .clickable {
                                        submissionTryAgainState = true
                                    },
                                textAlign = TextAlign.Center,
                                fontSize = 16.sp,
                                color = LighterBlack,
                                fontWeight = FontWeight.Normal,

                                )
                        } else {
                            Button(
                                modifier = Modifier
                                    .fillMaxWidth(), onClick = {
                                    submissionTryAgainState = true
                                },
                                colors = ButtonDefaults.buttonColors(VibrantPurple40),
                                shape = RoundedCornerShape(100.dp)

                            ) {
                                Text(
                                    text = stringResource(id = R.string.button_try_again),
                                    fontFamily = font_sf_pro,
                                    textAlign = TextAlign.Center,
                                    fontSize = 16.sp,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier
                                        .padding(top = 3.dp, bottom = 3.dp)
                                )
                            }
                        }


                        if (manualReviewOption) {
                            Text(
                                text = stringResource(id = R.string.manual_review_option),
                                fontFamily = font_sf_pro,
                                modifier = Modifier
                                    .padding(top = 12.dp, bottom = 3.dp)
                                    .clickable {
                                        currentPopupState =
                                            ReceiptScanningBottomSheetType.POPUP_MANUAL_REVIEW
                                        openBottomSheet()
                                    },
                                textAlign = TextAlign.Center,
                                fontSize = 16.sp,
                                color = LighterBlack,
                                fontWeight = FontWeight.Normal,

                                )
                        }
                    }
                }
            }
            if (congPopupState) {
                congPopupState = false
                LaunchedEffect(key1 = true) {
                    analyzeExpenseResponse?.let {
                        it.receiptId?.let { it1 -> scanningViewModel.submitForProcessing(it1) }
                    }
                }
            }
            if (submissionTryAgainState) {
                submissionTryAgainState = false
                LaunchedEffect(key1 = true) {
                    analyzeExpenseResponse?.let {
                        it.receiptId?.let { it1 -> scanningViewModel.cancellingSubmission(it1) }
                    }
                }
            }
            when (receiptStatusUpdateViewState) {
                is ReceiptStatusUpdateViewState.ReceiptStatusUpdateSuccess -> {
                    if (statusUpdateInProgress) {
                        statusUpdateInProgress = false
                        totalPoints =
                            (receiptStatusUpdateViewState as ReceiptStatusUpdateViewState.ReceiptStatusUpdateSuccess).points
                        currentPopupState = ReceiptScanningBottomSheetType.POPUP_CONGRATULATIONS
                        openBottomSheet()
                    }
                }

                ReceiptStatusUpdateViewState.ReceiptStatusUpdateFailure -> {
                    if (statusUpdateInProgress) {
                        statusUpdateInProgress = false
                        currentPopupState = ReceiptScanningBottomSheetType.POPUP_CONGRATULATIONS
                        openBottomSheet()
                    }
                }

                ReceiptStatusUpdateViewState.ReceiptStatusUpdateInProgress -> {
                    statusUpdateInProgress = true
                }

                else -> {}
            }
            when (createTransactionJournalViewState) {
                CreateTransactionJournalViewState.CreateTransactionJournalSuccess -> {
                    if (inProgress) {
                        inProgress = false
                        LaunchedEffect(key1 = true) {

                            (analyzeExpenseResponse?.receiptId?.let {
                                getUpdatedReceiptStatus(
                                    context,
                                    receiptId = it,
                                    scanningViewModel
                                )
                            } ?: "")

                        }
                    }
                }

                CreateTransactionJournalViewState.CreateTransactionJournalInProgress -> {
                    inProgress = true
                }

                CreateTransactionJournalViewState.CreateTransactionJournalFailure -> {
                    if (inProgress) {
                        inProgress = false
                    }
                }

                else -> {}
            }

            when (cancelSubmissionViewState) {
                UploadRecieptCancelledViewState.UploadRecieptCancelledSuccess -> {
                    if (cancelInProgress) {
                        cancelInProgress = false
//                    closePopup()
                        navHostController.popBackStack(
                            MoreScreens.CaptureImageScreen.route,
                            false
                        )
                    }
                }

                UploadRecieptCancelledViewState.UploadRecieptCancelledInProgress -> {
                    cancelInProgress = true
                }
                UploadRecieptCancelledViewState.UploadRecieptCancelledFailure -> {
                    if (cancelInProgress) {
                        cancelInProgress = false
//                    closePopup()
                        navHostController.popBackStack(
                            MoreScreens.CaptureImageScreen.route,
                            false
                        )
                    }
                }
                else -> {}
            }

            if (inProgress || statusUpdateInProgress || cancelInProgress) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxSize(0.1f)
                        .align(Alignment.Center)
                )
            }
        }
    }
}

private fun getUpdatedReceiptStatus(
    context: Context,
    receiptId: String,
    scanningViewModel: ScanningViewModelInterface
) {
    val memberJson =
        PrefHelper.customPrefs(context)
            .getString(AppConstants.KEY_COMMUNITY_MEMBER, null)
    if (memberJson == null) {
    }
    val member = Gson().fromJson(memberJson, CommunityMemberModel::class.java)
    val membershipKey = member.membershipNumber ?: ""
    scanningViewModel.getReceiptStatus(
        receiptId = receiptId,
        membershipKey,
        maxRetryCount = ReceiptScanningConfig.RECEIPT_STATUS_MAX_RETRY_COUNT,
        delaySeconds = ReceiptScanningConfig.RECEIPT_STATUS_FETCH_DELAY.toLong()
    )
}

@Composable
fun openSheet(
    bottomSheetType: ReceiptScanningBottomSheetType, scanningViewModel: ScanningViewModelInterface,
    navController: NavController,
    analyzeExpenseResponse: AnalyzeExpenseResponse?,
    totalPoints: String?,
    closePopup: (ReceiptListScreenPopupState) -> Unit,
    isSubmittedForManualReview: (Boolean) -> Unit
) {
    val context = LocalContext.current
    when (bottomSheetType) {
        ReceiptScanningBottomSheetType.POPUP_MANUAL_REVIEW -> {
            analyzeExpenseResponse?.let { analyzeExpenseResponse ->
                analyzeExpenseResponse.receiptId?.let {
                    ManualReview(
                        scanningViewModel,
                        it,
                        analyzeExpenseResponse.receiptDate,
                        analyzeExpenseResponse,
                        null,
                        closePopup,
                        isSubmittedForManualReview
                    )
                }
            }
        }
        ReceiptScanningBottomSheetType.POPUP_CONGRATULATIONS -> {
            CongratulationsPopup(totalPoints = totalPoints, closePopup = {
                closePopup

                //Invalidate receipt list cache
                LocalFileManager.clearFolder(context, LocalFileManager.DIRECTORY_RECEIPT_LIST)
                navController.popBackStack(MoreScreens.ReceiptListScreen.route, false)
            }, scanAnotherReceipt = {
                closePopup
                //Invalidate receipt list cache
                LocalFileManager.clearFolder(context, LocalFileManager.DIRECTORY_RECEIPT_LIST)
            })
        }
        else -> {}
    }

}