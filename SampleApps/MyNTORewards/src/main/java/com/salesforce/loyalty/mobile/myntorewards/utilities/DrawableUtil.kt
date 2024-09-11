package com.salesforce.loyalty.mobile.myntorewards.utilities

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.provider.MediaStore.Images
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream


fun writeTextOnDrawable(context: Context, drawableId: Int, text: String) =
    writeTextOnDrawableInternal(context, drawableId, text, 25, -2, 0)

/**
 * Write the given text on top of the given image
 */
fun writeTextOnDrawableInternal(context: Context, drawableId: Int, text: String,
                                textSizeDp: Int, horizontalOffset: Int, verticalOffset: Int): BitmapDrawable {

    val bm = BitmapFactory.decodeResource(context.resources, drawableId)
        .copy(Bitmap.Config.ARGB_8888, true)

    val tf = Typeface.create("Helvetica", Typeface.BOLD)

    val paint = Paint().apply {
        style = Paint.Style.FILL
        color = Color.BLUE
        typeface = tf
        textAlign = Paint.Align.CENTER
        textSize = textSizeDp.toFloat()
    }

    val textRect = Rect()
    paint.getTextBounds(text, 0, text.length, textRect)

    val canvas = Canvas(bm)

    //If the text is bigger than the canvas , reduce the font size
    if (textRect.width() >= canvas.width - 4)
    //the padding on either sides is considered as 4, so as to appropriately fit in the text
        paint.textSize = 20f

    //Calculate the positions
    val xPos = canvas.width.toFloat()/2 + horizontalOffset

    //"- ((paint.descent() + paint.ascent()) / 2)" is the distance from the baseline to the center.
    val yPos = (canvas.height /*/ 2*/ - (paint.descent() + paint.ascent()) / 2) + verticalOffset

    canvas.drawText(text, xPos, 100f, paint)

    return BitmapDrawable(context.resources, bm)
}

/**
 * Bitmap to URI object
 */
fun getImageUri(context: Context, bmp: Bitmap?): Uri? {
    if (bmp != null) {
        val localUri = Uri.parse(
            MediaStore.Images.Media.insertImage(
                context.contentResolver, bmp, null, null
            )
        )
        if (localUri != null) {
            val cv = ContentValues()
            cv.put(
                MediaStore.Images.ImageColumns.DATE_TAKEN,
                java.lang.Long.valueOf(System.currentTimeMillis())
            )
            cv.put(
                MediaStore.Images.ImageColumns.DATE_ADDED,
                java.lang.Long.valueOf(System.currentTimeMillis())
            )
            context.contentResolver.update(
                localUri, cv, null,
                null
            )
        }
        bmp.recycle()
        return localUri
    }
    return null
}

fun bitmapToUri(context: Context, bmp: Bitmap): Uri? {
    var tempDir = Environment.getExternalStorageDirectory()
    tempDir = File(tempDir.absolutePath + "/.temp/")
    tempDir.mkdir()
    val tempFile = File.createTempFile("title", ".jpg", tempDir)
    val bytes = ByteArrayOutputStream()
    bmp.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
    val bitmapData = bytes.toByteArray()

    val fos = FileOutputStream(tempFile)
    fos.write(bitmapData)
    fos.flush()
    fos.close()
    return Uri.fromFile(tempFile)
}

fun getImageUriFromBitmap(context: Context, bitmap: Bitmap): Uri{
    val bytes = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
    val path: String =
        Images.Media.insertImage(context.contentResolver, bitmap, "Title", null)
    return Uri.parse(path.toString())
}