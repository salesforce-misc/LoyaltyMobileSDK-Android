package com.salesforce.loyalty.mobile.myntorewards.views.receipts

import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetState
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.gson.Gson
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.receiptscanning.models.ConfidenceStatus
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.LighterBlack
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.MyProfileScreenBG
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.VibrantPurple40
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.font_sf_pro
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants
import com.salesforce.loyalty.mobile.myntorewards.utilities.ReceiptScanningBottomSheetType
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_RECEIPT_UPLOAD
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint.ScanningViewModelInterface
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.ReceiptScanningViewState
import com.salesforce.loyalty.mobile.myntorewards.views.navigation.MoreScreens
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ImagePreviewScreen(
    navController: NavHostController,
    scanningViewModel: ScanningViewModelInterface,
    capturedImageBitmap: ImageBitmap,
    imageClicked: (Boolean) -> Unit

) {
    var currentPopupState: ReceiptScanningBottomSheetType? by remember { mutableStateOf(null) }
    var currentProgress by remember { mutableStateOf(AppConstants.RECEIPT_PROGRESS_STARTED) }
    val scannedReceiptLiveData by scanningViewModel.scannedReceiptLiveData.observeAsState()
    val scannedReceiptViewState by scanningViewModel.receiptScanningViewStateLiveData.observeAsState()
    var imageMoreThan5MB by remember { mutableStateOf(false) }

    imageMoreThan5MB = isImageMoreThan5MB(capturedImageBitmap)


    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(initialValue = BottomSheetValue.Collapsed,
            confirmValueChange = {
                // Prevent collapsing by swipe down gesture
                it != BottomSheetValue.Collapsed
            })
    )

    // Declaring Coroutine scope
    val coroutineScope = rememberCoroutineScope()

    val openBottomSheet = {
        coroutineScope.launch {
            if (bottomSheetScaffoldState.bottomSheetState.isCollapsed) {
                bottomSheetScaffoldState.bottomSheetState.expand()
            }
        }
    }

    val closeBottomSheet = {
        coroutineScope.launch {
            if (bottomSheetScaffoldState.bottomSheetState.isExpanded) {
                bottomSheetScaffoldState.bottomSheetState.collapse()
            }
        }
        currentPopupState = null
        imageClicked(false)
    }


    if (imageMoreThan5MB) {
        currentPopupState = ReceiptScanningBottomSheetType.POPUP_ERROR_IMAGEMORETHAN5MB
        openBottomSheet()
    }

    val errorMessageDesc = stringResource(id = R.string.receipt_scanning_error_desc)
    var errorMessage by remember { mutableStateOf(errorMessageDesc) }

    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,

        sheetContent = {
            Spacer(modifier = Modifier.height(1.dp))
            currentPopupState?.let { bottomSheetType ->
                OpenReceiptBottomSheetContent(
                    bottomSheetType = bottomSheetType,
                    navController = navController,
                    errorMessage = errorMessage,
                    currentProgress = currentProgress,
                    closeSheet = { closeBottomSheet() },
                )
            }
        },
        sheetShape = RoundedCornerShape(AppConstants.POPUP_ROUNDED_CORNER_SIZE),
        sheetPeekHeight = 0.dp,
        sheetGesturesEnabled = false
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MyProfileScreenBG)
                .testTag(TestTags.TEST_TAG_IMAGE_PREVIEW_SCREEN)
        ) {
            var progressPopupState by remember { mutableStateOf(false) }
            var processClicked by remember { mutableStateOf(false) }

            Spacer(modifier = Modifier.height(50.dp))
            Image(

                painter = painterResource(id = R.drawable.back_arrow),
                contentDescription = stringResource(id = R.string.cd_white_back_button),

                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .padding(start = 8.dp, top = 10.dp, bottom = 10.dp)
                    .width(32.dp)
                    .height(32.dp)
                    .clickable {
                        imageClicked(false)
                    }
            )

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxHeight(0.7f)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            )
            {


                Image(
                    bitmap = capturedImageBitmap,
                    contentDescription = stringResource(id = R.string.cd_captured_photo),
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Button(
                    modifier = Modifier
                        .testTag(TEST_TAG_RECEIPT_UPLOAD)
                        .fillMaxWidth(), onClick = {
                        processClicked = true

                    },
                    colors = ButtonDefaults.buttonColors(VibrantPurple40),
                    shape = RoundedCornerShape(100.dp),
                    enabled = !imageMoreThan5MB

                ) {


                    Text(
                        text = stringResource(id = R.string.button_process),
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
                        .clickable {
                            imageClicked(false)
                        },
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    color = LighterBlack,
                    fontWeight = FontWeight.Normal,

                    )
            }

            if (processClicked) {
                // Encoding the image to Base 64 and sending it to Analyze API.
                val baos = ByteArrayOutputStream()
                capturedImageBitmap?.asAndroidBitmap()?.let {
                    it.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                    val b: ByteArray = baos.toByteArray()
//                    val encImage: String = Base64.encodeToString(b, Base64.NO_WRAP)
//                    Log.d("ImageCaptureScreen", "Encoded image: $encImage")
                    val context = LocalContext.current
                    LaunchedEffect(key1 = true) {
//                        scanningViewModel.analyzeExpense(context, b)
                        scanningViewModel.uploadReceipt(context, b)
//                        scanningViewModel.analyzeExpense(context, "5f867fb8-edcb-800f-fbdb-3b81788e98f1.jpg")
                    }
                }
                processClicked = false
            }
            when (scannedReceiptViewState) {
                is ReceiptScanningViewState.UploadReceiptInProgress -> {
                    progressPopupState = true
                    currentProgress = AppConstants.RECEIPT_PROGRESS_FIRST_STEP
                    currentPopupState = ReceiptScanningBottomSheetType.POPUP_PROGRESS
                    openBottomSheet()
                }

                is ReceiptScanningViewState.UploadReceiptSuccess -> {
                    progressPopupState = true
                    currentProgress = AppConstants.RECEIPT_PROGRESS_SECOND_STEP
                    currentPopupState = ReceiptScanningBottomSheetType.POPUP_PROGRESS
                    openBottomSheet()
                }

                is ReceiptScanningViewState.ReceiptScanningSuccess -> {
                    if (progressPopupState) {
                        progressPopupState = true
                        currentProgress = AppConstants.RECEIPT_PROGRESS_COMPLETED
                        currentPopupState = ReceiptScanningBottomSheetType.POPUP_PROGRESS
                        openBottomSheet()
                        progressPopupState = false
                        closeBottomSheet()
                        /*currentPopupState = ReceiptScanningBottomSheetType.POPUP_SCANNED_RECEIPT
                        openBottomSheet()*/
                        scannedReceiptLiveData?.let { response ->
                            val confidenceStatus = response.confidenceStatus
                            confidenceStatus?.let {
                                if (ConfidenceStatus.FAILURE.status == it) {
                                    currentPopupState = ReceiptScanningBottomSheetType.POPUP_ERROR
                                    errorMessage =
                                        stringResource(id = R.string.receipt_error_confidence_status_failure)
                                    openBottomSheet()
                                } else {
                                    navController.currentBackStackEntry?.savedStateHandle?.apply {
                                        set(
                                            AppConstants.KEY_ANALYZED_AWS_RESPONSE,
                                            Gson().toJson(response)
                                        )
                                    }
                                    navController.navigate(MoreScreens.ScannedReceiptScreen.route)
                                }
                            }
                        }
                    }
                }

                is ReceiptScanningViewState.ReceiptScanningInProgress -> {
                    progressPopupState = true
                    currentPopupState = ReceiptScanningBottomSheetType.POPUP_PROGRESS
                    openBottomSheet()
                }

                is ReceiptScanningViewState.ReceiptScanningFailure -> {
                    if (progressPopupState) {
                        progressPopupState = false
                        currentPopupState = ReceiptScanningBottomSheetType.POPUP_ERROR
                        errorMessage = (scannedReceiptViewState as ReceiptScanningViewState.ReceiptScanningFailure).message?.let {
                            it
                        } ?: stringResource(id = R.string.receipt_scanning_error_desc)
                        openBottomSheet()
                    }
                    // TODO Handle failure scenario.
                }

                else -> {}
            }
        }
    }
}

fun isImageMoreThan5MB(capturedImageBitmap: ImageBitmap): Boolean {
    val baos = ByteArrayOutputStream()
    capturedImageBitmap.asAndroidBitmap().let {
        it.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b: ByteArray = baos.toByteArray()
        val length = (b.size / 1024)
        return length > (5 * 1024)
    }
}