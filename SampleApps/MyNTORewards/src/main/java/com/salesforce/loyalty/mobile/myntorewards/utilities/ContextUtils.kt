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
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.annotation.AnyRes
import com.salesforce.loyalty.mobile.MyNTORewards.R
import java.io.UnsupportedEncodingException
import java.net.URLEncoder


const val LOG_TAG = "ContextUtils"
const val INTENT_TYPE_TEXT = "text/plain"
const val INTENT_TYPE_IMAGE = "image/*"
const val INTENT_TYPE_MAIL = "message/rfc822"

/**
 * Copy the given text to Clipboard
 */
fun Context.copyToClipboard(text: String, label: String = "label") {
    val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText(label, text)
    clipboardManager.setPrimaryClip(clip)
}

fun isValidEmail(target: CharSequence?): Boolean {
    return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
}

fun Context.sendMail(emails: List<String>, subject: String, body: String) {
    if (emails.isEmpty() || emails.any { !isValidEmail(it.trim()) }) {
        Toast.makeText(this, "Please enter valid E-mails!", Toast.LENGTH_LONG).show()
        return
    }
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "message/rfc822"
        putExtra(Intent.EXTRA_SUBJECT, subject);
        putExtra(Intent.EXTRA_TEXT, body);
        putExtra(Intent.EXTRA_EMAIL, emails.toTypedArray())
    }
    startActivity(Intent.createChooser(intent, "Share Referral Code"))
}

fun Context.shareReferralCode(content: String, shareType: ShareType) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = INTENT_TYPE_TEXT
        putExtra(Intent.EXTRA_TEXT, content)
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
    }

    when (shareType) {
        ShareType.FACEBOOK -> {
            intent.setPackage("com.facebook.katana")
        }
        ShareType.INSTAGRAM -> {
            intent.also {
                it.setPackage("com.instagram.android")
                it.type = INTENT_TYPE_IMAGE
                it.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                val bitmap = writeTextOnDrawable(this, R.drawable.bg_refer_earn, content).bitmap
                intent.putExtra(Intent.EXTRA_STREAM, getImageUri(this, bitmap))
                it.putExtra(Intent.EXTRA_TEXT, content)
            }
        }
        ShareType.WHATSAPP -> {
            intent.setPackage("com.whatsapp")
        }
        ShareType.TWITTER -> {
            intent.setPackage("com.twitter.android")
        }
        ShareType.SHARE_OTHERS -> {
            // do nothing
        }
    }

    if (!isAppInstalled(intent.`package` ?: "")) {
        Toast.makeText(this, "Selected app is not installed!", Toast.LENGTH_LONG).show()
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