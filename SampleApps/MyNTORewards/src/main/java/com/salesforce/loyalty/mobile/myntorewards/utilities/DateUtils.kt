package com.salesforce.loyalty.mobile.myntorewards.utilities

import android.content.Context
import com.salesforce.loyalty.mobile.MyNTORewards.R
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

object DateUtils {

    private const val DATE_FORMAT_YYYYMMDD = "yyyy-MM-dd"
    private const val DATE_FORMAT_DDMMMMYYYY = "dd MMMM yyyy"
    private val dateFormatterInput = DateTimeFormatter.ofPattern(DATE_FORMAT_YYYYMMDD)

    fun datePeriod(startDate: String?): DatePeriodType {
        if (startDate.isNullOrEmpty()) {
            return DatePeriodType.TODAY
        }
        val (startDateFormatted, today, period) = periodFromStartDateToToday(startDate)
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

    private fun periodFromStartDateToToday(startDate: String?): Triple<LocalDate, LocalDate, Long> {
        val startDateFormatted = LocalDate.parse(startDate, dateFormatterInput)
        val today = LocalDate.now()
        val period = ChronoUnit.DAYS.between(startDateFormatted, today)
        return Triple(startDateFormatted, today, period)
    }

    fun formatDate(context: Context, inputDate: String?): String {
        val datePeriod = datePeriod(inputDate)
        val dateFormatterOutput = DateTimeFormatter.ofPattern(DATE_FORMAT_DDMMMMYYYY)
        val startDateFormatted = LocalDate.parse(inputDate, dateFormatterInput)
        return when(datePeriod) {
            DatePeriodType.TODAY -> context.getString(R.string.today_label)
            DatePeriodType.YESTERDAY -> context.getString(R.string.yesterday_label)
            DatePeriodType.WITHIN7DAYS -> context.getString(R.string.days_ago_label, periodFromStartDateToToday(inputDate).third.toString())
            else -> startDateFormatted.format(dateFormatterOutput)
        }
    }
}