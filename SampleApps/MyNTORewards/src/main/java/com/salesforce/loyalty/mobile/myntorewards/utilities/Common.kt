package com.salesforce.loyalty.mobile.myntorewards.utilities

import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.PROMOTION_DATE_API_FORMAT
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.PROMOTION_DATE_SAMPLE_APP_FORMAT
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.TRANSACTION_HISTORY_APP_DATE_FORMAT
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.TRANSACTION_HISTORY_DATETIME_FORMAT
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class Common {
    companion object {

        fun formatPromotionDate(apiDate: String): String {
            val apiDateFormat = DateTimeFormatter.ofPattern(PROMOTION_DATE_API_FORMAT)
            val date = LocalDate.parse(apiDate, apiDateFormat)
            val promotionDateFormat = DateTimeFormatter.ofPattern(PROMOTION_DATE_SAMPLE_APP_FORMAT)
            return date.format(promotionDateFormat)
        }

        fun formatTransactionDateTime(apiDateTime: String): String {
            val apiDateFormat = DateTimeFormatter.ofPattern(TRANSACTION_HISTORY_DATETIME_FORMAT)
            val date = LocalDate.parse(apiDateTime, apiDateFormat)

            val promotionDateFormat =
                DateTimeFormatter.ofPattern(TRANSACTION_HISTORY_APP_DATE_FORMAT)
            return date.format(promotionDateFormat)
        }

        fun isTransactionDateWithinCurrentMonth(apiDateTime: String): Boolean {
            val currentDate = LocalDate.now()
            val currentDateMinus30Days = currentDate.minusDays(30)
            val apiDateFormat = DateTimeFormatter.ofPattern(TRANSACTION_HISTORY_DATETIME_FORMAT)
            val date = LocalDate.parse(apiDateTime, apiDateFormat)
            if (date.isAfter(currentDateMinus30Days)) {
                return true
            }
            return false
        }
    }

}