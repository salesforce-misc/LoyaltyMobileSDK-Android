package com.salesforce.loyalty.mobile.myntorewards.utilities

import android.content.Context
import android.os.Build
import android.webkit.CookieManager

object WebUtility {

    fun clearCookies(context: Context?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            CookieManager.getInstance().removeAllCookies(null)
            CookieManager.getInstance().flush()
        }
    }
}