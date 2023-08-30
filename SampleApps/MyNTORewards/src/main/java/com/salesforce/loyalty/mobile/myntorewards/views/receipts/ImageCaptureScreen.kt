package com.salesforce.loyalty.mobile.myntorewards.views.receipts

import android.Manifest
import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.util.Base64
import android.util.Log
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Logger
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState

import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.LighterBlack
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.MyProfileScreenBG
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.VibrantPurple40
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.font_sf_pro
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants
import com.salesforce.loyalty.mobile.myntorewards.utilities.ReceiptScanningBottomSheetType
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_CAMERA
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_CAMERA_SCREEN
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_IMAGE_PREVIEW_SCREEN
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint.ScanningViewModelInterface
import com.salesforce.loyalty.mobile.myntorewards.views.navigation.MoreScreens
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

@RequiresApi(Build.VERSION_CODES.P)
@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterialApi::class)
@Composable
fun ImageCaptureScreen(
    navController: NavHostController,
    scanningViewModel: ScanningViewModelInterface
) {
    var capturedImageBitmap by remember { mutableStateOf(ImageBitmap(60, 60)) }
    var imageClicked by remember { mutableStateOf(false) }
    var imageMoreThan5MB by remember { mutableStateOf(false) }

    val scannedReceiptLiveData by scanningViewModel.scannedReceiptLiveData.observeAsState()
    val scannedReceiptViewState by scanningViewModel.receiptScanningViewStateLiveData.observeAsState()

    // Camera permission state
    val cameraPermissionState = rememberPermissionState(
        Manifest.permission.CAMERA
    )



    if (!imageClicked) {
        when (cameraPermissionState.status) {
            // If the camera permission is granted, then show screen with the feature enabled
            PermissionStatus.Granted -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag(TEST_TAG_CAMERA_SCREEN)
                ) {
                    CameraContent(navController, scanningViewModel) {
                        imageClicked = true
                        capturedImageBitmap = it.asImageBitmap()
                        // Encoding the image to Base 64 and sending it to Analyze API.
                        val baos = ByteArrayOutputStream()
                        it.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                        val b: ByteArray = baos.toByteArray()
                        val length= (b.size/1024)
                        imageMoreThan5MB = length>(5*1024)
                    }
                }
            }

            is PermissionStatus.Denied -> {

                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Spacer(modifier = Modifier.height(200.dp))
                    Text(
                        text = stringResource(id = R.string.permission_message),
                        fontFamily = font_sf_pro,
                        color = Color.Black,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(start = 32.dp, end = 32.dp),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(200.dp))
                    Button(
                        modifier = Modifier
                            .fillMaxWidth(), onClick = {
                            cameraPermissionState.launchPermissionRequest()
                        },
                        colors = ButtonDefaults.buttonColors(VibrantPurple40),
                        shape = RoundedCornerShape(100.dp)

                    ) {
                        Text(
                            text = stringResource(id = R.string.request_permission),
                            fontFamily = font_sf_pro,
                            textAlign = TextAlign.Center,
                            fontSize = 16.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(top = 3.dp, bottom = 3.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(200.dp))
                }
            }
        }
    } else {

        var currentPopupState: ReceiptScanningBottomSheetType? by remember { mutableStateOf(null) }
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
            imageClicked = false
        }
        BottomSheetScaffold(
            scaffoldState = bottomSheetScaffoldState,

            sheetContent = {
                Spacer(modifier = Modifier.height(1.dp))
                currentPopupState?.let { bottomSheetType ->
                    OpenReceiptBottomSheetContent(
                        bottomSheetType = bottomSheetType,
                        navController = navController,
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
                    .testTag(TEST_TAG_IMAGE_PREVIEW_SCREEN)
            ) {
                var progressPopupState by remember { mutableStateOf(false) }
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
                            imageClicked = false
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

                if(imageMoreThan5MB){

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
                            progressPopupState = true
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
                                imageClicked = false
                            },
                        textAlign = TextAlign.Center,
                        fontSize = 16.sp,
                        color = LighterBlack,
                        fontWeight = FontWeight.Normal,

                        )
                }

                if (progressPopupState) {
                    currentPopupState = ReceiptScanningBottomSheetType.POPUP_PROGRESS
                    openBottomSheet()
                    progressPopupState = false

                    // Encoding the image to Base 64 and sending it to Analyze API.
                    val baos = ByteArrayOutputStream()
                    capturedImageBitmap?.asAndroidBitmap()?.let {
                        it.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                        val b: ByteArray = baos.toByteArray()
                        val encImage: String = Base64.encodeToString(b, Base64.NO_WRAP)
                        Log.d("ImageCaptureScreen", "Encoded image: $encImage")
                        scanningViewModel.analyzeExpense(encImage)
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.P)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
private fun CameraContent(
    navController: NavHostController,
    scanningViewModel: ScanningViewModelInterface,
    onPhotoCaptured: (Bitmap) -> Unit
) {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraController = remember { LifecycleCameraController(context) }
    val mainExecutor = ContextCompat.getMainExecutor(context)
    var isFlashEnabled by remember { mutableStateOf(false) }
    if (isFlashEnabled) {
        //cameraController.enableTorch(true)//incase the auto flash does not work after testing
        cameraController.imageCaptureFlashMode = ImageCapture.FLASH_MODE_AUTO
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                Image(
                    painter = painterResource(R.drawable.shutter),
                    alignment = Alignment.BottomCenter,
                    contentDescription = stringResource(id = R.string.cd_shutter_button),
                    modifier = Modifier.clickable {

                        //kept it for testing purpose of future.
                        /*  Log.d("Torch", ""+cameraController.cameraInfo?.hasFlashUnit())
                        cameraController.imageCaptureFlashMode = ImageCapture.FLASH_MODE_AUTO
                          cameraController.enableTorch(true)*/
                        cameraController.takePicture(
                            mainExecutor,
                            object : ImageCapture.OnImageCapturedCallback() {
                                override fun onCaptureSuccess(image: ImageProxy) {
                                    cameraController.unbind()
                                    val correctedBitmap: Bitmap = image
                                        .toBitmap()
                                        .rotateBitmap(image.imageInfo.rotationDegrees)

                                    onPhotoCaptured(correctedBitmap)

                                    image.close()

                                }

                                override fun onError(exception: ImageCaptureException) {
                                    Log.e("CameraContent", "Error capturing image", exception)
                                }
                            })
                    },
                    contentScale = ContentScale.FillWidth
                )
            }


        }
    ) { paddingValues: PaddingValues ->

        Box() {
            var imageUri by remember {
                mutableStateOf<Uri?>(Uri.EMPTY)
            }
            var flashColour by remember { mutableStateOf(Color.White) }
            val launcher = rememberLauncherForActivityResult(
                contract =
                ActivityResultContracts.GetContent()
            ) { uri: Uri? ->
                imageUri = uri
            }
            val bitmap = remember {
                mutableStateOf<Bitmap?>(null)
            }

            if (imageUri?.path?.isNotEmpty() == true) {
                imageUri?.let {
                    val source = ImageDecoder
                        .createSource(context.contentResolver, it)
                    bitmap.value = ImageDecoder.decodeBitmap(source)
                }
                bitmap.value?.let { onPhotoCaptured(it) }

            }

            AndroidView(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .testTag(TEST_TAG_CAMERA),
                factory = { context ->
                    PreviewView(context).apply {
                        layoutParams = LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        setBackgroundColor(android.graphics.Color.BLACK)
                        implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                        scaleType = PreviewView.ScaleType.FILL_CENTER
                    }.also { previewView ->
                        previewView.controller = cameraController
                        cameraController.bindToLifecycle(lifecycleOwner)
                    }
                }
            )
            Image(
                painter = painterResource(id = R.drawable.white_back_button),
                contentDescription = stringResource(id = R.string.cd_camera_back_button),
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .padding(start = 10.dp, top = 50.dp, bottom = 10.dp)
                    .width(32.dp)
                    .height(32.dp)
                    .align(Alignment.TopStart)
                    .clickable {
                        navController.popBackStack()
                    }
            )
            Image(
                painter = painterResource(id = R.drawable.flash_icon),
                contentDescription = stringResource(id = R.string.cd_camera_flash_icon),
                colorFilter = ColorFilter.tint(flashColour),
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .padding(start = 10.dp, top = 50.dp, bottom = 10.dp, end = 22.dp)
                    .width(32.dp)
                    .height(32.dp)
                    .align(Alignment.TopEnd)
                    .clickable {

                        if (isFlashEnabled) {
                            flashColour = Color.White
                            isFlashEnabled = false
                        } else {
                            flashColour = Color.Green
                            isFlashEnabled = true
                        }

                    }
            )


            Column(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                    .align(Alignment.BottomCenter)
            ) {
                Text(
                    text = stringResource(id = R.string.camera_take_photo_text),
                    style = TextStyle(
                        fontSize = 18.sp,
                        lineHeight = 25.2.sp,
                        fontFamily = font_sf_pro,
                        fontWeight = FontWeight.Normal,
                        color = Color.White,
                    )
                )
                Spacer(modifier = Modifier.height(100.dp))

            }


            Image(
                painter = painterResource(R.drawable.image_gallery),
                contentDescription = stringResource(id = R.string.cd_gallery_icon),
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                    .align(Alignment.BottomStart)
                    .width(55.dp)
                    .height(55.dp)
                    .clickable {
                        launcher.launch("image/*")

                    },
                contentScale = ContentScale.FillWidth
            )


        }

    }
}

fun Bitmap.rotateBitmap(rotationDegrees: Int): Bitmap {
    val matrix = Matrix().apply {
        postRotate(-rotationDegrees.toFloat())
        postScale(-1f, -1f)
    }

    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}

@Composable
fun OpenReceiptBottomSheetContent(
    bottomSheetType: ReceiptScanningBottomSheetType,
    navController: NavHostController,
    setBottomSheetState: (bottomSheetState: ReceiptScanningBottomSheetType) -> Unit,
    closeSheet: () -> Unit,
) {
    when (bottomSheetType) {
        ReceiptScanningBottomSheetType.POPUP_PROGRESS -> {
            ScanningProgress(
                navHostController = navController,
                closePopup = { closeSheet() }) {
                setBottomSheetState(it)
            }
        }

        ReceiptScanningBottomSheetType.POPUP_SCANNED_RECEIPT -> {
            ShowScannedReceiptScreen(navController, closePopup = {
                closeSheet()
            }, openCongratsPopup = {
                setBottomSheetState(it)
            })
        }

        ReceiptScanningBottomSheetType.POPUP_CONGRATULATIONS -> {
            CongratulationsPopup(navController, closePopup = {
                closeSheet()
                navController.popBackStack(MoreScreens.ReceiptListScreen.route, false)
            }, scanAnotherReceipt = { closeSheet() })
        }
    }
}
