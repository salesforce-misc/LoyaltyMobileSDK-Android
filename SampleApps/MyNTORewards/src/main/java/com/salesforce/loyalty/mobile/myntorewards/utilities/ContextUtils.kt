package com.salesforce.loyalty.mobile.myntorewards.utilities

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import com.salesforce.loyalty.mobile.MyNTORewards.R
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
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, extraText)
    }
    var sharerUrl: String? = null

    when (shareType) {
        ShareType.FACEBOOK -> {
            intent.setPackage("com.facebook")
            sharerUrl = "https://www.facebook.com/sharer/sharer.php?u=${urlEncode(extraText)}"
        }
        ShareType.INSTAGRAM -> {
            intent.setPackage("com.instagram.android")
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

    if (!isAppInstalled(intent.`package` ?: "")) {
        intent = Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl))
    }
    startActivity(Intent.createChooser(intent, "Share Referral Code"))
}

fun Context.isAppInstalled(packageName: String): Boolean {
    return try {
        packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
        true
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }
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