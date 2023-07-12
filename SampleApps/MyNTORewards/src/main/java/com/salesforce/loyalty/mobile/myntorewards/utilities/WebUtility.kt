package com.salesforce.loyalty.mobile.myntorewards.utilities

import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.inputmethod.InputMethodManager
import android.webkit.CookieManager


object WebUtility {

    fun clearCookies() {
        CookieManager.getInstance().removeAllCookies(null)
        CookieManager.getInstance().flush()
    }

    fun hideKeyboard(activity: Activity) {
        val inputMethodManager: InputMethodManager? =
            activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager?
        inputMethodManager?.hideSoftInputFromWindow(activity.window.decorView.windowToken, 0)
    }
}