package com.salesforce.loyalty.mobile.myntorewards.utilities

import android.os.Build
import androidx.annotation.RequiresApi
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.PROMOTION_DATE_API_FORMAT
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.PROMOTION_DATE_SAMPLE_APP_FORMAT
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class Common {
    companion object  {

        fun formatPromotionDate(apiDate: String): String {
            val apiDateFormat = DateTimeFormatter.ofPattern(PROMOTION_DATE_API_FORMAT)
            val date = LocalDate.parse(apiDate, apiDateFormat)
            val promotionDateFormat = DateTimeFormatter.ofPattern(PROMOTION_DATE_SAMPLE_APP_FORMAT)
            return date.format(promotionDateFormat)
        }
    }

}