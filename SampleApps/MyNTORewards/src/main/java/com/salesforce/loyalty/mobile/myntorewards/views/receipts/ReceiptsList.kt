package com.salesforce.loyalty.mobile.myntorewards.views.receipts

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.gson.Gson
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.receiptscanning.models.AnalyzeExpenseResponse
import com.salesforce.loyalty.mobile.myntorewards.receiptscanning.models.Record
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.*
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.KEY_PROCESSED_AWS_RESPONSE
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.KEY_PURCHASE_DATE
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.KEY_RECEIPT_ID
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.KEY_RECEIPT_STATUS
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.KEY_RECEIPT_TOTAL_POINTS
import com.salesforce.loyalty.mobile.myntorewards.utilities.Common.Companion.formatReceiptListAPIDate
import com.salesforce.loyalty.mobile.myntorewards.utilities.Common.Companion.formatReceiptListDate
import com.salesforce.loyalty.mobile.myntorewards.utilities.Common.Companion.formatReceiptListDateAsPerAPIResponse
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_RECEIPT_LIST
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_RECEIPT_LIST_ITEM
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_RECEIPT_LIST_SCREEN
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_SEARCH_FIELD
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.ReceiptListScreenPopupState
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint.ScanningViewModelInterface
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.ReceiptViewState
import com.salesforce.loyalty.mobile.myntorewards.views.navigation.MoreScreens
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ReceiptsList(navController: NavHostController, scanningViewModel: ScanningViewModelInterface) {

    var searchText by rememberSaveable { mutableStateOf("") }
    val receiptListLiveData by scanningViewModel.receiptListLiveData.observeAsState()
    val receiptListViewState by scanningViewModel.receiptListViewState.observeAsState()
    var refreshing by remember { mutableStateOf(false) }
    val refreshScope = rememberCoroutineScope()


    val receiptRecords = receiptListLiveData?.records
    val focusManager = LocalFocusManager.current
    val context: Context = LocalContext.current
    fun refresh() = refreshScope.launch {
        scanningViewModel.getReceiptLists(context, true)
    }

//    var blurBG by remember { mutableStateOf(0.dp) }
    var isInProgress by remember { mutableStateOf(true) }
    LaunchedEffect(key1 = true) {
        scanningViewModel.getReceiptLists(context)
    }
    val state = rememberPullRefreshState(refreshing, ::refresh)
    Box(contentAlignment = Alignment.TopCenter) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp)
                .background(TextPurpleLightBG)
                .pullRefresh(state)
                .testTag(TEST_TAG_RECEIPT_LIST_SCREEN)
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        focusManager.clearFocus()
                    })
                },

            ) {

            Spacer(modifier = Modifier.height(50.dp))

            Text(
                text = stringResource(id = R.string.header_label_receipt),
                fontFamily = font_sf_pro,
                textAlign = TextAlign.Center,
                fontSize = 24.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(top = 3.dp, bottom = 3.dp)
            )
            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    SearchBar(onSearch = {
                        searchText = it
                    }, Modifier.weight(0.7f), focusManager)
                    Button(
                        modifier = Modifier
                            .weight(0.3f), onClick = {
                            navController.navigate(MoreScreens.CaptureImageScreen.route)
                        },
                        colors = ButtonDefaults.buttonColors(VibrantPurple40),
                        shape = RoundedCornerShape(100.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.label_new_receipt),
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
            when (receiptListViewState) {
                is ReceiptViewState.ReceiptListFetchSuccessView -> {
                    isInProgress = false
                }

                is ReceiptViewState.ReceiptListFetchInProgressView -> {
                    isInProgress = true
                }

                is ReceiptViewState.ReceiptListFetchFailureView -> {
                    isInProgress = false
                }

                    else -> {}
                }
                if (isInProgress) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .fillMaxSize(0.1f)
                        )
                    }

                } else {

                    if (receiptRecords?.isNotEmpty() == true) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag(TEST_TAG_RECEIPT_LIST),
                            contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            receiptRecords.let {
                                val filteredList = receiptRecords.filter {
                                    if (searchText.isNotEmpty() && !it.processedAWSResponse.isNullOrEmpty()) {
                                        it.processedAWSResponse.contains(
                                            searchText,
                                            ignoreCase = true
                                        )
                                    } else {
                                        true
                                    }
                                }
                                items(filteredList.size) {
                                    ReceiptItem(filteredList[it], navController, scanningViewModel)
                                }

                            }
                        }
                    } else {
                        ReceiptEmptyView()
                    }

                }


            }
        }
        PullRefreshIndicator(refreshing, state)
    }
}


