package com.salesforce.loyalty.mobile.myntorewards.views.receipts

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_CAMERA_SCREEN
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint.ScanningViewModelInterface
import com.salesforce.loyalty.mobile.myntorewards.views.myprofile.CameraGalleryPickerAndUI

@RequiresApi(Build.VERSION_CODES.P)

@Composable
fun ImageCaptureScreen(
    navController: NavHostController,
    scanningViewModel: ScanningViewModelInterface
) {
    var capturedImageBitmap by remember { mutableStateOf(ImageBitmap(60, 60)) }
    var imageClicked by remember { mutableStateOf(false) }


    if (!imageClicked) {
        ImageCaptureCameraScreen(navController) {
            imageClicked = true
            capturedImageBitmap = it.asImageBitmap()
        }
    } else {
        ImagePreviewScreen(
            navController,
            scanningViewModel,
            capturedImageBitmap
        ) {
            imageClicked = it
        }
    }
}


@RequiresApi(Build.VERSION_CODES.P)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ImageCaptureCameraScreen(navController: NavHostController, onPhotoCaptured: (Bitmap) -> Unit) {
    // Camera permission state
    val cameraPermissionState = rememberPermissionState(
        Manifest.permission.CAMERA
    )
    when (cameraPermissionState.status) {
        // If the camera permission is granted, then show screen with the feature enabled
        PermissionStatus.Granted -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag(TEST_TAG_CAMERA_SCREEN)
            ) {
                CameraContent(navController) {
                    onPhotoCaptured(it)
                }
            }
        }

        is PermissionStatus.Denied -> {
            CameraPermissionScreen {
                cameraPermissionState.launchPermissionRequest()
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.P)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
private fun CameraContent(
    navController: NavHostController,
    onPhotoCaptured: (Bitmap) -> Unit
) {

    val context = LocalContext.current
    val cameraController = remember { LifecycleCameraController(context) }
    var isFlashEnabled by remember { mutableStateOf(false) }
    val lifecycleOwner = LocalLifecycleOwner.current
    if (isFlashEnabled) {
        //cameraController.enableTorch(true)//incase the auto flash does not work after testing
        cameraController.imageCaptureFlashMode = ImageCapture.FLASH_MODE_AUTO
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            CameraImageClickButton(cameraController, context) {
                onPhotoCaptured(it)
            }
        }
    ) {
        CameraGalleryPickerAndUI(navController, cameraController, context, lifecycleOwner, { capturedPic ->
            cameraController.unbind()
//            (ProcessCameraProvider.unbindAll()
            onPhotoCaptured(capturedPic)
        }, { flashEnableStatus ->
            isFlashEnabled = flashEnableStatus
        })
    }
}


@Composable
fun CameraImageClickButton(
    cameraController: LifecycleCameraController,
    context: Context,
    onPhotoCaptured: (Bitmap) -> Unit
) {
    val mainExecutor = ContextCompat.getMainExecutor(context)
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

fun Bitmap.rotateBitmap(rotationDegrees: Int): Bitmap {
    val matrix = Matrix().apply {
        postRotate(-rotationDegrees.toFloat())
        postScale(-1f, -1f)
    }

    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}
