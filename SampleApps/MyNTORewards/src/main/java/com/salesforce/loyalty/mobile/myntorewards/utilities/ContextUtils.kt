package com.salesforce.loyalty.mobile.myntorewards.utilities

import android.content.ClipData
import android.content.ClipboardManager
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.annotation.AnyRes
import com.salesforce.loyalty.mobile.MyNTORewards.R
import java.io.ByteArrayOutputStream
import java.io.UnsupportedEncodingException
import java.net.URLEncoder


const val LOG_TAG = "ContextUtils"

/**
 * Copy the given text to Clipboard
 */
fun Context.copyToClipboard(text: String, label: String = "label") {
    val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText(label, text)
    clipboardManager.setPrimaryClip(clip)
}

fun Context.shareReferralCode(content: String, shareType: ShareType) {
    val extraText = getString(R.string.share_referral_message, content)
    var intent = Intent(Intent.ACTION_SEND).apply {
//        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, extraText)
        putExtra(Intent.EXTRA_TITLE, extraText)
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
    }

    intent.type = "image/*"
    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    val bitmap = writeTextOnDrawable(R.drawable.bg_refer_earn, extraText).bitmap
    intent.putExtra(Intent.EXTRA_STREAM, getImageUri(this, bitmap))
    intent.putExtra(Intent.EXTRA_TEXT, extraText)
    intent.putExtra(Intent.EXTRA_TITLE, extraText)

    var sharerUrl: String? = null

    when (shareType) {
        ShareType.FACEBOOK -> {
            intent.setPackage("com.facebook.katana")
            sharerUrl = "https://m.facebook.com/composer/"
        }
        ShareType.INSTAGRAM -> {
            intent.setPackage("com.instagram.android")
//            intent.type = "image/*"
//            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//            val bitmap = writeTextOnDrawable(R.drawable.bg_refer_earn, extraText).bitmap
//            intent.putExtra(Intent.EXTRA_STREAM, getImageUri(this, bitmap))
//            intent.putExtra(Intent.EXTRA_STREAM, getUriToResource(R.drawable.bg_refer_friend_banner))
        }
        ShareType.WHATSAPP -> {
            intent.setPackage("com.whatsapp")
        }
        ShareType.TWITTER -> {
            intent.setPackage("com.twitter.android")
            sharerUrl = "https://twitter.com/intent/tweet?text=${urlEncode(extraText)}"
        }
        ShareType.SHARE_OTHERS -> {
            // do nothing
        }
    }

    if (sharerUrl.isNullOrBlank().not() && !isAppInstalled(intent.`package` ?: "")) {
        intent = Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl))
        Toast.makeText(this, "Selected app is not installed, redirecting to web page!", Toast.LENGTH_LONG).show()
    }
    startActivity(Intent.createChooser(intent, "Share Referral Code"))
}

fun Context.isAppInstalled(packageName: String): Boolean {
    return try {
        packageManager.getApplicationInfo(packageName, 0)
        true
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }
}

/**
 * get uri to any resource type Via Context Resource instance
 * @param context - context
 * @param resId - resource id
 * @throws Resources.NotFoundException if the given ID does not exist.
 * @return - Uri to resource by given id
 */
@Throws(Resources.NotFoundException::class)
fun Context.getUriToResource(
    @AnyRes resId: Int
): Uri {
    return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + resources.getResourcePackageName(resId) + '/' + resources.getResourceTypeName(resId) + '/' + resources.getResourceEntryName(resId) )
}

/**
 * Convert to UTF-8 text to put it on url format
 *
 * @param s text to be converted
 * @return text on UTF-8 format
 */
fun urlEncode(s: String): String? {
    return try {
        URLEncoder.encode(s, "UTF-8")
    } catch (e: UnsupportedEncodingException) {
        Log.e(LOG_TAG, "UTF-8 should always be supported", e)
        throw RuntimeException("URLEncoder.encode() failed for $s")
    }
}

fun Context.writeTextOnDrawable(drawableId: Int, text: String) =
    DrawableUtil.writeTextOnDrawableInternal(this, drawableId, text, 25, -2, 0)

object DrawableUtil {

    fun writeTextOnDrawableInternal(context: Context, drawableId: Int, text: String,
                                    textSizeDp: Int, horizontalOffset: Int, verticalOffset: Int): BitmapDrawable {

        val bm = BitmapFactory.decodeResource(context.resources, drawableId)
            .copy(Bitmap.Config.ARGB_8888, true)

        val tf = Typeface.create("Helvetica", Typeface.BOLD)

        val paint = Paint()
        paint.style = Paint.Style.FILL
        paint.color = Color.BLUE
        paint.typeface = tf
        paint.textAlign = Paint.Align.CENTER
        paint.textSize = 50f

        val textRect = Rect()
        paint.getTextBounds(text, 0, text.length, textRect)

        val canvas = Canvas(bm)

        //If the text is bigger than the canvas , reduce the font size
        if (textRect.width() >= canvas.getWidth() - 4)
        //the padding on either sides is considered as 4, so as to appropriately fit in the text
            paint.textSize = 20f

        //Calculate the positions
        val xPos = canvas.width.toFloat()/2 + horizontalOffset

        //"- ((paint.descent() + paint.ascent()) / 2)" is the distance from the baseline to the center.
        val yPos = (canvas.height /*/ 2*/ - (paint.descent() + paint.ascent()) / 2) + verticalOffset

        canvas.drawText(text, xPos, 100f, paint)

        return BitmapDrawable(context.resources, bm)
    }
}

/*fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
    val bytes = ByteArrayOutputStream()
    inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
    val path = MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
    return Uri.parse(path)
}*/

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