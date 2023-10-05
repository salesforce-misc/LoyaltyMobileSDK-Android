package com.salesforce.loyalty.mobile.myntorewards.views.receipts

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.gson.Gson
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.receiptscanning.api.ReceiptScanningConfig
import com.salesforce.loyalty.mobile.myntorewards.receiptscanning.models.AnalyzeExpenseResponse
import com.salesforce.loyalty.mobile.myntorewards.receiptscanning.models.LineItem
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.LighterBlack
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.MyProfileScreenBG
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.VibrantPurple40
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.font_sf_pro
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants
import com.salesforce.loyalty.mobile.myntorewards.utilities.Common
import com.salesforce.loyalty.mobile.myntorewards.utilities.CommunityMemberModel
import com.salesforce.loyalty.mobile.myntorewards.utilities.ReceiptScanningBottomSheetType
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_RECEIPT_TABLE_SCREEN
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_ROW_STORE_DETAILS
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_TRY_AGAIN_SCANNED_RECEIPT
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint.ScanningViewModelInterface
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.CreateTransactionJournalViewState
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.ReceiptStatusUpdateViewState
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.UploadRecieptCancelledViewState
import com.salesforce.loyalty.mobile.myntorewards.views.navigation.MoreScreens
import com.salesforce.loyalty.mobile.sources.PrefHelper
import com.salesforce.loyalty.mobile.sources.forceUtils.Logger

@Composable
fun ShowScannedReceiptScreen(
    navHostController: NavHostController,
    scanningViewModel: ScanningViewModelInterface,
    analyzeExpenseResponse: AnalyzeExpenseResponse?,
    closePopup: () -> Unit,
    openCongratsPopup: (popupStatus: ReceiptScanningBottomSheetType) -> Unit,
    setTotalPoints: (totalPoints: String?) -> Unit
) {
    var congPopupState by remember { mutableStateOf(false) }
    var submissionTryAgainState by remember { mutableStateOf(false) }
    val createTransactionJournalViewState by scanningViewModel.createTransactionJournalViewStateLiveData.observeAsState()
    val cancelSubmissionViewState by scanningViewModel.cancellingSubmissionLiveData.observeAsState()
    val receiptStatusUpdateViewState by scanningViewModel.receiptStatusUpdateViewStateLiveData.observeAsState()
    var inProgress by remember { mutableStateOf(false) }
    var cancelInProgress by remember { mutableStateOf(false) }
    var statusUpdateInProgress by remember { mutableStateOf(false) }
    val itemLists = analyzeExpenseResponse?.lineItems
    val context: Context = LocalContext.current
    Box() {
        Column(
            modifier = Modifier
                .fillMaxHeight(0.92f)
                .fillMaxWidth()
                .background(MyProfileScreenBG, RoundedCornerShape(22.dp))
                .padding(16.dp)
                .testTag(TEST_TAG_RECEIPT_TABLE_SCREEN),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.field_receipt_number) + " " + analyzeExpenseResponse?.receiptNumber,
                style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 20.sp,
                    fontFamily = font_sf_pro,
                    fontWeight = FontWeight.SemiBold,
                    color = LighterBlack,
                )
            )
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(TEST_TAG_ROW_STORE_DETAILS)
//                        .padding(16.dp)
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
                    textAlign = TextAlign.Start,
                    fontSize = 13.sp,
                    modifier = Modifier.weight(0.5f)
                )
            }
            Column(
                modifier = Modifier.weight(0.8f),
            ) {
                ReceiptDetailTable(itemLists = itemLists)
            }

            Column(
                modifier = Modifier.weight(0.2f)
                    .padding(16.dp)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
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
                    val totalPoint =
                        (receiptStatusUpdateViewState as ReceiptStatusUpdateViewState.ReceiptStatusUpdateSuccess).points
                    setTotalPoints(totalPoint)
                    openCongratsPopup(ReceiptScanningBottomSheetType.POPUP_CONGRATULATIONS)
                }
            }

            ReceiptStatusUpdateViewState.ReceiptStatusUpdateFailure -> {
                if (statusUpdateInProgress) {
                    statusUpdateInProgress = false
                    openCongratsPopup(ReceiptScanningBottomSheetType.POPUP_CONGRATULATIONS)
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
                        analyzeExpenseResponse?.receiptId?.let {
                            getUpdatedReceiptStatus(
                                context,
                                receiptId = it,
                                scanningViewModel
                            )
                        }
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
                    closePopup()
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
                    closePopup()
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

/*@Preview
@Composable
fun ScannedPreview(){
    ShowScannedReceiptPopup()
}*/

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
