package com.salesforce.loyalty.mobile.myntorewards.views.receipts

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.gson.Gson
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.receiptscanning.models.AnalyzeExpenseResponse
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.*
import com.salesforce.loyalty.mobile.myntorewards.utilities.Common
import com.salesforce.loyalty.mobile.myntorewards.utilities.LocalFileManager
import com.salesforce.loyalty.mobile.myntorewards.utilities.LocalFileManager.DIRECTORY_RECEIPT_LIST
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_MANUAL_BACK_BUTTON
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_MANUAL_RECEIPT_COMMENT
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_MANUAL_RECEIPT_DATE
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_MANUAL_RECEIPT_NUMBER
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_MANUAL_RECEIPT_POINT
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_MANUAL_REVIEW_CLOSE_POPUP_ICON
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_MANUAL_REVIEW_HEADER
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_MANUAL_SUBMIT_BUTTON
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.ReceiptListScreenPopupState
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint.ScanningViewModelInterface
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.ReceiptStatusUpdateViewState


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ManualReview(
    scanningViewModel: ScanningViewModelInterface,
    receiptId: String,
    purchase_date: String?,
    analyzeExpenseResponse: AnalyzeExpenseResponse?,
    totalPoints: String?,
    closePopup: (ReceiptListScreenPopupState) -> Unit,
    isSubmittedForManualReview: (Boolean) -> Unit
) {
    var statusUpdateInProgress by remember {
        mutableStateOf(false)
    }
    val context: Context = LocalContext.current

    Column(
        modifier = Modifier
            .background(MyProfileScreenBG, RoundedCornerShape(22.dp, 22.dp, 0.dp, 0.dp))
            .padding(start = 20.dp, end = 18.dp, top = 16.dp, bottom = 16.dp)
            .fillMaxHeight(0.7f)
            .verticalScroll(
                rememberScrollState()
            )
            .navigationBarsPadding()
            .imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        val keyboardController = LocalSoftwareKeyboardController.current
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 16.dp),
            horizontalAlignment = Alignment.End
        ) {
            Image(
                painter = painterResource(id = R.drawable.close_button_without_bg),
                contentDescription = stringResource(R.string.cd_close_popup),
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .clickable {
                        closePopup(ReceiptListScreenPopupState.RECEIPT_LIST_SCREEN)
                    }.testTag(TEST_TAG_MANUAL_REVIEW_CLOSE_POPUP_ICON)
                    .align(Alignment.End))
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(id = R.string.text_submit_manual_review),
            fontWeight = FontWeight.SemiBold,
            color = LighterBlack,
            fontFamily = font_sf_pro,
            textAlign = TextAlign.Start,
            fontSize = 16.sp,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Start)
                .padding(start = 4.dp).testTag(TEST_TAG_MANUAL_REVIEW_HEADER)
        )

        Spacer(modifier = Modifier.height(8.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        ) {
            Column(
                modifier = Modifier.weight(0.65f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                val receiptNumber = analyzeExpenseResponse?.receiptNumber ?: ""
                Text(
                    text = stringResource(R.string.field_receipt_number) + " " + receiptNumber,
                    fontWeight = FontWeight.SemiBold,
                    color = LighterBlack,
                    fontFamily = font_sf_pro,
                    textAlign = TextAlign.Start,
                    fontSize = 13.sp,
                    modifier = Modifier.testTag(TEST_TAG_MANUAL_RECEIPT_NUMBER)
                )

                Text(
                    text = stringResource(R.string.field_date) + " " + purchase_date?.let {
                        Common.formatReceiptListAPIDate(
                            it,
                            context
                        )
                    },
                    fontFamily = font_sf_pro,
                    color = LighterBlack,
                    textAlign = TextAlign.Start,
                    fontSize = 13.sp,
                    modifier = Modifier.testTag(TEST_TAG_MANUAL_RECEIPT_DATE)
                )

            }
            Column(
                modifier = Modifier.weight(0.3f).testTag(TEST_TAG_MANUAL_RECEIPT_POINT),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.End

            ) {
                Text(
                    text = analyzeExpenseResponse?.totalAmount ?: "",
                    fontWeight = FontWeight.SemiBold,
                    color = LighterBlack,
                    fontFamily = font_sf_pro,
                    textAlign = TextAlign.End,
                    fontSize = 13.sp,
                )
                totalPoints?.let {
                    Text(
                        text = it + " " + stringResource(R.string.receipt_points),
                        fontFamily = font_sf_pro,
                        color = LighterBlack,
                        textAlign = TextAlign.Start,
                        fontSize = 13.sp,
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(id = R.string.comment_text),
            style = TextStyle(
                fontSize = 13.sp,
                lineHeight = 22.4.sp,
                fontFamily = font_sf_pro,
                fontWeight = FontWeight.Normal,
                color = LightBlack,
                textAlign = TextAlign.Start,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(8.dp))

        var reviewText by remember { mutableStateOf(TextFieldValue("")) }
        var manualReviewSubmitted by remember {
            mutableStateOf(false)
        }
        val receiptStatusUpdateViewState by scanningViewModel.receiptStatusUpdateViewStateLiveData.observeAsState()
        OutlinedTextField(
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = Color(0xFFA0A0A0),
                    shape = RoundedCornerShape(size = 8.dp)
                )
                .fillMaxWidth()
                .height(186.dp)
                .background(
                    color = Color(0xFFFAFCFF),
                    shape = RoundedCornerShape(size = 8.dp)
                ).testTag(TEST_TAG_MANUAL_RECEIPT_COMMENT),
            placeholder = {
                Text(
                    text = stringResource(id = R.string.review_screen_placeholder_text),
                    style = TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        fontFamily = font_sf_pro,
                        fontWeight = FontWeight.Normal,
                        color = TextDarkGray,
                    )
                )
            },
            shape = RoundedCornerShape(8.dp),
            value = reviewText,
            onValueChange = { reviewText = it },
            keyboardOptions = KeyboardOptions(imeAction = androidx.compose.ui.text.input.ImeAction.Done),
            maxLines = 7,
            textStyle = TextStyle(
                fontSize = 16.sp,
                lineHeight = 24.sp,
                fontFamily = font_sf_pro,
                fontWeight = FontWeight.Normal,
                color = TextDarkGray,
            )
        )

        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                modifier = Modifier.testTag(TEST_TAG_MANUAL_SUBMIT_BUTTON)
                    .fillMaxWidth(), onClick = {
                    keyboardController?.hide()
                    manualReviewSubmitted = true
                },
                colors = ButtonDefaults.buttonColors(VibrantPurple40),
                shape = RoundedCornerShape(100.dp)

            ) {
                Text(
                    text = stringResource(id = R.string.text_submit_manual_review),
                    fontFamily = font_sf_pro,
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .padding(top = 3.dp, bottom = 3.dp)
                )
            }

            Text(
                text = stringResource(id = R.string.back_text),
                fontFamily = font_sf_pro,
                modifier = Modifier
                    .padding(top = 12.dp, bottom = 3.dp)
                    .testTag(
                        TEST_TAG_MANUAL_BACK_BUTTON
                    )
                    .clickable {
                        keyboardController?.hide()
                        closePopup(ReceiptListScreenPopupState.RECEIPT_DETAIL)

                    },
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                color = LighterBlack,
                fontWeight = FontWeight.Normal,

                )
        }
        if (manualReviewSubmitted) {
            LaunchedEffect(key1 = true) {
                scanningViewModel.submitForManualReview(receiptId, reviewText.text)
            }
        }
        when (receiptStatusUpdateViewState) {
            is ReceiptStatusUpdateViewState.ReceiptStatusUpdateSuccess -> {
                if (statusUpdateInProgress) {
                    val receiptNumber = analyzeExpenseResponse?.receiptNumber ?: ""
                    Toast.makeText(
                        LocalContext.current,
                        "Receipt $receiptNumber was submitted for manual review.",
                        Toast.LENGTH_LONG
                    ).show()
                    reviewText = TextFieldValue("")
                    closePopup(ReceiptListScreenPopupState.RECEIPT_LIST_SCREEN)
                    statusUpdateInProgress = false

                    isSubmittedForManualReview(true)
                    // Invalidate the receipt list cache
                    LocalFileManager.clearFolder(context, DIRECTORY_RECEIPT_LIST)
                }
            }
            ReceiptStatusUpdateViewState.ReceiptStatusUpdateFailure -> {
                if(statusUpdateInProgress) {
                    Toast.makeText(
                        LocalContext.current,
                        "Failed to Submit for Manual Review. Try again later.",
                        Toast.LENGTH_LONG
                    ).show()
                    statusUpdateInProgress = false
                }
            }
            ReceiptStatusUpdateViewState.ReceiptStatusUpdateInProgress -> {
                statusUpdateInProgress = true
            }
            else -> {}
        }


    }
}