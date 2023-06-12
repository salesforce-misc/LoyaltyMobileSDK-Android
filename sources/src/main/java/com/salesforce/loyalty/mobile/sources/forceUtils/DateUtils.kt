/*
 * Copyright (c) 2023, Salesforce, Inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.salesforce.loyalty.mobile.sources.forceUtils

import java.text.SimpleDateFormat
import java.util.Calendar

/**
 * Class which holds utility methods related to Date.
 */
object DateUtils {

    const val TAG = "DateUtils"
    const val DATE_FORMAT_YYYYMMDDTHHMMSS = "yyyy-MM-dd'T'HH:mm:ss"
    /**
     * Get current date in "2022-01-01T00:00:00" format
     *
     * @param format Format in which the Date Time value should be returned
     * @return Current date and time in specified format
     */
    fun getCurrentDateTime(format: String): String? {
        return try {
            val sdf = SimpleDateFormat(format)
            sdf.format(Calendar.getInstance().timeInMillis)
        } catch (e: java.lang.Exception) {
            Logger.e(TAG, "Exception occurred when retrieving current date time in $format", e)
            null
        }
    }
}