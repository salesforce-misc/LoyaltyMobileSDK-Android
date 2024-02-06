package com.salesforce.loyalty.mobile.myntorewards.utilities

import android.content.Context
import com.salesforce.loyalty.mobile.MyNTORewards.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoUnit

object DateUtils {

    const val DATE_FORMAT_YYYYMMDD = "yyyy-MM-dd"
    const val DATE_FORMAT_DDMMMMYYYY = "dd MMMM yyyy"

    fun datePeriod(startDate: String?, inputDateFormat: String = DATE_FORMAT_YYYYMMDD): DatePeriodType {
        if (startDate.isNullOrEmpty()) {
            return DatePeriodType.INVALID_DATE
        }
        val (startDateFormatted, today, period) = periodFromStartDateToToday(startDate, inputDateFormat)
        return when {
            startDateFormatted == today -> DatePeriodType.TODAY
            startDateFormatted == today.minusDays(1) -> DatePeriodType.YESTERDAY
            startDateFormatted == today.plusDays(1) -> DatePeriodType.TOMORROW
            period in 2..7 -> DatePeriodType.WITHIN7DAYS
            period in 8..30 -> DatePeriodType.WITHIN1MONTH
            period in 31..90 -> DatePeriodType.WITHIN3MONTHS
            else -> DatePeriodType.MORETHAN3MONTHS
        }
    }

    private fun periodFromStartDateToToday(startDate: String?, inputDateFormat: String): Triple<LocalDate, LocalDate, Long> {
        val dateFormatterInput = DateTimeFormatter.ofPattern(inputDateFormat)
        var startDateFormatted = LocalDate.parse(startDate, dateFormatterInput)
        val today = LocalDate.now()
        val period = ChronoUnit.DAYS.between(startDateFormatted, today)
        return Triple(startDateFormatted, today, period)
    }

    fun formatDate(context: Context, inputDate: String?, inputDateFormat: String = DATE_FORMAT_YYYYMMDD): String {
        val datePeriod = datePeriod(inputDate)
        val dateFormatterOutput = DateTimeFormatter.ofPattern(DATE_FORMAT_DDMMMMYYYY)
        val dateFormatterInput = DateTimeFormatter.ofPattern(inputDateFormat)
        val startDateFormatted = LocalDate.parse(inputDate, dateFormatterInput)
        return when(datePeriod) {
            DatePeriodType.TODAY -> context.getString(R.string.today_label)
            DatePeriodType.YESTERDAY -> context.getString(R.string.yesterday_label)
            DatePeriodType.WITHIN7DAYS -> context.getString(R.string.days_ago_label, periodFromStartDateToToday(inputDate, inputDateFormat).third.toString())
            else -> startDateFormatted.format(dateFormatterOutput)
        }
    }
}