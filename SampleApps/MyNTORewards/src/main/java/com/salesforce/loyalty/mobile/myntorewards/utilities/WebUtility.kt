package com.salesforce.loyalty.mobile.myntorewards.utilities

import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.webkit.CookieManager
import androidx.core.content.ContextCompat.getSystemService


object WebUtility {

    fun clearCookies(context: Context?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            CookieManager.getInstance().removeAllCookies(null)
            CookieManager.getInstance().flush()
        }
    }

    fun hideKeyboard(activity: Activity) {
        val inputMethodManager: InputMethodManager? =
            activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager?
        inputMethodManager?.hideSoftInputFromWindow(activity.window.decorView.windowToken, 0)
    }
}