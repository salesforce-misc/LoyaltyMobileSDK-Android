package com.salesforce.loyalty.mobile.myntorewards.views.myprofile

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.ColourBlackQR
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.ColourWhiteQR
import com.salesforce.loyalty.mobile.sources.forceUtils.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun QRCode(value: String, width: Int, height: Int, colour:Long= ColourBlackQR ) {

    val qrCodeBitmap = remember { mutableStateOf<ImageBitmap?>(null) }
    val scope = rememberCoroutineScope()

    val hints = mutableMapOf<EncodeHintType, Any>()
    hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
    hints.put(EncodeHintType.MARGIN, 0)


    LaunchedEffect(value) {
        scope.launch {
            withContext(Dispatchers.Default) {
                qrCodeBitmap.value = try {

                    createBitmap(
                        MultiFormatWriter().encode(
                            value,
                            BarcodeFormat.QR_CODE,
                            width,
                            height,
                            hints
                        ), colour
                    )?.asImageBitmap()
                } catch (e: Exception) {
                    Logger.e("ComposeBarcodes", "Invalid Barcode Format", e)
                    null
                }
            }
        }
    }

    qrCodeBitmap.value?.let { qrCode ->
        Image(
            painter = BitmapPainter(qrCode),
            contentDescription = stringResource(R.string.cd_onboard_screen_bottom_fade),
            modifier = Modifier
                .width(width.dp)
                .height(height.dp)
                .background(Color.Yellow),
            contentScale = ContentScale.FillWidth,
        )
    }

}

fun createBitmap(matrix: BitMatrix, colour:Long): Bitmap? {

    val width = matrix.width
    val height = matrix.height
    val pixels = IntArray(width * height)
    for (y in 0 until height) {
        val offset = y * width
        for (x in 0 until width) {
            pixels[offset + x] = (if (matrix[x, y]) colour else ColourWhiteQR).toInt()
        }
    }
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
    return bitmap
}
