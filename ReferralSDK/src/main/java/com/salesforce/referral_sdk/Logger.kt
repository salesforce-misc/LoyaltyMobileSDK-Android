package com.salesforce.referral_sdk

import android.util.Log

/**
 * Logger framework class with standard logging functions.
 */
object Logger {

    private val isDebug = BuildConfig.LOG_ENABLED

    /**
     * Add Debug log
     * @param tag Tag can be used to specify class name,
     * @param message Message can be given as any custom value for which log is called
     */
    fun d(tag: String, message: String) {
        if (isDebug) {
            Log.d(tag, message)
        }
    }

    /**
     * Add Verbose log
     * @param tag Tag can be used to specify class name,
     * @param message Message can be given as any custom value for which log is called
     */
    fun v(tag: String, message: String) {
        if (isDebug) {
            Log.v(tag, message)
        }
    }

    /**
     * Add Information log
     * @param tag Tag can be used to specify class name,
     * @param message Message can be given as any custom value for which log is called
     */
    fun i(tag: String, message: String) {
        if (isDebug) {
            Log.i(tag, message)
        }
    }

    /**
     * Add Error log
     * @param tag Tag can be used to specify class name,
     * @param message Message can be given as any custom value for which log is called
     * @param throwable Throwable
     */
    fun e(tag: String, message: String, throwable: Throwable?) {
        if (isDebug) {
            Log.e(tag, message, throwable)
        }
    }

}