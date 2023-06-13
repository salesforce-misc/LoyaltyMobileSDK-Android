package com.salesforce.loyalty.mobile.sources.forceUtils

import android.util.Log
import com.salesforce.loyalty.mobile.sources.BuildConfig

object Logger {

    private val isDebug = BuildConfig.LOG_ENABLED

    fun d(tag: String, message: String): Int {
        if (isDebug) {
            Log.d(tag, message)
        }
        return 0
    }

    fun v(tag: String, message: String) {
        if (isDebug) {
            Log.v(tag, message)
        }
    }

    fun i(tag: String, message: String) {
        if (isDebug) {
            Log.i(tag, message)
        }
    }

    fun e(tag: String, message: String, throwable: Throwable?) {
        if (isDebug) {
            Log.e(tag, message, throwable)
        }
    }

}