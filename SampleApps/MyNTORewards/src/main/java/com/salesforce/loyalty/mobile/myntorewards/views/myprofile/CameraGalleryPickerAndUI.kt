package com.salesforce.loyalty.mobile.myntorewards.views.myprofile

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavHostController
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.font_sf_pro
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun CameraGalleryPickerAndUI(navController: NavHostController, cameraController: LifecycleCameraController, context: Context, lifecycleOwner: LifecycleOwner, onPhotoCaptured: (Bitmap) -> Unit, flashEnabled: (Boolean)-> Unit)
{
    var isFlashEnabled by remember { mutableStateOf(false) }

//    val lifecycleOwner = LocalLifecycleOwner.current
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
            try {
                imageUri?.let {
                    val source = ImageDecoder
                        .createSource(context.contentResolver, it)
                    bitmap.value = ImageDecoder.decodeBitmap(source)
                }
                bitmap.value?.let {
                cameraController.unbind()
                    onPhotoCaptured(it)
                }
            }
            catch (imageDecodeException: ImageDecoder.DecodeException){
                val errorMsg= stringResource(id = R.string.not_supported_image_msg)
                imageDecodeException.message?.let { Log.d("ImageCaptureScreen: ", it) }
                Toast.makeText(
                    LocalContext.current,
                    errorMsg,
                    Toast.LENGTH_LONG
                ).show()
            }
            catch (exception: Exception)
            {
                val errorMsg= stringResource(id = R.string.not_supported_image_msg)
                exception.message?.let { Log.d("ImageCaptureScreen: ", it) }
                Toast.makeText(
                    LocalContext.current,
                    errorMsg,
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .testTag(TestTags.TEST_TAG_CAMERA),
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
                    cameraController.unbind()
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
                        flashEnabled(false)
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