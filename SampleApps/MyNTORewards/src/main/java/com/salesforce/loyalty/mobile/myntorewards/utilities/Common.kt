package com.salesforce.loyalty.mobile.myntorewards.utilities

import android.content.Context
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.DEFAULT_SAMPLE_APP_FORMAT
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.KEY_APP_DATE
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.PROMOTION_DATE_API_FORMAT
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.RECEIPT_DATE_API_FORMAT
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.RECEIPT_DETAILS_API_DATETIME_FORMAT
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.RECEIPT_DETAILS_API_DATETIME_FORMAT2
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.TRANSACTION_HISTORY_DATETIME_FORMAT
import com.salesforce.loyalty.mobile.sources.loyaltyModels.MemberCurrency
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.*

class Common {
    companion object {

        fun formatPromotionDate(apiDate: String, context: Context): String {
            val apiDateFormat = DateTimeFormatter.ofPattern(PROMOTION_DATE_API_FORMAT)
            val date = LocalDate.parse(apiDate, apiDateFormat)
            val promotionDateFormat = DateTimeFormatter.ofPattern(getApplicationDateFormat(context))
            return date.format(promotionDateFormat)
        }
        fun formatReceiptListDate(apiDate: String, context: Context): String {
            val apiDateFormat = DateTimeFormatter.ofPattern(RECEIPT_DATE_API_FORMAT)
            val date = LocalDate.parse(apiDate, apiDateFormat)
            val promotionDateFormat = DateTimeFormatter.ofPattern(getApplicationDateFormat(context))
            return date.format(promotionDateFormat)
        }
        fun formatReceiptDetailDate(apiDate: String, context: Context): String {

            try{
                val apiDateFormat = DateTimeFormatter.ofPattern(PROMOTION_DATE_API_FORMAT)
                val date = LocalDate.parse(apiDate, apiDateFormat)
                val promotionDateFormat = DateTimeFormatter.ofPattern(getApplicationDateFormat(context))
                return date.format(promotionDateFormat)
            }
            catch (ee: DateTimeParseException)
            {
                try {
                    val apiDateFormat = DateTimeFormatter.ofPattern(RECEIPT_DETAILS_API_DATETIME_FORMAT2)
                    val date = LocalDate.parse(apiDate, apiDateFormat)
                    val promotionDateFormat = DateTimeFormatter.ofPattern(getApplicationDateFormat(context))
                    return date.format(promotionDateFormat)
                }
                catch (ee:Exception)
                {
                    return apiDate
                }

            }

        }

        fun getApplicationDateFormat(context: Context): String {
            val preferencesManager = DatePreferencesManager(context)
            return preferencesManager.getData(KEY_APP_DATE, DEFAULT_SAMPLE_APP_FORMAT)
        }


        fun formatTransactionDateTime(apiDateTime: String, context: Context): String {
            val apiDateFormat = DateTimeFormatter.ofPattern(TRANSACTION_HISTORY_DATETIME_FORMAT)
            val date = LocalDate.parse(apiDateTime, apiDateFormat)

            val promotionDateFormat = DateTimeFormatter.ofPattern(getApplicationDateFormat(context))
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

        fun voucherEmptyViewMsg(selectedTab: Int): Int {
          return when (selectedTab) {
                0 -> R.string.label_empty_vouchers
                1 -> R.string.label_empty_vouchers_redeem_tab
                2 -> R.string.label_empty_vouchers_expired_tab
                else -> R.string.label_empty_vouchers
            }
        }
    }

}