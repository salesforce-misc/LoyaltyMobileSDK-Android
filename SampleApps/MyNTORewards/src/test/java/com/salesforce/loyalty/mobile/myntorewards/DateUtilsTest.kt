package com.salesforce.loyalty.mobile.myntorewards

import android.content.Context
import com.salesforce.loyalty.mobile.myntorewards.utilities.DatePeriodType
import com.salesforce.loyalty.mobile.myntorewards.utilities.DateUtils
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import java.text.SimpleDateFormat
import java.util.Calendar

class DateUtilsTest {

    @Mock
    private val mockApplicationContext: Context? = null

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    fun `Given today's date, when calculating date period, then verify Date Period`() {
        // Given
        val today =
            SimpleDateFormat(DateUtils.DATE_FORMAT_YYYYMMDD).format(Calendar.getInstance().time)
        // When
        val datePeriodType = DateUtils.datePeriod(today)
        // Then
        TestCase.assertEquals(DatePeriodType.TODAY, datePeriodType)
    }

    @Test
    fun `Given yesterday's date, when calculating date period, then verify Date Period`() {
        // Given
        val yesterday = Calendar.getInstance().apply { add(Calendar.DATE, -1) }.time
        val givenDate =
            SimpleDateFormat(DateUtils.DATE_FORMAT_YYYYMMDD).format(yesterday)
        // When
        val datePeriodType = DateUtils.datePeriod(givenDate)
        // Then
        TestCase.assertEquals(DatePeriodType.YESTERDAY, datePeriodType)
    }

    @Test
    fun `Given tomorrow's date, when calculating date period, then verify Date Period`() {
        // Given
        val tomorrow = Calendar.getInstance().apply { add(Calendar.DATE, +1) }.time
        val givenDate =
            SimpleDateFormat(DateUtils.DATE_FORMAT_YYYYMMDD).format(tomorrow)
        // When
        val datePeriodType = DateUtils.datePeriod(givenDate)
        // Then
        TestCase.assertEquals(DatePeriodType.TOMORROW, datePeriodType)
    }

    @Test
    fun `Given 2 days ago date, when calculating date period, then verify Date Period`() {
        // Given
        val twoDaysAgo = Calendar.getInstance().apply { add(Calendar.DATE, -2) }.time
        val givenDate =
            SimpleDateFormat(DateUtils.DATE_FORMAT_YYYYMMDD).format(twoDaysAgo)
        // When
        val datePeriodType = DateUtils.datePeriod(givenDate)
        // Then
        TestCase.assertEquals(DatePeriodType.WITHIN7DAYS, datePeriodType)
    }

    @Test
    fun `Given date between a week and month date, when calculating date period, then verify Date Period`() {
        // Given
        val twoDaysAgo = Calendar.getInstance().apply { add(Calendar.DATE, -10) }.time
        val givenDate =
            SimpleDateFormat(DateUtils.DATE_FORMAT_YYYYMMDD).format(twoDaysAgo)
        // When
        val datePeriodType = DateUtils.datePeriod(givenDate)
        // Then
        TestCase.assertEquals(DatePeriodType.WITHIN1MONTH, datePeriodType)
    }

    @Test
    fun `Given date between a month and 3 months date range, when calculating date period, then verify Date Period`() {
        // Given
        val twoDaysAgo = Calendar.getInstance().apply { add(Calendar.DATE, -60) }.time
        val givenDate =
            SimpleDateFormat(DateUtils.DATE_FORMAT_YYYYMMDD).format(twoDaysAgo)
        // When
        val datePeriodType = DateUtils.datePeriod(givenDate)
        // Then
        TestCase.assertEquals(DatePeriodType.WITHIN3MONTHS, datePeriodType)
    }

    @Test
    fun `Given date older than 100 days, when calculating date period, then verify Date Period`() {
        // Given
        val twoDaysAgo = Calendar.getInstance().apply { add(Calendar.DATE, -100) }.time
        val givenDate =
            SimpleDateFormat(DateUtils.DATE_FORMAT_YYYYMMDD).format(twoDaysAgo)
        // When
        val datePeriodType = DateUtils.datePeriod(givenDate)
        // Then
        TestCase.assertEquals(DatePeriodType.MORETHAN3MONTHS, datePeriodType)
    }

    @Test
    fun `Given date is null, when calculating date period, then verify Date Period`() {
        // Given
        val givenDate = null
        // When
        val datePeriodType = DateUtils.datePeriod(givenDate)
        // Then
        TestCase.assertEquals(DatePeriodType.INVALID_DATE, datePeriodType)
    }

    @Test
    fun `Given today's date, when formatting date, then verify returned string`() {
        // Given
        val today = SimpleDateFormat(DateUtils.DATE_FORMAT_YYYYMMDD).format(Calendar.getInstance().time)
        `when`(mockApplicationContext?.getString(anyInt())).thenReturn("Today")
        // When
        val formattedDate = DateUtils.formatDate(mockApplicationContext!!, today)
        // Then
        TestCase.assertEquals("Today", formattedDate)
    }

    @Test
    fun `Given yesterday's date, when formatting date, then verify returned string`() {
        // Given
        val yesterday = Calendar.getInstance().apply { add(Calendar.DATE, -1) }.time
        val inputDate = SimpleDateFormat(DateUtils.DATE_FORMAT_YYYYMMDD).format(yesterday)
        `when`(mockApplicationContext?.getString(anyInt())).thenReturn("One Day Ago")
        // When
        val formattedDate = DateUtils.formatDate(mockApplicationContext!!, inputDate)
        // Then
        TestCase.assertEquals("One Day Ago", formattedDate)
    }

    @Test
    fun `Given 5 days ago date, when formatting date, then verify returned string`() {
        // Given
        val fiveDaysAgoDate = Calendar.getInstance().apply { add(Calendar.DATE, -5) }.time
        val inputDate = SimpleDateFormat(DateUtils.DATE_FORMAT_YYYYMMDD).format(fiveDaysAgoDate)
        `when`(mockApplicationContext?.getString(anyInt(), anyString())).thenReturn("5 Days Ago")
        // When
        val formattedDate = DateUtils.formatDate(mockApplicationContext!!, inputDate)
        // Then
        TestCase.assertEquals("5 Days Ago", formattedDate)
    }

    @Test
    fun `Given date older than 7 days, when formatting date, then verify returned string`() {
        // Given
        val inputDate = "2024-01-21"
        // When
        val formattedDate = DateUtils.formatDate(mockApplicationContext!!, inputDate)
        // Then
        TestCase.assertEquals("21 January 2024", formattedDate)
    }

}