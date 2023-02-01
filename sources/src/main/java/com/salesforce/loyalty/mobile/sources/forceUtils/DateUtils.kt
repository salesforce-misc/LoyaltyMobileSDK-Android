package com.salesforce.loyalty.mobile.sources.forceUtils

import java.text.SimpleDateFormat
import java.util.Calendar

/**
 * Class which holds utility methods related to Date.
 */
object DateUtils {

    /**
     * Get current date in "2022-01-01T00:00:00" format
     *
     * @return Current date
     */
    fun getCurrentDateInYYYYMMDDTHHMMSS(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        return sdf.format(Calendar.getInstance().timeInMillis)
    }
}