@Composable
fun ReceiptItem(receipt: Record, navController: NavHostController, scanningViewModel: ScanningViewModelInterface) {
    var openReceiptDetail by remember { mutableStateOf(ReceiptListScreenPopupState.RECEIPT_LIST_SCREEN) }
    val context: Context = LocalContext.current
    val interactionSource = remember { MutableInteractionSource() }
    Spacer(modifier = Modifier.height(12.dp))
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(8.dp))
            .padding(16.dp)
            .testTag(TEST_TAG_RECEIPT_LIST_ITEM)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                navController.currentBackStackEntry?.arguments?.putString(
                    KEY_PROCESSED_AWS_RESPONSE,
                    receipt.processedAWSResponse
                )
                navController.currentBackStackEntry?.arguments?.putString(
                    KEY_RECEIPT_TOTAL_POINTS,
                    receipt.total_points?.toString()
                )
                navController.currentBackStackEntry?.arguments?.putString(
                    KEY_PURCHASE_DATE,
                    receipt.purchase_date
                )
                navController.currentBackStackEntry?.arguments?.putString(
                    KEY_RECEIPT_ID,
                    receipt.id
                )
                navController.currentBackStackEntry?.arguments?.putString(
                    KEY_RECEIPT_STATUS,
                    receipt.receipt_status
                )
                navController.navigate(MoreScreens.ReceiptDetailScreen.route)
            }
    ) {
        Column(modifier = Modifier.weight(0.7f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = stringResource(R.string.field_receipt_number) + " " + formatReceiptListAPIDate(receipt.purchase_date, context),
                fontWeight = FontWeight.Bold,
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
            // ToDo currency type should be added.
            val amount = receipt.total_amount ?: "0.0"
            Text(
                text = "" + amount,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.End,
                fontSize = 13.sp,
                modifier = Modifier
            )

            ReceiptStatusText(receipt.total_points?.toString(), receipt.receipt_status)

        }
    }

    when (openReceiptDetail) {
        ReceiptListScreenPopupState.RECEIPT_DETAIL -> {
            navController.navigate(MoreScreens.ReceiptDetailScreen.route)
        }/*ReceiptDetail(navController*//*closePopup = {
            openReceiptDetail = it
            if (it != ReceiptListScreenPopupState.MANUAL_REVIEW)
                blurBG(AppConstants.NO_BLUR_BG)
        }*//*)*/

        ReceiptListScreenPopupState.MANUAL_REVIEW -> {}/*ManualReview(
            scanningViewModel,
            receipt.id,
            receipt.processedAWSResponse,
            closePopup = {
                openReceiptDetail = it
            })*/

        else -> {}
    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(onSearch: (String) -> Unit, modifier: Modifier, focusManager: FocusManager) {
    var text by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val trailingIconView = @Composable {
        IconButton(
            onClick = {
                text = ""
                onSearch(text)
            },
        ) {
            Icon(
                Icons.Filled.Clear,
                contentDescription = null,
                tint = SearchIconColor
            )
        }
    }
    TextField(
        value = text,
        onValueChange = {
            text = it
            onSearch(it)
        },
        placeholder = {
            Text(
                stringResource(id = R.string.receipt_search_text),
                color = TextDarkGray
            )
        },
        leadingIcon = {
            Icon(
                Icons.Filled.Search,
                contentDescription = null,
                tint = SearchIconColor
            )
        },
        trailingIcon = if (text.isNotBlank()) trailingIconView else null,
        modifier = modifier
            .fillMaxWidth()
            .testTag(TEST_TAG_SEARCH_FIELD),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = {
            onSearch(text)
            // Hide the keyboard after submitting the search
            keyboardController?.hide()
            //or hide keyboard
            focusManager.clearFocus()

        }),
        shape = RoundedCornerShape(size = 12.dp),
        colors = TextFieldDefaults.colors(
            disabledTextColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            focusedContainerColor = SearchFieldColor,
            unfocusedContainerColor = SearchFieldColor
        )
    )
}

@Composable
fun ReceiptEmptyView() {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_empty_view),
            contentDescription = stringResource(id = R.string.label_empty_receipt)
        )
        Spacer(modifier = Modifier.padding(10.dp))
        androidx.compose.material3.Text(
            text = stringResource(id = R.string.label_empty_receipt),
            fontWeight = FontWeight.Bold,
            fontFamily = font_sf_pro,
            color = Color.Black,
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ReceiptStatusText(totalPoints: String?, status: String) {
    var receiptPoints = ""
    var textColour = Color.Black
    when (status) {
        AppConstants.RECEIPT_POINT_STATUS_PENDING, AppConstants.RECEIPT_POINT_STATUS_MANUAL_REVIEW -> {
            receiptPoints = stringResource(id = R.string.receipt_status_pending)
            textColour = ReceiptPointColourWarning
        }

        AppConstants.RECEIPT_POINT_STATUS_PROCESSING -> {
            receiptPoints = stringResource(R.string.receipt_status_processing)
            textColour = ReceiptPointColourWarning
        }

        AppConstants.RECEIPT_POINT_STATUS_PROCESSED -> {
            val points = totalPoints ?: "0"
            receiptPoints =
                "" + points + " " + stringResource(R.string.receipt_points)
            textColour = TextGreen
        }

        AppConstants.RECEIPT_POINT_STATUS_REJECTED -> {
            receiptPoints = stringResource(R.string.receipt_status_rejected)
            textColour = ReceiptPointColourError
        }

        else -> {
            receiptPoints = status
            textColour = ReceiptPointColourWarning
        }
    }
    Text(
        text = "" + receiptPoints,
        color = textColour,
        textAlign = TextAlign.End,
        fontSize = 13.sp,
        modifier = Modifier
    )
}
/*
@Preview
@Composable
fun PreviewReceiptScreen() {
    val navController = rememberNavController()
    ReceiptsList(navController)
}*/
