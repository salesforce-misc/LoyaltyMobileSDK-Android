package com.salesforce.loyalty.mobile.myntorewards.utilities

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import com.salesforce.loyalty.mobile.MyNTORewards.R


/**
 * Copy the given text to Clipboard
 */
fun Context.copyToClipboard(text: String, label: String = "label") {
    val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText(label, text)
    clipboardManager.setPrimaryClip(clip)
}

fun Context.shareReferralCode(code: String, shareType: ShareType) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, getString(R.string.share_referral_message, code))
    }
    when (shareType) {
        ShareType.FACEBOOK -> {
            intent.setPackage("com.facebook")
        }
        ShareType.INSTAGRAM -> {
            intent.setPackage("com.instagram.android")
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
    startActivity(Intent.createChooser(intent, "Share Referral Code"))
}

fun isAppInstalled(context: Context, packageName: String): Boolean {
    return try {
        context.packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
        true
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }
}