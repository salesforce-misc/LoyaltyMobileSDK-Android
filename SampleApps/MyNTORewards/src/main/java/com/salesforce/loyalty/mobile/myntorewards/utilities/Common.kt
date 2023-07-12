package com.salesforce.loyalty.mobile.myntorewards.utilities

import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.PROMOTION_DATE_API_FORMAT
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.PROMOTION_DATE_SAMPLE_APP_FORMAT
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.TRANSACTION_HISTORY_APP_DATE_FORMAT
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.TRANSACTION_HISTORY_DATETIME_FORMAT
import com.salesforce.loyalty.mobile.sources.loyaltyModels.MemberCurrency
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

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

        fun getCurrencyPointBalance(currencyList: List<MemberCurrency>): Double? {
            for (currency in currencyList) {
                if (currency.loyaltyMemberCurrencyName?.uppercase() == AppConstants.REWARD_CURRENCY_NAME.uppercase()) {
                    return currency.pointsBalance
                }
            }
            return null
        }
        fun isEndDateExpired(endDateString: String?): Boolean {
            try {
                val currentDate = Date()
                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val endDate = endDateString?.let { sdf.parse(it) }

               /* adding 24 hour to end date as date coming from server is
                having only date not time. and by default its taking time as 00:00
                hence comparison was getting failed as time of start date was hight than time of end date
                incase of same date*/

                if (endDate != null) {
                    endDate.time= endDate.time + (24 * 60 * 60 * 1000)
                }
                if (currentDate.after(endDate)) {
                    return true
                }
                return false
            } catch (e: Exception) {
                return false
            }
        }
        fun isWithinMentionedDay(apiDateTime: String, withinDay: Long): Boolean {
            val currentDate = LocalDate.now()
            val currentDateMinus30Days = currentDate.minusDays(withinDay)

            val apiFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val apiDate = apiDateTime?.let { LocalDate.parse(it, apiFormatter) }

            if (apiDate?.isBefore(currentDateMinus30Days) == true) {
                return false
            }
            return true
        }
    }

}