package com.salesforce.loyalty.mobile.myntorewards.views.receipts

import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.LighterBlack
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.MyProfileScreenBG
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.VibrantPurple40
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.font_sf_pro
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants
import com.salesforce.loyalty.mobile.myntorewards.utilities.ReceiptScanningBottomSheetType
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint.ScanningViewModelInterface
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.ReceiptScanningViewState
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
    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,

        sheetContent = {
            Spacer(modifier = Modifier.height(1.dp))
            currentPopupState?.let { bottomSheetType ->
                OpenReceiptBottomSheetContent(
                    bottomSheetType = bottomSheetType,
                    navController = navController,
                    scannedReceiptLiveData = scannedReceiptLiveData,
                    scanningViewModel = scanningViewModel,
                    setBottomSheetState = {
                        currentPopupState = it
                    },
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
            var scannedReceiptPopupState by remember { mutableStateOf(false) }

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
                    .fillMaxHeight(0.7f),
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

            if (imageMoreThan5MB) {

                Text(
                    text = stringResource(id = R.string.image_more_than_5mb_msg),
                    fontFamily = font_sf_pro,
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    color = Color.Red,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(top = 3.dp, bottom = 3.dp)
                        .fillMaxWidth()
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
                    val encImage: String = Base64.encodeToString(b, Base64.NO_WRAP)
                    Log.d("ImageCaptureScreen", "Encoded image: $encImage")
                    val context = LocalContext.current
                    LaunchedEffect(key1 = true) {
                        scanningViewModel.analyzeExpense(context, encImage)
                    }
                }
                processClicked = false
            }
            when (scannedReceiptViewState) {
                is ReceiptScanningViewState.ReceiptScanningSuccess -> {
                    if (progressPopupState) {
                        progressPopupState = false
                        currentPopupState = ReceiptScanningBottomSheetType.POPUP_SCANNED_RECEIPT
                        openBottomSheet()
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