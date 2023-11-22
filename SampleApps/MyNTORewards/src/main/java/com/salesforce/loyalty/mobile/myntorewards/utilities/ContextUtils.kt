package com.salesforce.loyalty.mobile.myntorewards.utilities

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context

/**
 * Copy the given text to Clipboard
 */
fun Context.copyToClipboard(text: String, label: String = "label") {
    val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText(label, text)
    clipboardManager.setPrimaryClip(clip)
}